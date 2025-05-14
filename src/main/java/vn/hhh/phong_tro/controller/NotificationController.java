package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.NotificationRequest;
import vn.hhh.phong_tro.dto.request.post.CreatePostRequest;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.model.Notification;
import vn.hhh.phong_tro.service.NotificationService;
import vn.hhh.phong_tro.util.Uri;

@RestController
@RequestMapping(Uri.Notifications)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(
                request.getUserId(),
                request.getType(),
                request.getTitle(),
                request.getContent()
        );
    }

    @Operation(summary = "Get Noti for User", description = "")
    @GetMapping
    public ResponseData<?> getUserNotifications(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        try {
            return new ResponseData<>(
                    HttpStatus.OK.value(),
                    "Get success",
                    notificationService.getNotificationsForUser(Long.valueOf(userId), page, size)
            );
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Mark Read", description = "")
    @PutMapping("/read")
    public ResponseData<?> markAsRead(@RequestParam Integer id) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Mark success", notificationService.markRead(Long.valueOf(id)));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @Operation(summary = "Mark All Read", description = "")
    @PutMapping("/read-all")
    public ResponseData<?> markAllRead(@RequestParam Integer userId) {
        try {
            notificationService.markAllRead(Long.valueOf(userId));
            return new ResponseData<>(HttpStatus.OK.value(), "Mark All success");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


}
