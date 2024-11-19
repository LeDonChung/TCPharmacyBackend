package vn.edu.iuh.fit.pharmacy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {
    @GetMapping({"hello"})
    public String hello() {
        return "Hello World!";
    }
}
