package com.wmsweb.Purchase;

import com.wmsweb.SortingPurchase.SortingPurchase;
import com.wmsweb.SortingPurchase.SortingPurchaseRepository;
import com.wmsweb.bayCapacity.BayCapacity;
import com.wmsweb.commonData.CommonDataRepository;
import com.wmsweb.permitNo.PermitNoRepository;
import com.wmsweb.production.Production;
import com.wmsweb.production.ProductionRepository;
import com.wmsweb.skuList.SkuList;
import com.wmsweb.transport.Transport;
import com.wmsweb.transport.TransportRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(origins = "*")
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    SortingPurchaseRepository sortingPurchaseRepository;
    @Autowired
    ProductionRepository productionRepository;
    @Autowired
    CommonDataRepository commonDataRepository;
    @Autowired
    TransportRepository transportRepository;
    @Autowired
    PermitNoRepository permitNoRepository;
    int i = 1;

    @PostMapping({"/insertPurchaseData"})
    public String insertData(@RequestBody Purchase purchase) throws InterruptedException {
        String message = "{\"message\":\"Unsuccessful\"}";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentDate = sdf.format(date);

        int insertData = purchaseRepository.insertData(purchase.getOrder_id(), purchase.getPermit_no(), purchase.getSku(), purchase.getQty(), CurrentDate);
        if (insertData > 0) {
            message = "{\"message\":\"Successful\"}";
            try {
                //    commonDataRepository.insertData(purchase.getOrder_id(), "order", "1", CurrentDate);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Thread.sleep(2000);
        makeSorting(purchase.getOrder_id(), purchase.getSku());
        return message;
    }

    @PostMapping("/upadatePurchaseStatus")
    public String updatePurchaseStatus(@RequestBody Purchase purchase) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updatePurchaseStatus = purchaseRepository.updatePurchaseStatus(purchase.getOrder_id());
        if (updatePurchaseStatus > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }

    @GetMapping({"/getPurchaseDetails"})
    public Map<String, ArrayList<Purchase>> getpurchaseDetails() {
        HashMap<String, ArrayList<Purchase>> hmap = new HashMap<String, ArrayList<Purchase>>();
        ArrayList<Purchase> list = (ArrayList<Purchase>) purchaseRepository.getPurchaseDetails();
        hmap.put("purchaseDetails", list);
        return hmap;
    }

    public void makeSorting(long order_id, String sku) {
        sortingPurchaseRepository.deleteQty();
        List<Purchase> getUniqueList = purchaseRepository.getPurchaseList(order_id, sku);
        HashSet<Purchase> uniqueList = new HashSet<Purchase>(getUniqueList);
        for (Purchase uPurchase : uniqueList) {
            int purchaseQty = 0;
            int increment = 1;
            List<Purchase> getPurchaseData = purchaseRepository.getPurchaseDetails(uPurchase.getSku(), order_id);
            for (Purchase purchase : getPurchaseData) {
                purchaseQty += purchase.getQty();
            }
            List<Production> getProductionData = productionRepository.getProductionData(uPurchase.getSku(), "PASS");
            for (int count = 0; count < increment; ++count) {
                int mainQty = 0;
                List<SortingPurchase> getSortingPurchase = sortingPurchaseRepository.getSortingPurchase(
                        getProductionData.get(count).getSku(), getProductionData.get(count).getBatch_no()
                        , getProductionData.get(count).getBay_no()
                );
                if (getSortingPurchase.size() > 0) {
                    int qty = getProductionData.get(count).getQty();
                    int tmpQty = 0;
                    for (SortingPurchase sp : getSortingPurchase) {
                        tmpQty += sp.getQty();
                    }
                    int tQty = qty - tmpQty;
                    if (tQty <= 0) {
                        ++increment;
                    } else if (tQty > 0) {
                        int sendQty = 0;
                        if (tQty > purchaseQty) {
                            sendQty = purchaseQty;
                        } else {
                            mainQty += purchaseQty - tQty;
                            sendQty = mainQty;
                        }


                        if (mainQty <= 0) {
                            System.out.println();
                            sortingPurchaseRepository.insertData(uPurchase.getOrder_id(), uPurchase.getPermit_no(), uPurchase.getSku(), getProductionData.get(count).getBatch_no(), getProductionData.get(count).getBay_no(), sendQty, 0, uPurchase.getDate());
                            System.out.println("first_1" + mainQty);
                        } else if (mainQty > 0) {
                            sortingPurchaseRepository.insertData(uPurchase.getOrder_id(), uPurchase.getPermit_no(), uPurchase.getSku(), getProductionData.get(count).getBatch_no(), getProductionData.get(count).getBay_no(), tQty, 0, uPurchase.getDate());
                            System.out.println("second_1" + mainQty);
                            purchaseQty -= tQty;
                            ++increment;
                        }
                    }
                } else {
                    int sendQty = 0;
                    if (mainQty > 0) {
                        sendQty = mainQty;

                    } else if (mainQty == 0) {
                        mainQty += purchaseQty - getProductionData.get(count).getQty();

                        sendQty = purchaseQty;

                    }


                    if (mainQty <= 0) {
                        sortingPurchaseRepository.insertData(uPurchase.getOrder_id(), uPurchase.getPermit_no(), uPurchase.getSku(), getProductionData.get(count).getBatch_no(), getProductionData.get(count).getBay_no(), sendQty, 0, uPurchase.getDate());
                        System.out.println("first_2" + mainQty);
                    } else if (mainQty > 0) {
                        sortingPurchaseRepository.insertData(uPurchase.getOrder_id(), uPurchase.getPermit_no(), uPurchase.getSku(), getProductionData.get(count).getBatch_no(), getProductionData.get(count).getBay_no(), getProductionData.get(count).getQty(), 0, uPurchase.getDate());
                        System.out.println("second_2" + mainQty);
                        purchaseQty -= getProductionData.get(count).getQty();
                        ++increment;
                    }
                }
            }
            purchaseRepository.updatePurchaseStatus(uPurchase.getOrder_id());
        }
    }

    @PostMapping({"/changeStatusWithOrderid"})
    public String changeStatusWithOrderid(@RequestParam("order_id") long order_id) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updatePurchaseStatus = purchaseRepository.runChangeStatusWithOrderId(order_id);
        if (updatePurchaseStatus > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }

    @PostMapping({"/insertBatchPurchaseData"})
    public String insertBatchPurchaseData(@RequestBody Purchase purchase) throws InterruptedException {
        String message = "{\"message\":\"Unsuccessful\"}";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentDate = sdf.format(date);
        int insertData = purchaseRepository.insertData(purchase.getOrder_id(),
                purchase.getPermit_no(), purchase.getSku(), purchase.getQty(),
                CurrentDate, purchase.getBay_no(), purchase.getBatch_no());
        if (insertData > 0) {
            int insert = sortingPurchaseRepository.insertData(purchase.getOrder_id(),
                    purchase.getPermit_no(), purchase.getSku(), purchase.getBatch_no(),
                    purchase.getBay_no(), purchase.getQty(), 0, CurrentDate);
            if (insert > 0) {
                message = "{\"message\":\"Successful\"}";
            }
        }
        return message;
    }


    @PostMapping("/uploadDispatchPlan")
    public String uploadDispatchPlan(@RequestParam("file") MultipartFile file) throws InterruptedException {
        Workbook workbook = null;
        try {
            workbook = (Workbook) new XSSFWorkbook(file.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        XSSFSheet sheet2 = (XSSFSheet) workbook.getSheetAt(1);
        String response = "{\"message\":\"Unsuccessful\"}";
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i).getCell(0).getCellType() != CellType.BLANK) {
                String permitNo = "", address = "", state = "", partyName = "";
                int orderId = 0;

                if (sheet.getRow(i).getCell(1).getCellType() == CellType.STRING) {
                    partyName = sheet.getRow(i).getCell(1).getStringCellValue();
                } else if (sheet.getRow(i).getCell(1).getCellType() == CellType.NUMERIC) {
                    partyName = String.valueOf(sheet.getRow(i).getCell(1).getNumericCellValue());
                }
                if (sheet.getRow(i).getCell(2).getCellType() == CellType.STRING) {
                    address = sheet.getRow(i).getCell(2).getStringCellValue();
                } else if (sheet.getRow(i).getCell(2).getCellType() == CellType.NUMERIC) {
                    address = String.valueOf(sheet.getRow(i).getCell(2).getNumericCellValue());
                }
                if (sheet.getRow(i).getCell(3).getCellType() == CellType.STRING) {
                    state = sheet.getRow(i).getCell(3).getStringCellValue();
                } else if (sheet.getRow(i).getCell(3).getCellType() == CellType.NUMERIC) {
                    state = String.valueOf(sheet.getRow(i).getCell(3).getNumericCellValue());
                }
                orderId = (int) sheet.getRow(i).getCell(0).getNumericCellValue();
                Transport transport = new Transport();
                transport.setAddress(address);
                transport.setParty_name(partyName);
                transport.setPermit_no(permitNo);
                transport.setState(state);
                transport.setDriver_name("");
                transport.setContact_no("");
                transport.setVehicle_no("");
                transport.setTruck_bay_no("");
                String allSku = "";
                int totalQuantity = 0;

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                transport.setDate(sdf.format(date));
                Transport t1 = transportRepository.save(transport);
                for (int j = 1; j <= sheet2.getLastRowNum(); j++) {
                    int purchaseQty = 0;
                    String sku = "";
                    if (sheet2.getRow(j).getCell(1).getCellType() == CellType.STRING) {
                        sku = sheet2.getRow(j).getCell(1).getStringCellValue();
                    }
                    List<SortingPurchase> getPurchaseQty = (List<SortingPurchase>) sortingPurchaseRepository.getSortingPurchase(sku);
                    for (SortingPurchase sortingPurchase : getPurchaseQty) {
                        purchaseQty += sortingPurchase.getQty();
                    }
                    int qty1 = 0, qty = 0;
                    ArrayList<Production> allData = (ArrayList<Production>) productionRepository.getQuantity(sku, "PASS");
                    for (Production production : allData) {
                        qty1 += production.getQty();
                    }
                    if (sheet2.getRow(j).getCell(2).getCellType() == CellType.NUMERIC) {
                        qty = (int) sheet2.getRow(j).getCell(2).getNumericCellValue();
                    }
                    if (qty < (qty1 - purchaseQty)) {

                        List<String> productionList = productionRepository.getAllSku(sku);
                        if (productionList.size() > 0) {

                            int order_id = (int) sheet2.getRow(j).getCell(0).getNumericCellValue();
                            if (orderId == order_id) {

                                if (t1.getOrder_id() != 0) {

                                    if (sheet2.getRow(j).getCell(3).getCellType() == CellType.STRING) {
                                        permitNo = sheet2.getRow(j).getCell(3).getStringCellValue();
                                    } else if (sheet2.getRow(j).getCell(3).getCellType() == CellType.NUMERIC) {
                                        permitNo = String.valueOf(sheet2.getRow(j).getCell(3).getNumericCellValue());
                                    }

                                    int insert = purchaseRepository.insertData(t1.getOrder_id(), permitNo,
                                            sku, qty, sdf.format(date));
                                    if (insert > 0) {
                                        response = "{\"message\":\"Successful\"}";
                                    }
                                    totalQuantity += qty;
                                    allSku += sku + "(" + qty + ") ";
                                    Thread.sleep(2000);
                                    makeSorting(t1.getOrder_id(), sku);
                                }
                                transportRepository.updateTransport(t1.getOrder_id(),
                                        String.valueOf(totalQuantity), allSku);

                            }
                        }
                    } else {
                        response = "{\"message\":\"Unsuccessful\"}";

                    }
                }

            }

        }

        return response;
    }


}
