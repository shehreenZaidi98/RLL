package com.wmsweb.path;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PathInsertController {

    @Autowired
    PathRepository pathRepository;

    public String insertPathPassword(){
        Path path=new Path();
        path.setPassword("admin@123");
        List<Path> path1= (List<Path>) pathRepository.findAll();
        if (path1.size()==0) {
            pathRepository.save(path);
        }
        return "save path password";
    }
}
