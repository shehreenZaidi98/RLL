package com.wmsweb.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.wmsweb.AdditionDeletion.AdditionDeletion;
import com.wmsweb.AdditionDeletion.AdditionDeletionRepository;

import java.util.List;

public class LoginInsertController {

    @Autowired
    LoginRepository loginRepository;

    public String insertLoginPassword(){
    	 List<Login>list=(List<Login>) loginRepository.findAll();
         if(list.size()==0){
        	 Login login=new Login();
        	 login.setPassword("12345");
        	 login.setUser_name("pernod");
          loginRepository.save(login);
         }
        return "save Addition password";
    }
}
