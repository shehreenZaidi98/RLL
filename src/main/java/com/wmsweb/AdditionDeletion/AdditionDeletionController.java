package com.wmsweb.AdditionDeletion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping({"/api"})
public class AdditionDeletionController {
    @Autowired
    AdditionDeletionRepository additionDeletionRepository;

    @PostMapping("insertAdditionPassword")
    public String insertPassword(@RequestParam("password")String password){
        String message="UnSuccessful";
        List<AdditionDeletion>list=additionDeletionRepository.getPassword(password);
        if(list.size()>0){
            message="Not Required";
        }else {
            int insert = additionDeletionRepository.insertPassword(password);
            if (insert > 0) {
                message = "Inserted";
            }
        }
        return  message;
    }

    @PostMapping("updateAdditionPassword")
    public String updateAdditional(@RequestBody AdditionDeletion additionDeletion) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updateAdditional = additionDeletionRepository.updatePassword(additionDeletion.getPassword(),
                additionDeletion.getId());
        if (updateAdditional > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }

    @GetMapping("getPassword")
    public String checkPassword(@RequestParam("password")String password){
        String   message = "{\"message\":\"UnSuccessful\"}";
        List<AdditionDeletion>list=additionDeletionRepository.getPassword(password);
        if(list.size()>0){
            message = "{\"message\":\"Successful\"}";        }
        return message;

    }


}
