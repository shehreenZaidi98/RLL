// 
// Decompiled by Procyon v0.5.36
// 

package com.wmsweb.skuList;

import com.wmsweb.bayCapacity.BayCapacity;
import com.wmsweb.bayCapacity.BayCapacityRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(origins = "*")
public class SkuListController {
    @Autowired
    SkuListRepository skuListRepository;
    @Autowired
    BayCapacityRepository bayCapacityRepository;

    @PostMapping({"/uploadExcel"})
    public String uploadExcel(@RequestParam("file") MultipartFile multipartFile) {
        Workbook workbook = null;
       try {
            workbook = (Workbook) new XSSFWorkbook(multipartFile.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        XSSFSheet sheet2 = (XSSFSheet) workbook.getSheetAt(1);
        String response = "{\"message\":\"Unsuccessful\"}";
        List<SkuList> skuList = (List<SkuList>) this.skuListRepository.getSkuList();
        List<BayCapacity> bayCapacity = this.bayCapacityRepository.getBay();
        if (skuList.size() > 0) {
            response = "{\"message\":\"Allready Exist\"}";
        } else {
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                if (sheet.getRow(i).getCell(0).getCellType() != CellType.BLANK) {
                    try {
                        List<SkuList> skList = (List<SkuList>) this.skuListRepository.getSkuList(sheet.getRow(i).getCell(0).getStringCellValue().trim());
                        if (skList.size() == 0) {
                            int insert = skuListRepository.insertSku(sheet.getRow(i).getCell(0).getStringCellValue().trim(), sheet.getRow(i).getCell(1).getNumericCellValue(), sheet.getRow(i).getCell(2).getNumericCellValue());
                            if (insert > 0) {
                                response = "{\"message\":\"Successful\"}";
                            }
                        }
                    } catch (Exception e2) {
                        System.out.println("Exception Sku:  " + e2.getMessage());
                    }
                }
            }
        }
        if (bayCapacity.size() > 0) {
            response = "{\"message\":\"Allready Exist\"}";
        } else {
            try {
                for (int i = 1; i <= sheet2.getLastRowNum(); ++i) {
                    if (sheet2.getRow(i).getCell(0).getCellType() != CellType.BLANK) {
                        List<BayCapacity> bayList = bayCapacityRepository.getBay(sheet2.getRow(i).getCell(0).getStringCellValue().trim());
                        if (bayList.size() == 0) {
                            int insert = this.bayCapacityRepository.insertBay(sheet2.getRow(i).getCell(0).getStringCellValue().trim(), sheet2.getRow(i).getCell(1).getNumericCellValue());
                            if (insert > 0) {
                                response = "{\"message\":\"Successful\"}";
                            }
                        }
                    }
                }
            } catch (Exception e3) {
                System.out.println("Exception Bay:  " + e3);
            }
        }
        return response;
    }

    @PostMapping({"/updateSku"})
    public String updateSku(@RequestBody SkuList skuList) {
        List<SkuList> skList = (List<SkuList>) skuListRepository.getSkuList(skuList.getSku());
        String response = "{\"message\":\"Unsuccessful\"}";
        if (skList.size() > 0) {
            int update = skuListRepository.updateSku(skuList.getSku(), skuList.getCases_of_pallets(), skuList.getPallet_weight());
            if (update > 0) {
                response = "{\"message\":\"Successful\"}";
            }
        } else {
            int insert = skuListRepository.insertSku(skuList.getSku(), skuList.getCases_of_pallets(), skuList.getPallet_weight());
            if (insert > 0) {
                response = "{\"message\":\"Successful\"}";
            }
        }
        return response;
    }

    @GetMapping({"/getSkuListData"})
    public Map<String, ArrayList<SkuList>> getTransportStatus() {
        HashMap<String, ArrayList<SkuList>> hmap = new HashMap<String, ArrayList<SkuList>>();
        ArrayList<SkuList> list = (ArrayList<SkuList>) skuListRepository.getSkuList();
        hmap.put("SkuData", list);
        return hmap;
    }

    @GetMapping("/getPalletWeight")
    public Map<String, ArrayList<String>> getPalletWeight(@RequestParam("sku") String sku) {
        ArrayList<String> list = (ArrayList<String>) skuListRepository.getpalletWeight(sku);
        HashMap<String, ArrayList<String>> hmap = new HashMap<String, ArrayList<String>>();
        hmap.put("PalletWeight", list);
        return hmap;

    }

   
}
    
    

