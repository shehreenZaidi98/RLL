package com.wmsweb.multipleProduction;

import com.wmsweb.production.Production;
import com.wmsweb.production.ProductionRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MultipleProductionController {
    @Autowired
    ProductionRepository productionRepository;

    @PostMapping("uploadMultipleProduction")
    public String uploadExcel(@RequestParam("file") MultipartFile multipartFile) {
        Workbook workbook = null;
        try {
            workbook = (Workbook) new XSSFWorkbook(multipartFile.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
        String response = "{\"message\":\"Unsuccessful\"}";
        String batch_no = "", bay_no = "", sku = "", date = "", status = "";
        for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
            if (sheet.getRow(i).getCell(0).getCellType() != CellType.BLANK) {
                try {

                    if (sheet.getRow(i).getCell(1).getCellType() == CellType.NUMERIC) {
                        batch_no = String.valueOf(sheet.getRow(i).getCell(1).getNumericCellValue());
                    } else if (sheet.getRow(i).getCell(1).getCellType() == CellType.STRING) {
                        batch_no = sheet.getRow(i).getCell(1).getStringCellValue().trim();

                    }

                    if (sheet.getRow(i).getCell(3).getCellType() == CellType.NUMERIC) {
                        date = String.valueOf(sheet.getRow(i).getCell(3).getNumericCellValue());
                    } else if (sheet.getRow(i).getCell(3).getCellType() == CellType.STRING) {
                        date = sheet.getRow(i).getCell(3).getStringCellValue().trim();

                    }

                    if (sheet.getRow(i).getCell(5).getCellType() == CellType.NUMERIC) {
                        sku = String.valueOf(sheet.getRow(i).getCell(5).getNumericCellValue());
                    } else if (sheet.getRow(i).getCell(5).getCellType() == CellType.STRING) {
                        sku = sheet.getRow(i).getCell(5).getStringCellValue().trim();

                    }
                    if (sheet.getRow(i).getCell(2).getCellType() == CellType.NUMERIC) {
                        bay_no = String.valueOf(sheet.getRow(i).getCell(2).getNumericCellValue());
                    } else if (sheet.getRow(i).getCell(2).getCellType() == CellType.STRING) {
                        bay_no = sheet.getRow(i).getCell(2).getStringCellValue().trim();

                    }

                    if (sheet.getRow(i).getCell(6).getCellType() == CellType.NUMERIC) {
                        status = String.valueOf(sheet.getRow(i).getCell(6).getNumericCellValue());
                    } else if (sheet.getRow(i).getCell(6).getCellType() == CellType.STRING) {
                        status = sheet.getRow(i).getCell(6).getStringCellValue().trim();

                    }
                    List<Production> productionList = productionRepository.getProductionData(batch_no, sku, bay_no, status);
                    if (productionList.size() > 0) {
                        int update = productionRepository.updateProduction(batch_no, date
                                , productionList.get(0).getQty() + (int) sheet.getRow(i).getCell(4).getNumericCellValue()
                                , bay_no, status, sku);
                        if (update > 0) {
                            response = "{\"message\":\"Successful\"}";

                        }
                    } else {
                        int insert = productionRepository.insertProduction(
                                batch_no, date,
                                (int) sheet.getRow(i).getCell(4).getNumericCellValue(),
                                sku, bay_no, status);
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
