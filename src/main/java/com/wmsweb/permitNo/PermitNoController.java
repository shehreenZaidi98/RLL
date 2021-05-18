package com.wmsweb.permitNo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PermitNoController {

    @Autowired
    PermitNoRepository permitNoRepository;

    @GetMapping("/getPermitNo")
    public Map<String, HashSet<String>> getPermitNo() {
        List<String> list = permitNoRepository.getPermitList();
        HashSet<String> hSet = new HashSet<String>(list);
        HashMap<String, HashSet<String>> hMap = new HashMap<String, HashSet<String>>();
        hMap.put("permit", hSet);
        return hMap;
    }
}
