package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.service.StatisticService;
import vn.hhh.phong_tro.util.PostStatus;
import vn.hhh.phong_tro.util.Uri;

@RestController
@Slf4j
@Tag(name = "Statistic Controller")
@RequestMapping(Uri.Statistic)
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;
    @GetMapping("/card")
    public ResponseData<?> getStatisticCard() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), " success", statisticService.getCardStats());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/post-by-date")
    public ResponseData<?> getPostByDate() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), " success", statisticService.getPostStatisticsByDate());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @GetMapping("/revenue-by-date")
    public ResponseData<?> getRevenueStatsByDate() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), " success", statisticService.getRevenueStatisticsByDate());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/post-by-status")
    public ResponseData<?> getPostCountByStatus() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), " success", statisticService.getPostCountByStatus());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @GetMapping("/post-by-type")
    public ResponseData<?> getPostCountByType() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), " success", statisticService.getPostCountByType());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
