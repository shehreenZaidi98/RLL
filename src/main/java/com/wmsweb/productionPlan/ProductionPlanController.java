

package com.wmsweb.productionPlan;

import com.wmsweb.companyCode.CompanyCodeRepository;
import com.wmsweb.skuList.SkuList;
import com.wmsweb.skuList.SkuListRepository;

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
public class ProductionPlanController {
    @Autowired
    SkuListRepository skuListRepository;
    @Autowired
    ProductionPlanRepository productionPlanRepository;
    @Autowired
    CompanyCodeRepository companyCodeRepository;


    Date date;
    SimpleDateFormat sdf;

    public ProductionPlanController() {
        this.date = new Date();
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @PostMapping({"/insertProductionPlan"})
    @ResponseBody
    public String insertProductionPlan(@RequestBody ProductionPlan productionPlan) {
        String message = "{\"message\":\"Unsuccessful\"}";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy");
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String batch_format = String.format("%04d", Integer.parseInt(productionPlan.getBatch_no()));
        String batch_no = companyCodeRepository.getCompanyCode() + "-" + batch_format + "-" + (month + 1) + sdf2.format(date);
        List<ProductionPlan> getSku = productionPlanRepository.getTodayProductionPlan(productionPlan.getSku()
                , sdf1.format(date) + " 00:00:00", sdf.format(date), productionPlan.getLine_no(), batch_no);
        if (getSku.size() > 0) {
            int update = productionPlanRepository.updateProduction_plan(
                    getSku.get(0).getQty() + productionPlan.getQty(),
                    productionPlan.getSku(), productionPlan.getLine_no());
            if (update > 0) {
                message = "{\"message\":\"Successful\"}";
            }
        } else {

            int insert = productionPlanRepository.insertProduction_plan(batch_no,
                    sdf.format(date), productionPlan.getQty(),
                    productionPlan.getSku(), productionPlan.getLine_no());
            if (insert > 0) {
                message = "{\"message\":\"Successful\"}";
            }
        }


        return message;
    }

    @GetMapping({"/getSkuList"})
    public Map<String, ArrayList<SkuList>> getSkuList() {
        ArrayList<SkuList> list = (ArrayList<SkuList>) skuListRepository.getSkuList();
        HashMap<String, ArrayList<SkuList>> hmap = new HashMap<String, ArrayList<SkuList>>();
        hmap.put("sku", list);
        return hmap;
    }

    @GetMapping({"/getTodayProductionPlan"})
    
    public Map<String, ArrayList<ProductionPlan>> getTodayProductionPlan() {
        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, ArrayList<ProductionPlan>> hmap = new HashMap<String, ArrayList<ProductionPlan>>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
       int hour=Integer.parseInt(sdf1.format(date));
       String fromDate = null;
       String toDateString = null;
       
       if(hour>=20) {
    	   fromDate=sdf2.format(date);
    	   Calendar c = Calendar.getInstance();
    	   c.setTime(new Date()); // Now use today date.
    	   c.add(Calendar.DATE, 1); // Adding 5 days
    	  toDateString = sdf2.format(c.getTime());
       }
       else if (hour<20) {
    	   toDateString=sdf2.format(date);
    	   Calendar c = Calendar.getInstance();
    	   c.setTime(new Date()); // Now use today date.
    	   c.add(Calendar.DATE, -1); // Adding 5 days
    	  fromDate = sdf2.format(c.getTime());
	}
        
       System.out.println("todayDate"+toDateString);
       System.out.println("fromDate"+fromDate);

       // ArrayList<ProductionPlan> productionPlanList = (ArrayList<ProductionPlan>) productionPlanRepository.getTodayProductionPlan(fromDate + "20:00:00", toDateString+ " 19:59:59");
        ArrayList<ProductionPlan> productionPlanList = (ArrayList<ProductionPlan>) productionPlanRepository.getTodayProductionPlan(fromDate + " 00:00:00", toDateString+ " 23:59:59");
        System.out.println(productionPlanList.size());
        hmap.put("proplan", productionPlanList);
        return hmap;
    }

    @GetMapping("getLineNo")
    public Map<String, TreeSet<String>> getLineNo() {
        List<String> duplicateData = productionPlanRepository.getLineNo();
        TreeSet<String> tList = new TreeSet<>(duplicateData);
        HashMap<String, TreeSet<String>> hMap = new HashMap<>();
        hMap.put("dp", tList);
        return hMap;
    }
    
    
    @PostMapping("uploadProductionExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile multipartFile) {
        Workbook workbook = null;
        try {
            workbook = (Workbook) new XSSFWorkbook(multipartFile.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        String response = "{\"message\":\"Unsuccessful\"}";
           Date date=new Date();
           SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");


            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                if (sheet.getRow(i).getCell(0).getCellType() != CellType.BLANK) {
                    try {
                        List<ProductionPlan> skList = productionPlanRepository.getTodayProductionPlan(
                        		sheet.getRow(i).getCell(2).getStringCellValue().trim(),
                        		sdf1.format(date)+" 00:00:00",sdf.format(date),
                        		String.valueOf((int)sheet.getRow(i).getCell(3).getNumericCellValue()),
                        		sheet.getRow(i).getCell(0).getStringCellValue().trim());
                        if (skList.size()> 0) {
                            int update=productionPlanRepository.updateProduction_plan(
                            		skList.get(0).getQty()+(int)sheet.getRow(i).getCell(1).getNumericCellValue(),
                        			sheet.getRow(i).getCell(2).getStringCellValue(),
                        			String.valueOf((int)sheet.getRow(i).getCell(3).getNumericCellValue()));
                        	
                        	if (update>0) {
                        		response = "{\"message\":\"Successful\"}";
							}
                        }
                        else {
                        	int insert = productionPlanRepository.insertProduction_plan(sheet.getRow(i).getCell(0).getStringCellValue().trim(),
                                    sdf.format(date),
                                     (int)sheet.getRow(i).getCell(1).getNumericCellValue(),

                                      (sheet.getRow(i).getCell(2).getStringCellValue()),
                                     String.valueOf((int)sheet.getRow(i).getCell(3).getNumericCellValue()));
                             if (insert > 0) {
                                 response = "{\"message\":\"Successful\"}";
                             }
                        }
                    } catch (Exception e2) {
                        System.out.println("Exception Sku:  " + e2.getMessage());
                    }
                }
            }


        return response;
    }
}
