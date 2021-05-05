package com.wmsweb.companyCode;



import org.springframework.beans.factory.annotation.Autowired;

import com.wmsweb.AdditionDeletion.AdditionDeletion;
import com.wmsweb.AdditionDeletion.AdditionDeletionRepository;

import java.util.List;

public class CompanyCodeInsertController {

    @Autowired
    CompanyCodeRepository companyCodeRepository;

    public String insertCompanyCodePassword(){
    	 List<CompanyCode>list=(List<CompanyCode>) companyCodeRepository.findAll();
         if(list.size()==0){
        	 CompanyCode companyCode=new CompanyCode();
        	 companyCode.setCompany_code(11);
          companyCodeRepository.save(companyCode);
         }
        return "save CompanyCode password";
    }
}
