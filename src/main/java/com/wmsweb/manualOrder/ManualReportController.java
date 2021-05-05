package com.wmsweb.manualOrder;

import com.wmsweb.FilterQty.FilterQty;
import com.wmsweb.production.Production;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ManualReportController {
    @Autowired
    ManualOrderRepository manualOrderRepository;



    @GetMapping("/manualOrderReport")
    public void createProductionSheet(HttpServletResponse response1,
                                      @RequestParam("from_date") String fromDate,
                                      @RequestParam("to_date") String toDate) throws IOException {
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


            List<ManualOrder> manualOrderList = manualOrderRepository.getManualOrder(fromDate,toDate);
            try {
                Sheet sheet = workbook.createSheet("Manual Order");
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

                cell0.setCellStyle(style0);
                cell1.setCellStyle(style0);
                cell2.setCellStyle(style0);
                cell3.setCellStyle(style0);
                cell5.setCellStyle(style0);
                cell4.setCellStyle(style0);

                cell0.setCellValue("Sr.No.");
                cell1.setCellValue("Date");
                cell2.setCellValue("Bay No");
                cell3.setCellValue("SKU");
                cell4.setCellValue("Batch No");
                cell5.setCellValue("Quantity");

                int j = 1;
                for (ManualOrder manualOrder : manualOrderList) {
                    Row row1 = sheet.createRow(j++);
                    Cell cell11 = row1.createCell(0);
                    Cell cell12 = row1.createCell(1);
                    Cell cell13 = row1.createCell(2);

                    Cell cell14 = row1.createCell(3);
                    Cell cell15 = row1.createCell(4);
                    Cell cell16 = row1.createCell(5);

                    cell11.setCellStyle(style1);
                    cell12.setCellStyle(style1);
                    cell13.setCellStyle(style1);
                    cell14.setCellStyle(style1);
                    cell16.setCellStyle(style1);
                    cell15.setCellStyle(style1);


                    cell11.setCellValue(j - 1);
                    cell12.setCellValue(manualOrder.getDate());
                    cell13.setCellValue(manualOrder.getBay_no());
                    cell14.setCellValue(manualOrder.getSku());
                    cell15.setCellValue(manualOrder.getBatch_no());
                    cell16.setCellValue(manualOrder.getQty());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response1.setHeader("content-disposition", "attachment;filename=Manual Order Report_" + sdf.format(date) + ".xls");
        workbook.write(response1.getOutputStream());
    }
}
