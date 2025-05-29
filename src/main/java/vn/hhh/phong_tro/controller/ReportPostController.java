package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.ReportRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.dto.response.post.PostDetailResponse;
import vn.hhh.phong_tro.dto.response.post.ReportedPostDto;
import vn.hhh.phong_tro.service.ReportPostService;
import vn.hhh.phong_tro.util.Uri;

import java.util.List;

@RestController
@RequestMapping(Uri.Report)
@RequiredArgsConstructor
public class ReportPostController {
    private final ReportPostService reportPostService;

    @PostMapping
    public ResponseData<?> reportPost(@RequestBody ReportRequest request) {
        try {
            reportPostService.reportPost(request.getPostId(), request.getUserId(), request.getReason());
            return new ResponseData<>(HttpStatus.CREATED.value(), "Reported successfully");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/admin")
    public ResponseData<?> getReportedPosts() {

        try {
             List<ReportedPostDto> list =reportPostService.getReportedPosts();
            return new ResponseData<>(HttpStatus.OK.value(), "List Reported ",list);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

}
