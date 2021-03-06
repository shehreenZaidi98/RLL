

package com.wmsweb.bayCapacity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(origins = "*")
public class BayCapacityController {
    @Autowired
    BayCapacityRepository bayCapacityRepository;

    @PostMapping("/insertBay")
    public String insertBay(@RequestBody BayCapacity bay) {
        List<BayCapacity> bayList = this.bayCapacityRepository.getBay(bay.getBay());
        String response = "{\"message\":\"Unsuccessful\"}";
        if (bayList.size() > 0) {
            int update = this.bayCapacityRepository.updateBay(bay.getBay(), bay.getCapacity());
            if (update > 0) {
                response = "{\"message\":\"Successful\"}";
            }
        } else {
            int insert = this.bayCapacityRepository.insertBay(bay.getBay(), bay.getCapacity());
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";
            }
        }
        return response;
    }

    @GetMapping("/getBayList")
    public Map<String, ArrayList<BayCapacity>> getBayList() {
        ArrayList<BayCapacity> bayList = (ArrayList<BayCapacity>) bayCapacityRepository.getBay();
        HashMap<String, ArrayList<BayCapacity>> hmap = new HashMap<String, ArrayList<BayCapacity>>();
        hmap.put("bay", bayList);
        return hmap;
    }
}
