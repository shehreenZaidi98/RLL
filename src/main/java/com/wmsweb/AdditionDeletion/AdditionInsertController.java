package com.wmsweb.AdditionDeletion;


import org.springframework.beans.factory.annotation.Autowired;

import com.wmsweb.AdditionDeletion.AdditionDeletion;
import com.wmsweb.AdditionDeletion.AdditionDeletionRepository;

import java.util.List;

public class AdditionInsertController {

    @Autowired
    AdditionDeletionRepository additionDeletionRepository;

    public String insertAddProPassword(){
    	 List<AdditionDeletion>list=(List<AdditionDeletion>) additionDeletionRepository.findAll();
         if(list.size()==0){
          additionDeletionRepository.insertPassword("pernod@123");
         }
        return "save Addition password";
    }
}
