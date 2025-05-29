package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.dto.request.VerificationRequest;
import vn.hhh.phong_tro.exception.ResourceNotFoundException;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.model.Verify;
import vn.hhh.phong_tro.repository.VerifyRepository;
import vn.hhh.phong_tro.util.VerifyStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerifyService {
    private final VerifyRepository verifyRepository;
    private final UserService userService;
    private final S3Service s3Service;

    public Integer submitVerification(VerificationRequest request) throws IOException {

        // 1. Upload ảnh lên S3
        MultipartFile file = request.getFrontImageUrl();
        String ImageUrl = "";
        if (file != null) {
            ImageUrl =  s3Service.upload(file);
        }
        // 2. Hash CCCD
        String hashedCCCD = hashCCCD(request.getCccdNumber());
        User user = userService.getById(request.getUserId());
        // 3. Lưu vào DB
        Verify verification = new Verify();
        verification.setUser(user);
        verification.setCccdNumber(hashedCCCD);
        verification.setFrontImageUrl(ImageUrl);
        verification.setExtractedName(request.getExtractedName());
        verification.setExtractedDob(request.getExtractedDob());
        verification.setExtractedAddress(request.getExtractedAddress());
        verification.setStatus(VerifyStatus.PENDING);
        verifyRepository.save(verification);

        return Math.toIntExact(verification.getId());
    }

    public void approveVerification(Integer id, VerifyStatus status) {
        Verify verification = verifyRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Verification not found"));
        if (!verification.getFrontImageUrl().isEmpty()) {
            s3Service.delete(s3Service.extractKeyFromUrl(verification.getFrontImageUrl()));

        }
        verification.setStatus(status);
        verification.setApprovedAt(LocalDateTime.now());
        verifyRepository.save(verification);
    }
    public Page<Verify> getByStatus(VerifyStatus status, Pageable pageable) {
        return verifyRepository.findByStatus(status, pageable);
    }

    public boolean checkIfUserVerified(Long userId) {
        return verifyRepository.existsByUserIdAndStatus(userId, VerifyStatus.APPROVED);
    }

    public String hashCCCD(String cccdNumber) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(cccdNumber.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

}
