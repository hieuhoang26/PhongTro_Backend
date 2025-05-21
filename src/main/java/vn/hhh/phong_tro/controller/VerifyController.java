package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.VerificationRequest;
import vn.hhh.phong_tro.dto.request.post.UpdatePostRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.service.VerifyService;
import vn.hhh.phong_tro.util.Uri;

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
    @PutMapping()
    public ResponseData<?> updateP(@RequestParam Integer id) {
        try {
            verifyService.approveVerification(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Update success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
