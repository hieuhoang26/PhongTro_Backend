package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.VerificationRequest;
import vn.hhh.phong_tro.dto.request.post.UpdatePostRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.model.Verify;
import vn.hhh.phong_tro.service.VerifyService;
import vn.hhh.phong_tro.util.Uri;
import vn.hhh.phong_tro.util.VerifyStatus;

@RestController
@Validated
@Slf4j
@Tag(name = "Verify Controller")
@RequestMapping(Uri.Verify)
@RequiredArgsConstructor
public class VerifyController {
    private  final VerifyService verifyService;

    @Operation(summary = "Xác thực người dùng ", description = "")
    @PostMapping()
    public ResponseData<?> identify(@ModelAttribute VerificationRequest request) {
        try {

            return new ResponseData<>(HttpStatus.OK.value(), " success",verifyService.submitVerification(request));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(summary = "approve identifier", description = "")
    @PatchMapping()
    public ResponseData<?> updateP(@RequestParam Integer id, @RequestParam VerifyStatus status) {
        try {
            verifyService.approveVerification(id,status);
            return new ResponseData<>(HttpStatus.OK.value(), "Update success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get List for Admin", description = "")
    @GetMapping("/list")
    public ResponseData<?> listForAdmin(
            @RequestParam(name = "status", defaultValue = "PENDING") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size

    ) {
        try {
            VerifyStatus parsedStatus = VerifyStatus.valueOf(status.toUpperCase());
            Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable pageable = PageRequest.of(page, size, sortObj);
            Page<Verify> list = verifyService.getByStatus(parsedStatus, pageable);
            return new ResponseData<>(HttpStatus.OK.value(), "Get List success",list);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
