package vn.hhh.phong_tro.service;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.config.s3.S3BucketClient;

@Service
@Slf4j(topic = "S3-SERVICE")
@RequiredArgsConstructor
public class S3Service {

    private final S3BucketClient s3BucketClient;
    private final String bucketName = "phongtrodatn";

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be blank");
        }

        log.info("Uploading file to bucket: {}", bucketName);
        return s3BucketClient.putObject(bucketName, file, true); // true = public
    }
}
