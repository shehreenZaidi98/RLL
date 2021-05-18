package com.wmsweb.clientRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ClientRegistrationController {
    @Autowired
    ClientRegistrationRepo clientRegistrationRepo;

    @PostMapping("insertClientRegistration")
    public String insertClientRegistration(@RequestBody ClientRegistration clientRegistration){
        String message="UnSuccessful";
        int insert=clientRegistrationRepo.insertClientRegistration(clientRegistration.getParty_name(),
                clientRegistration.getPhone(),clientRegistration.getEmail(),clientRegistration.getGst(),
                clientRegistration.getPan(),clientRegistration.getAddress(),clientRegistration.getCountry(),
                clientRegistration.getState(),clientRegistration.getPin_code());
        if(insert>0){
            message="Inserted";
        }
        return message;
    }

    @GetMapping("getAllPartyNameList")
    public Map<String, HashSet<String>> getPartyName() {
        HashSet<String> set = (HashSet<String>) this.clientRegistrationRepo.getPartyName();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("party", set);
        return hmap;
    }

    @GetMapping("getClientRegistrationDetails")
    public Map<String, List<ClientRegistration>>getClientRegistrationDetails(@RequestParam("party_name")String party_name){
        List<ClientRegistration>clientRegistrations=clientRegistrationRepo.getClientRegistration(party_name);
        HashMap<String,List<ClientRegistration>>hMap=new HashMap<>();
        hMap.put("details",clientRegistrations);
        return hMap;

    }







}
