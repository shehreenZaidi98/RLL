package com.wmsweb.FilterQty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FilterQtyService {
    @Autowired
    FilterQtyRepo filterQtyRepo;

    @GetMapping("getAllData")
    public Map<String, List<FilterQty>> getFilterData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<FilterQty> getFilterData = filterQtyRepo.getAllData(sdf.format(date));
        HashMap<String, List<FilterQty>> hMap = new HashMap<>();
        hMap.put("pData", getFilterData);

        return hMap;
    }
}
