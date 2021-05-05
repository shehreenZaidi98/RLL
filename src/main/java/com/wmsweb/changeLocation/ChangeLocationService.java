// 
// Decompiled by Procyon v0.5.36
// 

package com.wmsweb.changeLocation;

import com.wmsweb.commonData.CommonDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(origins = "*")
public class ChangeLocationService {
    @Autowired
    ChangeLocationRepository changelocationRepository;
    @Autowired
    CommonDataRepository commonDataRepository;

    @PostMapping("/insertChangeLocation")
    public String insertaData(@RequestBody ChangeLocation changelocation) {
        String message = "{\"message\":\"Unsuccessful\"}";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentDate = sdf.format(date);
        changelocation.setDate(CurrentDate);
        ChangeLocation ch = changelocationRepository.save(changelocation);
        if (ch.getId() != 0L) {
            message = "{\"message\":\"Successful\"}";
            //commonDataRepository.insertData(ch.getId(), "change_location", "1", CurrentDate ,ch.getBay_no());
        }
        return message;
    }

    @PostMapping("/updateLocationStatus")
    public String updateGoodstatus(@RequestParam("id") long id) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updateLocationstatus = this.changelocationRepository.updateLocationstatus(id);
        if (updateLocationstatus > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }

    @GetMapping("/getLocationChange")
    public Map<String, ArrayList<ChangeLocation>> getLocationStatus(@RequestParam("id") long id) {
        HashMap<String, ArrayList<ChangeLocation>> hmap = new HashMap<String, ArrayList<ChangeLocation>>();
        ArrayList<ChangeLocation> list = (ArrayList<ChangeLocation>) changelocationRepository.getLocationChange(id);
        hmap.put("LocationChange", list);
        return hmap;
    }
}
