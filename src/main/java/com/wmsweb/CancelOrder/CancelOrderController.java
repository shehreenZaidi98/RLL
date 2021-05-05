package com.wmsweb.CancelOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api")
public class CancelOrderController {
    @Autowired
    CancelOrderRepository cancelOrderRepository;

    @PostMapping("insertCancelOrder")
    public String insertData(@RequestBody CancelOrder cancelOrder){
        String message="UnSuccessful";
        List<CancelOrder>list=cancelOrderRepository.getSku(cancelOrder.getSku());
        if(list.size()>0){
            int update=cancelOrderRepository.updateCancelOrder(cancelOrder.getBay(),cancelOrder.getQty(),cancelOrder.getBatch(),cancelOrder.getSku());
            if(update>0){
                message="updated";
            }
        }else {
            int insert = cancelOrderRepository.insertCancelOrder(cancelOrder.getBay(),cancelOrder.getSku(),cancelOrder.getQty(),cancelOrder.getBatch());
            if (insert > 0) {
                message = "Successful";
            }
        }
        return  message;
    }
}
