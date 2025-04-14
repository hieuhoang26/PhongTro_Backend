package vn.hhh.phong_tro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @PreAuthorize("hasAuthority('VIEW_LISTING')")
    @GetMapping("/hi")
    public String Demo(){
        return "Welcome";
    }

}
