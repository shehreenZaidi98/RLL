package com.wmsweb.FifoViolate;



import org.springframework.beans.factory.annotation.Autowired;

import com.wmsweb.AdditionDeletion.AdditionDeletion;
import com.wmsweb.AdditionDeletion.AdditionDeletionRepository;

import java.util.List;

public class FifoInsertController {

    @Autowired
    FifoViolateRepository fifoViolateRepository;

    public String insertFifoPassword(){
    	List<FifoViolate>list=(List<FifoViolate>) fifoViolateRepository.findAll();
        if(list.size()==0){
            fifoViolateRepository.insertFifoPassword("admin@123");
                   }
        return "save Fifo password";
    }
}
