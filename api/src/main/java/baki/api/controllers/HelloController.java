package baki.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 */

@RestController
@RequestMapping("/api")
 public class HelloController {

    @GetMapping("/hello")
    
    public String hello(){
        return "{\"message\":\"Hello from SpringBoot 5\"}";
    }
}