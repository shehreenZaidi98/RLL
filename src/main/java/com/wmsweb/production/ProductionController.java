

package com.wmsweb.production;

import com.wmsweb.FilterQty.FilterQty;
import com.wmsweb.FilterQty.FilterQtyRepo;
import com.wmsweb.SortingPurchase.SortingPurchase;
import com.wmsweb.SortingPurchase.SortingPurchaseRepository;
import com.wmsweb.bayCapacity.BayCapacity;
import com.wmsweb.bayCapacity.BayCapacityRepository;
import com.wmsweb.companyCode.CompanyCodeRepository;
import com.wmsweb.manualOrder.ManualOrder;
import com.wmsweb.manualOrder.ManualOrderRepository;
import com.wmsweb.model.ProductionModel;
import com.wmsweb.productionPlan.ProductionPlan;
import com.wmsweb.productionPlan.ProductionPlanRepository;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(origins = "*")
public class ProductionController {
    @Autowired
    ProductionRepository productionRepository;
    @Autowired
    BayCapacityRepository bayCapacityRepository;
    @Autowired
    SortingPurchaseRepository sortingPurchaseRepository;
    @Autowired
    FilterQtyRepo filterQtyRepo;
    @Autowired
    ProductionPlanRepository productionPlanRepository;
    @Autowired
    CompanyCodeRepository companyCodeRepository;
    @Autowired
    ManualOrderRepository ManualOrderRepository;


    @PostMapping("/insertProduction")
    public String insertProduction(@RequestBody Production production,
                                   @RequestParam("line_no") String line_no) {
        int totQty = 0;
        int todayProPlan = 0;
        Date date = new Date();
        System.out.println("Bay:-"+production.getBay_no()+"Sku:-"+production.getSku()+"batch:-"+production.getBatch_no()
        +"Qty:-"+production.getQty()+"line:-"+line_no);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        String response = "{\"message\":\"Unsuccessful\"}";
        if (production.getStatus() == null) {
            production.setStatus("PASS");
        }
        List<ProductionPlan> getProductionPlans=productionPlanRepository.getTodayProductionPlan(
        		production.getSku(), sdf1.format(date)+" 00:00:00", sdf.format(date), line_no, production.getBatch_no());
        if(getProductionPlans.size()>0) {
        
        List<FilterQty> fList = filterQtyRepo.getFilterData(sdf1.format(date),
                production.getSku(), line_no, production.getBatch_no());

        for (FilterQty i : fList) {

            totQty += i.getQty();

        }


        List<ProductionPlan> pList = productionPlanRepository.getProductionPlan(production.getSku());

        for (ProductionPlan list : pList) {
            if (line_no.trim().contentEquals(list.getLine_no().trim())
                    && production.getBatch_no().equals(list.getBatch_no())) {
                todayProPlan = list.getQty();

            }
        }

        if (totQty < todayProPlan && totQty + production.getQty() <= todayProPlan) {

            List<FilterQty> getFilter = filterQtyRepo.getFilterData(sdf1.format(date), production.getSku(),
                    line_no, production.getBatch_no(), production.getBay_no());
            if (getFilter.size() > 0) {
                filterQtyRepo.updateData(production.getSku(), production.getQty() + getFilter.get(0).getQty(),
                        sdf1.format(date), line_no, production.getBatch_no(), production.getBay_no());
            } else {
                filterQtyRepo.insertData(production.getSku(), production.getQty()
                        , sdf1.format(date), line_no, production.getBatch_no(), production.getBay_no());
            }
            List<Production> productionList = productionRepository.getProductionData(production.getBatch_no(),
                    production.getSku(), production.getBay_no(), production.getStatus());
            if (productionList.size() > 0) {
                int update = productionRepository.updateProduction(production.getBatch_no(), sdf.format(date)
                        , productionList.get(0).getQty() + production.getQty()
                        , production.getSku(), production.getBay_no(), production.getStatus());
                if (update > 0) {
                    response = "{\"message\":\"Successful\"}";

                }
            } else {
                int insert = productionRepository.insertProduction(production.getBatch_no(),
                        sdf.format(date), production.getQty(), production.getSku(),
                        production.getBay_no(), production.getStatus());
                if (insert > 0) {
                    response = "{\"message\":\"Successful\"}";

                }
            }
        } else {
            response = "{\"message\":\"Limit Exceeded\"}";
        }
        }
        else {
        	response = "{\"message\":\"Wrong Data Recieved\"}";
		}

        return response;
    }

    
    @GetMapping("getStateList")
    public Map<String, HashSet<String>> getAllSku() {
        List<String> list = productionRepository.getAllStateList();
        ArrayList<String> ls = new ArrayList<>();
        for (String sku : list) {
            String state = "";
            for (int i = 0; i < sku.toCharArray().length; i++) {
                if (i > sku.toCharArray().length - 7) {
                    state += sku.toCharArray()[i];
                }
            }
            ls.add(state);
        }
        HashSet<String> sk = new HashSet<>(ls);
        HashMap<String, HashSet<String>> hMap = new HashMap<>();
        hMap.put("state", sk);
        return hMap;
    }
    
  
    
    @PostMapping("/updateProductionOrder")
    public String updateProductionOrder(@RequestBody Production production,
                                        @RequestParam("order_id") long order_id) {
        List<Production> productionList = null;
        productionList = productionRepository.getProductionData(production.getBatch_no(),
                production.getSku(), production.getBay_no(), "PASS");
        String response = "{\"message\":\"Unsuccessful\"}";
        System.out.println("order_id:" + order_id);
        if (productionList.size() > 0) {
        	
            if (production.getQty() < 0) {
            	System.out.println("if");
                production.setQty(-production.getQty());
                int update = productionRepository.updateProduction(production.getBatch_no(),
                        productionList.get(0).getDate(), productionList.get(0).getQty() + production.getQty()
                        , production.getSku(), production.getBay_no(), "PASS");
                sortingPurchaseRepository.updateWithOrderId(order_id);
                if (update > 0) {
                    response = "{\"message\":\"Successful\"}";
                }
            } else if (production.getQty() > 0) {
            	System.out.println("else");

                int update = productionRepository.updateProduction(production.getBatch_no(),
                        productionList.get(0).getDate(), productionList.get(0).getQty() - production.getQty()
                        , production.getSku(), production.getBay_no(), "PASS");
                sortingPurchaseRepository.updateWithOrderId(order_id);
                if (update > 0) {
                    response = "{\"message\":\"Successful\"}";
                }
            }
        } else {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int insert = productionRepository.insertProduction(production.getBatch_no(),
                    sdf.format(date), productionList.get(0).getQty() + production.getQty()
                    , production.getSku(), production.getBay_no(), "PASS");
            sortingPurchaseRepository.updateWithOrderId(order_id);
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";
            }

        }
        return response;
    }
    
    
    
    @PostMapping("/updateManualProductionOrder")
    public String updateManualProductionOrder(@RequestBody Production production
                                       ) {
        List<Production> productionList = null;
        productionList = productionRepository.getProductionData(production.getBatch_no(),
                production.getSku(), production.getBay_no(), "PASS");
        String response = "{\"message\":\"Unsuccessful\"}";
        if (productionList.size() > 0) {
        	
            if (production.getQty() < 0) {
            	System.out.println("if");
                production.setQty(-production.getQty());
                int update = productionRepository.updateProduction(production.getBatch_no(),
                        productionList.get(0).getDate(), productionList.get(0).getQty() + production.getQty()
                        , production.getSku(), production.getBay_no(), "PASS");
                if (update > 0) {
                    response = "{\"message\":\"Successful\"}";
                    response = "{\"message\":\"Successful\"}";
                    ManualOrder manualOrder=new ManualOrder();
                    manualOrder.setBatch_no(production.getBatch_no());
                    manualOrder.setBay_no(production.getBay_no());
                    manualOrder.setDate(production.getDate());
                    manualOrder.setSku(production.getSku());
                    manualOrder.setQty(production.getQty());
                    ManualOrderRepository.save(manualOrder);
                }
            } else if (production.getQty() > 0) {
            	System.out.println("else");

                int update = productionRepository.updateProduction(production.getBatch_no(),
                        productionList.get(0).getDate(), productionList.get(0).getQty() - production.getQty()
                        , production.getSku(), production.getBay_no(), "PASS");
                if (update > 0) {
                    response = "{\"message\":\"Successful\"}";
                    ManualOrder manualOrder=new ManualOrder();
                    manualOrder.setBatch_no(production.getBatch_no());
                    manualOrder.setBay_no(production.getBay_no());
                    manualOrder.setDate(production.getDate());
                    manualOrder.setSku(production.getSku());
                    manualOrder.setQty(production.getQty());
                    ManualOrderRepository.save(manualOrder);
                }
            }
        } else {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int insert = productionRepository.insertProduction(production.getBatch_no(),
                    sdf.format(date), productionList.get(0).getQty() + production.getQty()
                    , production.getSku(), production.getBay_no(), "PASS");
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";
                response = "{\"message\":\"Successful\"}";
                ManualOrder manualOrder=new ManualOrder();
                manualOrder.setBatch_no(production.getBatch_no());
                manualOrder.setBay_no(production.getBay_no());
                manualOrder.setDate(production.getDate());
                manualOrder.setSku(production.getSku());
                manualOrder.setQty(production.getQty());
                ManualOrderRepository.save(manualOrder);
            }

        }
        return response;
    }



    @PostMapping("/changeBayAndStatus")
    public String changeBayAndStatus(@RequestBody Production production) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        String response = "{\"message\":\"Unsuccessful\"}";
        if (production.getStatus() == null) {
            production.setStatus("PASS");
        }
        List<Production> productionList = productionRepository.getProductionData(production.getBatch_no(),
                production.getSku(), production.getBay_no(), production.getStatus());
        if (productionList.size() > 0) {
            int update = productionRepository.updateProduction(production.getBatch_no(), sdf.format(date)
                    , productionList.get(0).getQty() + production.getQty()
                    , production.getSku(), production.getBay_no(), production.getStatus());
            if (update > 0) {
                response = "{\"message\":\"Successful\"}";

            }
        } else {
            int insert = productionRepository.insertProduction(production.getBatch_no(), sdf.format(date)
                    , production.getQty()
                    , production.getSku(), production.getBay_no(), production.getStatus());
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";

            }
        }
        return response;
    }

    @PostMapping("/verify")
    public String verify(@RequestBody Production production, @RequestParam("line_no") String line_no) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        String response = "{\"message\":\"Unsuccessful\"}";
        List<Production> productionList = productionRepository.getProductionData(production.getBatch_no(),
                production.getSku(), production.getBay_no(), production.getStatus());
        if (productionList.size() > 0) {
            int update = productionRepository.updateProduction(production.getBatch_no(), sdf.format(date)
                    , productionList.get(0).getQty() + production.getQty()
                    , production.getSku(), production.getBay_no(), production.getStatus());
            if (update > 0) {
                response = "{\"message\":\"Successful\"}";
                List<FilterQty> getFilter = filterQtyRepo.getFilterData(sdf1.format(date), production.getSku(),
                        line_no, production.getBatch_no(), production.getBay_no());
                filterQtyRepo.updateData(production.getSku(), production.getQty() + getFilter.get(0).getQty(),
                        sdf1.format(date), line_no, production.getBatch_no(), production.getBay_no());

            }
        }

        return response;
    }

    @GetMapping("/getSkuData")
    public Map<String, HashSet<String>> getSkuData(@RequestParam("sku") String sku) {
        ArrayList<String> allData = (ArrayList<String>) productionRepository.getAllSku(sku);
        HashSet<String> uniqueSku = new HashSet<String>(allData);
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("production", uniqueSku);
        return hmap;
    }
    
    
    @GetMapping("/getAllSku")
    public Map<String, HashSet<String>> getAllSku1() {
        ArrayList<String> allData = (ArrayList<String>) productionRepository.getAllStateList();
        HashSet<String> uniqueSku = new HashSet<String>(allData);
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("production", uniqueSku);
        return hmap;
    }

  

    @GetMapping("/getAllProductionData")
    public Map<String, ArrayList<ProductionModel>> getAllData() {
        productionRepository.deleteProduction();
        List<BayCapacity> bayList = this.bayCapacityRepository.getBay();
        ArrayList<ProductionModel> productionList = new ArrayList<ProductionModel>();
        for (BayCapacity bayCapacity : bayList) {
            List<Production> allData = (List<Production>) productionRepository.getAllProductionData(bayCapacity.getBay());
            if (allData.size() > 0) {
                for (Production production : allData) {
                    productionList.add(new ProductionModel(production.getSku(), production.getBatch_no(), production.getQty(), production.getBay_no(), production.getStatus(), production.getDate()));
                }
            } else {
                productionList.add(new ProductionModel("Empty", "Empty", 0, bayCapacity.getBay(), "Empty", "Empty"));
            }
        }

        HashMap<String, ArrayList<ProductionModel>> hmap = new HashMap<String, ArrayList<ProductionModel>>();
        hmap.put("production", productionList);
        return hmap;
    }

    @GetMapping("/getQuantity")
    public Map<String, Integer> getQuantity(@RequestParam("sku") String sku) {
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        int purchaseQty = 0;
        List<SortingPurchase> getPurchaseQty = (List<SortingPurchase>) sortingPurchaseRepository.getSortingPurchase(sku);
        for (SortingPurchase sortingPurchase : getPurchaseQty) {
            purchaseQty += sortingPurchase.getQty();
        }
        int qty = 0;
        ArrayList<Production> allData = (ArrayList<Production>) productionRepository.getQuantity(sku, "PASS");
        for (Production production : allData) {
            qty += production.getQty();
        }
        hmap.put("quantity", qty - purchaseQty);
        return hmap;
    }

    @GetMapping("/getBayQuantity")
    public Map<String, Integer> getBayQuantity(@RequestParam("sku") String sku,
                                               @RequestParam("batch_no") String batch_no,
                                               @RequestParam("bay_no") String bay_no) {
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        int purchaseQty = 0;
        List<SortingPurchase> getPurchaseQty = sortingPurchaseRepository.getSortingPurchase(sku,
                batch_no, bay_no);
        for (SortingPurchase sortingPurchase : getPurchaseQty) {
            purchaseQty += sortingPurchase.getQty();
        }
        int qty = 0;
        ArrayList<Production> allData = (ArrayList<Production>) productionRepository.getQuantity(sku, "PASS",
                bay_no, batch_no);
        for (Production production : allData) {
            qty += production.getQty();
        }
        hmap.put("quantity", qty - purchaseQty);
        return hmap;
    }

    @GetMapping("/getBatchNo")
    public Map<String, HashSet<String>> getSkuData() {
        ArrayList<String> allData = (ArrayList<String>) productionRepository.getBatchNo();
        HashSet<String> uniqueSku = new HashSet<String>(allData);
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("BatchNo", uniqueSku);
        return hmap;
    }

    @GetMapping("/getSearchProduct")
    public Map<String, ArrayList<Production>> getSearchProduct(@RequestParam(name = "sku", required = false) String sku, @RequestParam(name = "batch_no", required = false) String batch_no, @RequestParam(name = "bay_no", required = false) String bay_no) {
        HashMap<String, ArrayList<Production>> hmap = new HashMap<String, ArrayList<Production>>();
        if (batch_no == null || batch_no.equalsIgnoreCase("select") || batch_no.isEmpty()) {
            batch_no = "0";
        }
        ArrayList<Production> list = (ArrayList<Production>) productionRepository.getSearchProduct(sku, batch_no, bay_no);
        hmap.put("SearchData", list);
        return hmap;
    }

    @GetMapping("/getProductionData1")
    public Map<String, ArrayList<Production>> getProductionComplete() {
        HashMap<String, ArrayList<Production>> hmap = new HashMap<String, ArrayList<Production>>();
        productionRepository.deleteProduction();
        ArrayList<Production> list = (ArrayList<Production>) productionRepository.getProductionComplete();
        hmap.put("productionData", list);
        return hmap;
    }


    @GetMapping("/getBatch")
    public Map<String, HashSet<String>> getBatchNo(@RequestParam("sku") String sku) {
        HashMap<String, HashSet<String>> hMap = new HashMap<>();
        List<String> getBatchNo = productionRepository.getBatchNo(sku);
        HashSet<String> uniqueBatch = new HashSet<>(getBatchNo);
        hMap.put("batch", uniqueBatch);
        return hMap;
    }

    @GetMapping("/getBay")
    public Map<String, HashSet<String>> getBay(@RequestParam("sku") String sku
            , @RequestParam("batch_no") String batch_no) {
        HashMap<String, HashSet<String>> hMap = new HashMap<>();
        List<String> getBay = productionRepository.getBay(sku, batch_no);
        HashSet<String> uniqueBay = new HashSet<>(getBay);
        hMap.put("bay", uniqueBay);
        return hMap;
    }

    @GetMapping("/generateExcel")
    public void createProductionSheet(HttpServletResponse response1) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        HSSFCellStyle style1 = (HSSFCellStyle) workbook.createCellStyle();
        CellStyle style0 = workbook.createCellStyle();

        style0.setVerticalAlignment(VerticalAlignment.CENTER);
        style0.setAlignment(HorizontalAlignment.CENTER);

        style0.setBorderBottom(BorderStyle.THIN);
        style0.setBorderTop(BorderStyle.THIN);
        style0.setBorderLeft(BorderStyle.THIN);
        style0.setBorderRight(BorderStyle.THIN);


        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);

        org.apache.poi.ss.usermodel.Font font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 10);

        style0.setFont((org.apache.poi.ss.usermodel.Font) font);
        style0.setWrapText(true);

        style1.setFont((org.apache.poi.ss.usermodel.Font) font1);
        style1.setWrapText(true);

        try {


            List<Production> productionList = productionRepository.getAllProductionData();
            try {
                Sheet sheet = workbook.createSheet("All Production");
                Row row0 = sheet.createRow(0);

                row0.setHeight((short) 600);
                sheet.setColumnWidth(0, 5000);

                sheet.setColumnWidth(6, 5000);
                sheet.setColumnWidth(5, 5000);
                sheet.setColumnWidth(4, 5000);
                sheet.setColumnWidth(3, 5000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(1, 5000);


                Cell cell0 = row0.createCell(0);
                Cell cell1 = row0.createCell(1);
                Cell cell2 = row0.createCell(2);
                Cell cell3 = row0.createCell(3);
                Cell cell4 = row0.createCell(4);
                Cell cell5 = row0.createCell(5);
                Cell cell6 = row0.createCell(6);

                cell0.setCellStyle(style0);
                cell1.setCellStyle(style0);
                cell2.setCellStyle(style0);
                cell3.setCellStyle(style0);
                cell5.setCellStyle(style0);
                cell4.setCellStyle(style0);
                cell6.setCellStyle(style0);

                cell0.setCellValue("Sr.No.");
                cell1.setCellValue("Date");
                cell2.setCellValue("Bay No");
                cell3.setCellValue("SKU");
                cell4.setCellValue("Batch No");
                cell5.setCellValue("Quantity");
                cell6.setCellValue("Status");

                int j = 1;
                for (Production productionData : productionList) {
                    System.out.println("bay no " + productionData.getBay_no());
                    Row row1 = sheet.createRow(j++);
                    Cell cell11 = row1.createCell(0);
                    Cell cell12 = row1.createCell(1);
                    Cell cell13 = row1.createCell(2);

                    Cell cell14 = row1.createCell(3);
                    Cell cell15 = row1.createCell(4);
                    Cell cell16 = row1.createCell(5);
                    Cell cell17 = row1.createCell(6);

                    cell11.setCellStyle(style1);
                    cell12.setCellStyle(style1);
                    cell13.setCellStyle(style1);
                    cell14.setCellStyle(style1);
                    cell17.setCellStyle(style1);
                    cell16.setCellStyle(style1);
                    cell15.setCellStyle(style1);


                    cell11.setCellValue(j - 1);
                    cell12.setCellValue(productionData.getDate());
                    cell13.setCellValue(productionData.getBay_no());
                    cell14.setCellValue(productionData.getSku());
                    cell15.setCellValue(productionData.getBatch_no());
                    cell16.setCellValue(productionData.getQty());
                    cell17.setCellValue(productionData.getStatus());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            List<FilterQty> todat_list = filterQtyRepo.getAllData(sdf.format(date));
            try {
                Sheet sheet1 = workbook.createSheet("Today's Data");
                sheet1.setColumnWidth(0, 5000);
                sheet1.setColumnWidth(7, 6000);
                sheet1.setColumnWidth(6, 5000);
                sheet1.setColumnWidth(5, 5000);
                sheet1.setColumnWidth(4, 5000);
                sheet1.setColumnWidth(3, 5000);
                sheet1.setColumnWidth(2, 5000);
                sheet1.setColumnWidth(1, 5000);


                Row row0 = sheet1.createRow(0);
                row0.setHeight((short) 600);

                Cell cell0 = row0.createCell(0);
                Cell cell1 = row0.createCell(1);
                Cell cell2 = row0.createCell(2);
                Cell cell3 = row0.createCell(3);
                Cell cell4 = row0.createCell(4);
                Cell cell5 = row0.createCell(5);
                Cell cell6 = row0.createCell(6);

                cell0.setCellStyle(style0);
                cell1.setCellStyle(style0);
                cell2.setCellStyle(style0);
                cell3.setCellStyle(style0);
                cell5.setCellStyle(style0);
                cell4.setCellStyle(style0);
                cell6.setCellStyle(style0);

                cell0.setCellValue("Sr.No.");
                cell1.setCellValue("Date");
                cell2.setCellValue("Bay No");
                cell3.setCellValue("SKU");
                cell4.setCellValue("Batch No");
                cell5.setCellValue("Quantity");
                cell6.setCellValue("Line No.");

                int j = 1;
                for (FilterQty fq : todat_list) {

                    System.out.println("bay no " + fq.getBay());
                    Row row1 = sheet1.createRow(j++);


                    Cell cell11 = row1.createCell(0);
                    Cell cell12 = row1.createCell(1);
                    Cell cell13 = row1.createCell(2);
                    Cell cell14 = row1.createCell(3);
                    Cell cell15 = row1.createCell(4);
                    Cell cell16 = row1.createCell(5);
                    Cell cell17 = row1.createCell(6);

                    cell11.setCellStyle(style1);
                    cell12.setCellStyle(style1);
                    cell13.setCellStyle(style1);
                    cell14.setCellStyle(style1);
                    cell17.setCellStyle(style1);
                    cell16.setCellStyle(style1);
                    cell15.setCellStyle(style1);


                    cell11.setCellValue(j - 1);
                    cell12.setCellValue(fq.getDate());
                    cell13.setCellValue(fq.getBay());
                    cell14.setCellValue(fq.getSku());
                    cell15.setCellValue(fq.getBatch_no());
                    cell16.setCellValue(fq.getQty());
                    cell17.setCellValue(fq.getLine_no());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date oneDayBefore = new Date(date.getTime() - (24 * 3600000));


            List<FilterQty> previous_list = filterQtyRepo.getAllData(sdf.format(oneDayBefore));
            try {
                Sheet sheet2 = workbook.createSheet("Yesterday's Data");

                sheet2.setColumnWidth(0, 5000);
                sheet2.setColumnWidth(7, 6000);
                sheet2.setColumnWidth(6, 5000);
                sheet2.setColumnWidth(5, 5000);
                sheet2.setColumnWidth(4, 5000);
                sheet2.setColumnWidth(3, 5000);
                sheet2.setColumnWidth(2, 5000);
                sheet2.setColumnWidth(1, 5000);
                Row row0 = sheet2.createRow(0);
                row0.setHeight((short) 600);

                Cell cell0 = row0.createCell(0);
                Cell cell1 = row0.createCell(1);
                Cell cell2 = row0.createCell(2);
                Cell cell3 = row0.createCell(3);
                Cell cell4 = row0.createCell(4);
                Cell cell5 = row0.createCell(5);
                Cell cell6 = row0.createCell(6);

                cell0.setCellStyle(style0);
                cell1.setCellStyle(style0);
                cell2.setCellStyle(style0);
                cell3.setCellStyle(style0);
                cell5.setCellStyle(style0);
                cell4.setCellStyle(style0);
                cell6.setCellStyle(style0);

                cell0.setCellValue("Sr.No.");
                cell1.setCellValue("Date");
                cell2.setCellValue("Bay No");
                cell3.setCellValue("SKU");
                cell4.setCellValue("Batch No");
                cell5.setCellValue("Quantity");
                cell6.setCellValue("Line No.");

                int j = 1;
                for (FilterQty fq : previous_list) {
                    System.out.println("bay no " + fq.getBay());
                    Row row1 = sheet2.createRow(j++);

                    Cell cell11 = row1.createCell(0);
                    Cell cell12 = row1.createCell(1);
                    Cell cell13 = row1.createCell(2);
                    Cell cell14 = row1.createCell(3);
                    Cell cell15 = row1.createCell(4);
                    Cell cell16 = row1.createCell(5);
                    Cell cell17 = row1.createCell(6);

                    cell11.setCellStyle(style1);
                    cell12.setCellStyle(style1);
                    cell13.setCellStyle(style1);
                    cell14.setCellStyle(style1);
                    cell17.setCellStyle(style1);
                    cell16.setCellStyle(style1);
                    cell15.setCellStyle(style1);


                    cell11.setCellValue(j - 1);
                    cell12.setCellValue(fq.getDate());
                    cell13.setCellValue(fq.getBay());
                    cell14.setCellValue(fq.getSku());
                    cell15.setCellValue(fq.getBatch_no());
                    cell16.setCellValue(fq.getQty());
                    cell17.setCellValue(fq.getLine_no());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response1.setHeader("content-disposition", "attachment;filename=Production Report_" + sdf.format(date) + ".xls");
        workbook.write(response1.getOutputStream());
    }

    @GetMapping("/generateProdExcel")
    public void createProductionSheet(HttpServletResponse response1, @RequestParam("date") String date,
                                      @RequestParam("to") String to)
            throws IOException {
        Workbook workbook = new HSSFWorkbook();
        HSSFCellStyle style1 = (HSSFCellStyle) workbook.createCellStyle();
        CellStyle style0 = workbook.createCellStyle();

        style0.setVerticalAlignment(VerticalAlignment.CENTER);
        style0.setAlignment(HorizontalAlignment.CENTER);
        ;
        style0.setBorderBottom(BorderStyle.THIN);
        style0.setBorderTop(BorderStyle.THIN);
        style0.setBorderLeft(BorderStyle.THIN);
        style0.setBorderRight(BorderStyle.THIN);


        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);

        org.apache.poi.ss.usermodel.Font font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 10);

        style0.setFont((org.apache.poi.ss.usermodel.Font) font);
        style0.setWrapText(true);

        style1.setFont((org.apache.poi.ss.usermodel.Font) font1);
        style1.setWrapText(true);


        try {


            List<FilterQty> todat_list = filterQtyRepo.getAllData(date, to);
            try {
                Sheet sheet1 = workbook.createSheet("Today's Data");
                sheet1.setColumnWidth(0, 5000);
                sheet1.setColumnWidth(7, 6000);
                sheet1.setColumnWidth(6, 5000);
                sheet1.setColumnWidth(5, 5000);
                sheet1.setColumnWidth(4, 5000);
                sheet1.setColumnWidth(3, 5000);
                sheet1.setColumnWidth(2, 5000);
                sheet1.setColumnWidth(1, 5000);


                Row row0 = sheet1.createRow(0);
                row0.setHeight((short) 600);

                Cell cell0 = row0.createCell(0);
                Cell cell1 = row0.createCell(1);
                Cell cell2 = row0.createCell(2);
                Cell cell3 = row0.createCell(3);
                Cell cell4 = row0.createCell(4);
                Cell cell5 = row0.createCell(5);
                Cell cell6 = row0.createCell(6);

                cell0.setCellStyle(style0);
                cell1.setCellStyle(style0);
                cell2.setCellStyle(style0);
                cell3.setCellStyle(style0);
                cell5.setCellStyle(style0);
                cell4.setCellStyle(style0);
                cell6.setCellStyle(style0);

                cell0.setCellValue("Sr.No.");
                cell1.setCellValue("Date");
                cell2.setCellValue("Bay No");
                cell3.setCellValue("SKU");
                cell4.setCellValue("Batch No");
                cell5.setCellValue("Quantity");
                cell6.setCellValue("Line No.");

                int j = 1;
                for (FilterQty fq : todat_list) {

                    System.out.println("bay no " + fq.getBay());
                    Row row1 = sheet1.createRow(j++);


                    Cell cell11 = row1.createCell(0);
                    Cell cell12 = row1.createCell(1);
                    Cell cell13 = row1.createCell(2);
                    Cell cell14 = row1.createCell(3);
                    Cell cell15 = row1.createCell(4);
                    Cell cell16 = row1.createCell(5);
                    Cell cell17 = row1.createCell(6);

                    cell11.setCellStyle(style1);
                    cell12.setCellStyle(style1);
                    cell13.setCellStyle(style1);
                    cell14.setCellStyle(style1);
                    cell17.setCellStyle(style1);
                    cell16.setCellStyle(style1);
                    cell15.setCellStyle(style1);


                    cell11.setCellValue(j - 1);
                    cell12.setCellValue(fq.getDate());
                    cell13.setCellValue(fq.getBay());
                    cell14.setCellValue(fq.getSku());
                    cell15.setCellValue(fq.getBatch_no());
                    cell16.setCellValue(fq.getQty());
                    cell17.setCellValue(fq.getLine_no());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        response1.setHeader("content-disposition", "attachment;filename=Production Report_" + date + ".xls");
        workbook.write(response1.getOutputStream());
    }


    @GetMapping("/allProductionData")
    public Map<String, List<Production>> allProductionData() {
        List<Production> getAllData = productionRepository.getAllProductionData();
        HashMap<String, List<Production>> hMap = new HashMap<>();
        hMap.put("production", getAllData);
        return hMap;
    }

    @PostMapping("/insertManualProduction")
    public String insertManualProduction(@RequestBody Production production, @RequestParam("month") String month) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date date1 = new Date();
        Date date = sdf.parse(month);
        String response = "{\"message\":\"Unsuccessful\"}";
        if (production.getStatus() == null) {
            production.setStatus("PASS");
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf4 = new SimpleDateFormat("dd");
        int mn = Integer.parseInt(sdf3.format(date));
        String batch_format = String.format("%04d", Integer.parseInt(production.getBatch_no()));
        String batch_no = companyCodeRepository.getCompanyCode() + "-" + batch_format + "-" + mn + sdf2.format(date);
        List<Production> productionList = productionRepository.getProductionData(batch_no,
                production.getSku(), production.getBay_no(), production.getStatus());
        if (productionList.size() > 0) {
            int update = productionRepository.updateProduction(batch_no, sdf.format(date) + "-" +
                            sdf4.format(date1) + " " + sdf1.format(date1)
                    , productionList.get(0).getQty() + production.getQty()
                    , production.getSku(), production.getBay_no(), production.getStatus());
            if (update > 0) {
                response = "{\"message\":\"Successful\"}";

            }
        } else {
            int insert = productionRepository.insertProduction(batch_no, sdf.format(date) + "-" +
                            sdf4.format(date1) + " " + sdf1.format(date1)
                    , production.getQty()
                    , production.getSku(), production.getBay_no(), production.getStatus());
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";

            }
        }
        return response;
    }

}
