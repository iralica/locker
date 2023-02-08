package com.example.locker;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/secure")
    public String secure(/* Principal principal */) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails details = null;
        if(principal instanceof UserDetails)
        {
            details = (UserDetails) principal;

            System.out.println("user: " + details.getUsername());
            System.out.println("roles: " + details.getAuthorities());
        }
        return "secure";
    }

    @GetMapping("/open")
    public String open(){
        return "open";
    }
}