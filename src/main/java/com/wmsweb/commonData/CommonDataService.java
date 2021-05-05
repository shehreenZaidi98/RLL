package com.wmsweb.commonData;

import com.wmsweb.Purchase.PurchaseRepository;
import com.wmsweb.SortingPurchase.SortingPurchase;
import com.wmsweb.SortingPurchase.SortingPurchaseRepository;
import com.wmsweb.transport.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CommonDataService {
    @Autowired
    CommonDataRepository commonDataRepository;
    @Autowired
    TransportRepository transportRepository;
    @Autowired
    SortingPurchaseRepository sortingPurchaseRepository;
    @Autowired
    PurchaseRepository purchaseRepository;


    @PostMapping("/updateCommonDataStatus")
    public String updateCommonDataStatus(@RequestParam("id") long id, @RequestParam("status") int status) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updateCommonDataStatus = commonDataRepository.updateCommonDataStatus(id, status);
        if (updateCommonDataStatus > 0) {
            List<CommonData> getCommonData = commonDataRepository.getCommonData(id);
            if (getCommonData.size() > 0) {
                int insert = transportRepository.updateStatus(getCommonData.get(0).getOrder_id(), status);
                if (insert > 0) {
                    message = "{\"message\":\"Updated Successfully\"}";
                }
            }
        }
        return message;
    }

    @GetMapping("/getCommonData")
    public Map<String, ArrayList<CommonData>> getTransportStatus() {
        HashMap<String, ArrayList<CommonData>> hmap = new HashMap<String, ArrayList<CommonData>>();
        ArrayList<CommonData> list = (ArrayList<CommonData>) commonDataRepository.getCommonData();
        hmap.put("CommonData", list);
        return hmap;
    }

    @GetMapping("orderComplete")
    public String OrderComplete(@RequestParam ("order_id")long order_id){
        String message = "{\"message\":\"Unsuccessful\"}";
        int updateComplete=commonDataRepository.orderComplete(order_id);
        int transportUpdate=transportRepository.transportOrderComplete(order_id);
        int purchaseUpdate=purchaseRepository.purchaseOrderComplete(order_id);

        if(updateComplete>0){
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }


}
