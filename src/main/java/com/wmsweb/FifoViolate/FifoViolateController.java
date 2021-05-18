package com.wmsweb.FifoViolate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api")
public class FifoViolateController {
    @Autowired
    FifoViolateRepository fifoViolateRepository;

    @PostMapping("insertFifoPassword")
    public String insertFifo(@RequestBody FifoViolate fifoViolate){
        String message="UnSuccessful";
        List<FifoViolate>list=fifoViolateRepository.getPassword(fifoViolate.getPassword());
        if(list.size()>0){
            message="Not Required";
        }else {
            int insert = fifoViolateRepository.insertFifoPassword(fifoViolate.getPassword());
            if (insert > 0) {
            message="Inserted";
            }
        }
        return message;
    }

    @PostMapping("updateFifoPassword")
    public String updateFifoPassword(@RequestBody FifoViolate fifoViolate){
        String message="Not Updated";
        int update=fifoViolateRepository.updateFifoPassword(fifoViolate.getPassword(),fifoViolate.getId());
        if(update>0){
            message="Updated";

        }
        return message;
    }

    @GetMapping("getFifoPassword")
    public String CheckFifoPassword(@RequestParam("password")String password) {
        String   message = "{\"message\":\"UnSuccessful\"}";
        List<FifoViolate> list = fifoViolateRepository.getPassword(password);
        if (list.size() > 0) {
            message = "{\"message\":\"Login\"}";
        }
        return message;
    }

}
