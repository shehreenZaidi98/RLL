package com.wmsweb.commonData;

import com.wmsweb.SortingPurchase.SortingPurchase;
import com.wmsweb.SortingPurchase.SortingPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommonDataController {
    @Autowired
    SortingPurchaseRepository sortingPurchaseRepository;
    @Autowired
    CommonDataRepository commonDataRepository;

    public String deleteHoldOrder() {
        List<CommonData> getCommonData = commonDataRepository.getCommonData1();
        for (CommonData commonData : getCommonData) {
            List<SortingPurchase> getPurchaseList = sortingPurchaseRepository.getOrderProduct(commonData.getOrder_id());
            System.out.println("Working");
            if (getPurchaseList.size() == 0) {
                commonDataRepository.cancelOrder(commonData.getOrder_id());
                System.out.println("Hold Data Deleted");
            }
        }
        return "deleted";
    }
}
