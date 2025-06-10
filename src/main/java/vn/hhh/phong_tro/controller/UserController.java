package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hhh.phong_tro.dto.request.user.UserRequest;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.dto.response.ResponseData;
import vn.hhh.phong_tro.dto.response.ResponseError;
import vn.hhh.phong_tro.dto.response.user.UserDetailResponse;
import vn.hhh.phong_tro.service.UserService;
import jakarta.validation.constraints.Min;
import vn.hhh.phong_tro.util.Uri;

@RestController
@Validated
@Slf4j
@Tag(name = "User Controller")
@RequestMapping(Uri.User)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
//    @PostMapping(value = "/")
//    public ResponseData<Long> addUser(@Valid @RequestBody UserRequest request) {
//        log.info("Request add user, {} {}", request.getName(), request.getPhone());
//        long userId = userService.addUser(request);
//        return new ResponseData<>(HttpStatus.CREATED.value(), "User has added successfully", userId);
//    }

    @Operation(summary = "Update user", description = "Send a request via this API to update user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable long userId, @ModelAttribute UserRequest request) {
        log.info("Request update userId={}", userId);

        try {
            userService.updateUser(userId, request);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User has updated successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");
        }

    }

    @Operation(summary = "Change status of user", description = "Send a request via this API to change status of user")
    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@PathVariable int userId) {
        log.info("Request change status, userId={}", userId);

        try {
            userService.changeStatus(userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status has changed successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user status fail");
        }
    }

    @Operation(summary = "Delete user permanently", description = "Send a request via this API to delete user permanently")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") long userId) {
        log.info("Request delete userId={}", userId);

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete user successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }
    }

    @Operation(summary = "Get user detail", description = "Send a request via this API to get user information")
    @GetMapping("/{userId}")
    public ResponseData<?> getUser(@PathVariable @Min(1) long userId) {
        log.info("Request get user detail, userId={}", userId);

        try {
            UserDetailResponse user = userService.getUserById(userId);
            return new ResponseData<>(HttpStatus.OK.value(), "user", user);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        try {
            PageResponse<?> users = userService.getAllUsers(pageNo, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "users", users);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

