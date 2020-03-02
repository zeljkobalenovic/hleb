package baki.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 */

@RestController
@RequestMapping("/api")
 public class HelloController {

    @GetMapping("/home/hello")
    public String helloHome(){
        return "Hello Home";
    }

    @GetMapping("/user/hello")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String helloUser(){
        return "Hello User";
    }

   @GetMapping("/employee/hello")
   @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public String helloEmployee(){
        return "Hello Employee";
    }

    @GetMapping("/manager/hello")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public String helloManager(){
        return "Hello Manager";
    }

    @GetMapping("/admin/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin(){
        return "Hello Admin";
    }

    @GetMapping(value = "/product")
    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    public String readProduct(){
        return "product read";
    }

    @PostMapping(value = "/product")
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public String writeProduct(){
        return "product write";
    }

    @GetMapping(value = "/order")
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public String readOrder(){
        return "order read";
    }

    @PostMapping(value = "/order")
    @PreAuthorize("hasAuthority('ORDER_WRITE')")
    public String writeOrder(){
        return "order write";
    }

    
}