package com.wmsweb;

import com.wmsweb.AdditionDeletion.AdditionInsertController;
import com.wmsweb.FifoViolate.FifoInsertController;
import com.wmsweb.commonData.CommonDataController;
import com.wmsweb.companyCode.CompanyCodeInsertController;
import com.wmsweb.login.LoginInsertController;
import com.wmsweb.path.PathInsertController;
import com.wmsweb.path.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class WmswebApplication {
    @Autowired
    PathRepository pathRepository;
    public static void main(String[] args) throws IOException {

    	SpringApplication.run(WmswebApplication.class, args);
        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:9080/login"});
        }

    @Bean(initMethod = "deleteHoldOrder")
    public CommonDataController deleteHoldOrder() {
        return new CommonDataController();
    }

    @Bean(initMethod = "insertPathPassword")
    public PathInsertController insertPathPassword() {
        return new PathInsertController();
    }

    @Bean(initMethod = "insertAddProPassword")
    public AdditionInsertController insertAddProPassword() {
        return new AdditionInsertController();
    }

    
    @Bean(initMethod = "insertFifoPassword")
    public FifoInsertController insertFifoPassword() {
        return new FifoInsertController();
    }
    
    
    @Bean(initMethod = "insertLoginPassword")
    public LoginInsertController insertLoginPassword() {
        return new LoginInsertController();
    }

    
    @Bean(initMethod = "insertCompanyCodePassword")
    public CompanyCodeInsertController insertCompanyCodePassword() {
        return new CompanyCodeInsertController();
    }

}
