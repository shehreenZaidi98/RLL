package com.wmsweb.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PathController {
    @Autowired
    PathRepository pathRepository;

    @GetMapping("/checkPathPassword")
    public String checkPassword(@RequestParam("password") String password){
        List<Path> checkPPassword= pathRepository.checkPassword(password);
        String message = "{\"message\":\"Not Login\"}";

        if(checkPPassword.size()>0){
            message = "{\"message\":\"Successfully\"}";
        }

        return message;
    }
}
