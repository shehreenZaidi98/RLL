

package com.wmsweb.transport;

import com.wmsweb.OutGoods.OutGoods;
import com.wmsweb.OutGoods.OutGoodsRepository;
import com.wmsweb.Purchase.Purchase;
import com.wmsweb.Purchase.PurchaseRepository;
import com.wmsweb.SortingPurchase.SortingPurchase;
import com.wmsweb.SortingPurchase.SortingPurchaseRepository;
import com.wmsweb.commonData.CommonDataRepository;
import com.wmsweb.model.OrderDetailsModel;
import com.wmsweb.model.PermitListModel;
import com.wmsweb.permitNo.PermitNoRepository;

import com.wmsweb.production.Production;
import com.wmsweb.production.ProductionRepository;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransportController {
    @Autowired
    TransportRepository transportRepository;
    @Autowired
    PermitNoRepository permitNoRepository;
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    SortingPurchaseRepository sortingPurchaseRepository;
    @Autowired
    CommonDataRepository commonDataRepository;
    @Autowired
    ProductionRepository productionRepository;
    @Autowired
    OutGoodsRepository outGoodsRepository;

    @PostMapping("/insertTransport")
    @ResponseBody
    public String insertTransport(@RequestBody Transport transport) {
        String message = "{\"message\":\"Unsuccessful\"}";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transport.setDate(simpleDateFormat.format(date));
        Transport transport2 = transportRepository.save(transport);
        System.out.println();
        String[] permitNo = transport2.getPermit_no().split(",");
        int insert = 0;
        for (String permit_no : permitNo) {
            insert = permitNoRepository.insertPermitNo(transport2.getOrder_id(), permit_no);
        }
        if (insert > 0) {
            message = "{\"message\":\"Successful\"}";
        }
        return message;
    }

    @GetMapping("/getAddress")
    public Map<String, HashSet<String>> getAddress() {
        HashSet<String> set = (HashSet<String>) transportRepository.getAddress();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("address", set);
        return hmap;
    }

    @GetMapping("/getDriverName")
    public Map<String, HashSet<String>> getDriverName() {
        HashSet<String> set = (HashSet<String>) transportRepository.getDriverName();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("driver", set);
        return hmap;
    }

    @GetMapping("/getContactNo")
    public Map<String, HashSet<String>> getContactNo() {
        HashSet<String> set = (HashSet<String>) this.transportRepository.getContactNo();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("contact", set);
        return hmap;
    }

    @GetMapping("/getVehicleNo")
    public Map<String, HashSet<String>> getVehicleNo() {
        HashSet<String> set = (HashSet<String>) this.transportRepository.getVehicleNo();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("vehicle", set);
        return hmap;
    }

    @GetMapping("/getPartyName")
    public Map<String, HashSet<String>> getPartyName() {
        HashSet<String> set = (HashSet<String>) this.transportRepository.getPartyName();
        HashMap<String, HashSet<String>> hmap = new HashMap<String, HashSet<String>>();
        hmap.put("party", set);
        return hmap;
    }

    @GetMapping("/getTransportData")
    public Map<String, ArrayList<PermitListModel>> getTransportData() {
        List<Transport> list = (List<Transport>) this.transportRepository.getTransportData();
        HashMap<String, ArrayList<PermitListModel>> hmap = new HashMap<String, ArrayList<PermitListModel>>();
        ArrayList<PermitListModel> transportArrayList = new ArrayList<PermitListModel>();
        if (list.size() > 0) {
            List<String> permitList = this.permitNoRepository.getPermitList(list.get(0).getOrder_id());
            PermitListModel permitListModel = new PermitListModel(list.get(0).getOrder_id(), permitList);
            transportArrayList.add(permitListModel);
        }
        hmap.put("permitList", transportArrayList);
        return hmap;
    }

    @GetMapping("/getTransportStatus")
    public Map<String, ArrayList<Transport>> getTransportStatus(@RequestParam("order_id") long order_id) {
        HashMap<String, ArrayList<Transport>> hmap = new HashMap<String, ArrayList<Transport>>();
        ArrayList<Transport> list = (ArrayList<Transport>) transportRepository.getTransportStatus(order_id);
        hmap.put("TransportStatusData", list);
        return hmap;
    }

    @PostMapping("/updateTransportStatus")
    public String updateData(@RequestParam("order_id") long order_id, @RequestParam("status") int status) {
        String message = "{\"message\":\"Unsuccessful\"}";
        int updateData = transportRepository.updateStatus(order_id, status);
        if (updateData > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }

    @GetMapping("/getOrderDetails")
    public Map<String, ArrayList<OrderDetailsModel>> getOrderDetails() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        ArrayList<OrderDetailsModel> orderDetailsModelArrayList = new ArrayList<OrderDetailsModel>();
        List<Transport> getTransportDetails = (List<Transport>) this.transportRepository.getOrder(sdf2.format(date) + "-01 00:00:00", sdf.format(date));
        System.out.println(sdf2.format(date) + "-01 00:00:00");
        System.out.println(sdf.format(date));
        for (Transport transport : getTransportDetails) {
            if (transport.getTotal_qty() != null) {


                orderDetailsModelArrayList.add(new OrderDetailsModel(transport.getOrder_id(),
                        transport.getVehicle_no(), transport.getParty_name(),
                        transport.getVehicle_type(), transport.getState(),
                        Integer.parseInt(transport.getTotal_qty()), transport.getStatus(),transport.getSku()));
            }
        }


        HashMap<String, ArrayList<OrderDetailsModel>> hmap = new HashMap<String, ArrayList<OrderDetailsModel>>();
        hmap.put("orderDetails", orderDetailsModelArrayList);
        return hmap;
    }

    @GetMapping("/cancelOrder")
    public String changeTransportStatusWithOrderId(@RequestParam("order_id") long order_id) throws InterruptedException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        String message = "{\"message\":\"Unsuccessful\"}";
        List<Transport> list = transportRepository.getTransportStatus(order_id);
        if (list.get(0).getStatus() == 1) {
            int updateData = transportRepository.changeTransportStatusWithOrderId(order_id);
            if (updateData > 0) {
                purchaseRepository.runChangeStatusWithOrderId(order_id);
                sortingPurchaseRepository.runUpdateWithOrderId(order_id);
                commonDataRepository.runCancelOrder(order_id);
                sortingPurchaseRepository.deleteQty(order_id);
                List<OutGoods> getCancelProductData = outGoodsRepository.getOutGoodsData(order_id);
                if (getCancelProductData.size() > 0) {
                    for (OutGoods outGoods : getCancelProductData) {
                        sortingPurchaseRepository.insertData(outGoods.getOrder_id(),
                                "NA", outGoods.getSku(), outGoods.getBatch_no(),
                                outGoods.getBay_no(), outGoods.getQty(), 3, sdf.format(date));


                    }


                } else {
                    sortingPurchaseRepository.deleteQty(order_id);
                    purchaseRepository.changeStatusWithOrderId(order_id);
                    sortingPurchaseRepository.updateWithOrderId(order_id);
                    commonDataRepository.cancelOrder(order_id);
                    transportRepository.cancelOrderNotRunning(order_id);
                    message = "{\"message\":\"Updated Successfully\"}";

                }

            }
        } else {
            int updateData = transportRepository.cancelOrderNotRunning(order_id);
            if (updateData > 0) {
                purchaseRepository.changeStatusWithOrderId(order_id);
                sortingPurchaseRepository.updateWithOrderId(order_id);
                commonDataRepository.cancelOrder(order_id);
                sortingPurchaseRepository.deleteQty(order_id);
                transportRepository.cancelOrderNotRunning(order_id);
                message = "{\"message\":\"Updated Successfully\"}";
            }
        }

        return message;
    }

    public void updateProduction(String sku, String bay_no, String batch_no, int qty, String status) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Production> productionList = productionRepository.getProductionData(batch_no, sku, bay_no, status);
        if (productionList.size() > 0) {
            productionRepository.updateProduction(batch_no, sdf.format(date), qty + productionList.get(0).getQty(),
                    sku, bay_no, status);

        } else {
            productionRepository.insertProduction(batch_no, sdf.format(date), qty, sku, bay_no, status);
        }
    }

    @GetMapping("/getOrderId")
    public Map<String, ArrayList<SortingPurchase>> getOrderId() {
        HashMap<String, ArrayList<SortingPurchase>> hmap = new HashMap<String, ArrayList<SortingPurchase>>();
        ArrayList<Transport> list = (ArrayList<Transport>) transportRepository.getOrderId();
        if (list.size() > 0) {
            ArrayList<SortingPurchase> list2 = (ArrayList<SortingPurchase>) sortingPurchaseRepository.getOrderProduct(list.get(0).getOrder_id());
            transportRepository.updateStatus(list.get(0).getOrder_id());
            sortingPurchaseRepository.updateStatus(list.get(0).getOrder_id());
            hmap.put("order", list2);
        }
        return hmap;
    }

    @PostMapping("/updateTransport")
    public String updateData(@RequestBody Transport transport) {
        String message = "{\"message\":\"Unsuccessful\"}";
        System.out.println(transport.getSku());
        int updateData = transportRepository.updateTransport(transport.getOrder_id(), transport.getTotal_qty(), transport.getSku());
        if (updateData > 0) {
            message = "{\"message\":\"Updated Successfully\"}";
        }
        return message;
    }


    @GetMapping("/generateTExcel")
    public void creating_sheet(HttpServletResponse responses)
            throws IOException, ParseException {
        Calendar cal = Calendar.getInstance();

        Workbook workbook = new HSSFWorkbook();

        //Current Day Report
        {
            Sheet sheet = workbook.createSheet("Today Dispatch Plan");
            HSSFCellStyle style1 = (HSSFCellStyle) workbook.createCellStyle();
            CellStyle style0 = workbook.createCellStyle();
            CellStyle style2 = workbook.createCellStyle();

            style0.setVerticalAlignment(VerticalAlignment.CENTER);
            style0.setAlignment(HorizontalAlignment.CENTER);

            style0.setBorderBottom(BorderStyle.THIN);
            style0.setBorderTop(BorderStyle.THIN);
            style0.setBorderLeft(BorderStyle.THIN);
            style0.setBorderRight(BorderStyle.THIN);
            style0.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
            style0.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());


            style1.setAlignment(HorizontalAlignment.CENTER);
            style1.setVerticalAlignment(VerticalAlignment.CENTER);
            style1.setBorderBottom(BorderStyle.THIN);
            style1.setBorderTop(BorderStyle.THIN);
            style1.setBorderLeft(BorderStyle.THIN);
            style1.setBorderRight(BorderStyle.THIN);

            style2.setAlignment(HorizontalAlignment.CENTER);
            style2.setVerticalAlignment(VerticalAlignment.CENTER);
            style2.setBorderBottom(BorderStyle.THIN);
            style2.setBorderTop(BorderStyle.THIN);
            style2.setBorderLeft(BorderStyle.THIN);
            style2.setBorderRight(BorderStyle.THIN);


            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 10);

            org.apache.poi.ss.usermodel.Font font1 = workbook.createFont();
            font1.setFontHeightInPoints((short) 10);

            style0.setFont((org.apache.poi.ss.usermodel.Font) font);
            style1.setWrapText(true);
            style0.setWrapText(true);
            style1.setFont((org.apache.poi.ss.usermodel.Font) font);
            style2.setFont((org.apache.poi.ss.usermodel.Font) font1);
            CellStyle rsColor = workbook.createCellStyle();
            rsColor.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            rsColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            rsColor.setBorderBottom(BorderStyle.THIN);
            rsColor.setBorderTop(BorderStyle.THIN);
            rsColor.setBorderLeft(BorderStyle.THIN);
            rsColor.setBorderRight(BorderStyle.THIN);
            rsColor.setAlignment(HorizontalAlignment.CENTER);
            rsColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle ibColor = workbook.createCellStyle();
            ibColor.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            ibColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ibColor.setBorderBottom(BorderStyle.THIN);
            ibColor.setBorderTop(BorderStyle.THIN);
            ibColor.setBorderLeft(BorderStyle.THIN);
            ibColor.setBorderRight(BorderStyle.THIN);
            ibColor.setAlignment(HorizontalAlignment.CENTER);
            ibColor.setVerticalAlignment(VerticalAlignment.CENTER);


            CellStyle hpColor = workbook.createCellStyle();
            hpColor.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            hpColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hpColor.setBorderBottom(BorderStyle.THIN);
            hpColor.setBorderTop(BorderStyle.THIN);
            hpColor.setBorderLeft(BorderStyle.THIN);
            hpColor.setBorderRight(BorderStyle.THIN);
            hpColor.setAlignment(HorizontalAlignment.CENTER);
            hpColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle hp12Color = workbook.createCellStyle();
            hp12Color.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            hp12Color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hp12Color.setBorderBottom(BorderStyle.THIN);
            hp12Color.setBorderTop(BorderStyle.THIN);
            hp12Color.setBorderLeft(BorderStyle.THIN);
            hp12Color.setBorderRight(BorderStyle.THIN);
            hp12Color.setAlignment(HorizontalAlignment.CENTER);
            hp12Color.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle ppColor = workbook.createCellStyle();
            ppColor.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            ppColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ppColor.setBorderBottom(BorderStyle.THIN);
            ppColor.setBorderTop(BorderStyle.THIN);
            ppColor.setBorderLeft(BorderStyle.THIN);
            ppColor.setBorderRight(BorderStyle.THIN);
            ppColor.setAlignment(HorizontalAlignment.CENTER);
            ppColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle bprColor = workbook.createCellStyle();
            bprColor.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            bprColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bprColor.setBorderBottom(BorderStyle.THIN);
            bprColor.setBorderTop(BorderStyle.THIN);
            bprColor.setBorderLeft(BorderStyle.THIN);
            bprColor.setBorderRight(BorderStyle.THIN);
            bprColor.setAlignment(HorizontalAlignment.CENTER);
            bprColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle bpqColor = workbook.createCellStyle();
            bpqColor.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            bpqColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bpqColor.setBorderBottom(BorderStyle.THIN);
            bpqColor.setBorderTop(BorderStyle.THIN);
            bpqColor.setBorderLeft(BorderStyle.THIN);
            bpqColor.setBorderRight(BorderStyle.THIN);
            bpqColor.setAlignment(HorizontalAlignment.CENTER);
            bpqColor.setVerticalAlignment(VerticalAlignment.CENTER);


            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
            Row row = sheet.createRow(0);
            Cell cell0_0 = row.createCell(0);
            Cell cell0_1 = row.createCell(1);
            Cell cell0_2 = row.createCell(2);
            Cell cell0_3 = row.createCell(3);
            Cell cell0_4 = row.createCell(4);

            cell0_0.setCellStyle(style0);
            cell0_1.setCellStyle(style0);
            cell0_2.setCellStyle(style0);
            cell0_3.setCellStyle(style0);
            cell0_4.setCellStyle(style0);

            cell0_0.setCellValue("DISPATCH PLAN	");

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));
            Row row1 = sheet.createRow(1);
            Cell cell1_0 = row1.createCell(0);
            row1.createCell(1).setCellStyle(style0);
            row1.createCell(2).setCellStyle(style0);
            row1.createCell(3).setCellStyle(style0);
            row1.createCell(4).setCellStyle(style0);
            row1.createCell(5).setCellStyle(style0);
            row1.createCell(6).setCellStyle(style0);
            row1.createCell(7).setCellStyle(style0);
            row1.createCell(8).setCellStyle(style0);
            row1.createCell(9).setCellStyle(style0);
            row1.createCell(10).setCellStyle(style0);
            row1.createCell(11).setCellStyle(style0);
            row1.createCell(12).setCellStyle(style0);
            row1.createCell(13).setCellStyle(style0);
            row1.createCell(14).setCellStyle(style0);
            row1.createCell(15).setCellStyle(style0);
            row1.createCell(16).setCellStyle(style0);
            row1.createCell(17).setCellStyle(style0);

            cell1_0.setCellStyle(style0);
            cell1_0.setCellValue("PERNOD RICARD INDIA PRIVATE LIMITED , GHOLU MAJRA DERABASSI PUNJAB");

            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
            Row row2 = sheet.createRow(2);
            Cell cell2_0 = row2.createCell(0);
            Cell cell2_4 = row2.createCell(4);
            cell2_0.setCellStyle(style0);
            row2.createCell(1).setCellStyle(style0);
            row2.createCell(2).setCellStyle(style0);
            row2.createCell(3).setCellStyle(style0);
            cell2_4.setCellStyle(style0);
            cell2_0.setCellValue("Month - ");
            int mn = cal.get(Calendar.MONTH);
            cell2_4.setCellValue((mn + 1) + "-" + cal.get(Calendar.YEAR));


            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));
            Row row3 = sheet.createRow(3);
            Cell cell3_0 = row3.createCell(0);
            Cell cell3_4 = row3.createCell(4);
            row3.createCell(1).setCellStyle(style0);
            row3.createCell(2).setCellStyle(style0);
            row3.createCell(3).setCellStyle(style0);
            cell3_0.setCellStyle(style0);
            cell3_4.setCellStyle(style0);

            cell3_0.setCellValue("Date - ");
            cell3_4.setCellValue(cal.get(Calendar.DATE) + "-" + (mn + 1) + "-" + cal.get(Calendar.YEAR));

            // DATA LOOP WILL WORK HERE

            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            System.out.println(day + " " + month);
            List<String> stateList = transportRepository.getTransportDetails(day, month + 1, year);
            HashSet<String> uniqueState = new HashSet<>(stateList);
            int row_inc = 4;
            for (String state1 : uniqueState) {

                Row row4 = sheet.createRow(row_inc);
                Cell cell4_0 = row4.createCell(0);
                Cell cell4_1 = row4.createCell(1);
                Cell cell4_2 = row4.createCell(2);
                Cell cell4_3 = row4.createCell(3);
                Cell cell4_4 = row4.createCell(4);
                Cell cell4_5 = row4.createCell(5);
                Cell cell4_6 = row4.createCell(6);
                Cell cell4_7 = row4.createCell(7);
                Cell cell4_8 = row4.createCell(8);
                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 8, 10));

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 11, 14));
                Cell cell4_11 = row4.createCell(11);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 15, 17));
                Cell cell4_15 = row4.createCell(15);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 18, 21));
                Cell cell4_18 = row4.createCell(18);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 22, 24));
                Cell cell4_22 = row4.createCell(22);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 25, 27));
                Cell cell4_25 = row4.createCell(25);
                Cell cell4_28 = row4.createCell(28);
                Cell cell4_29 = row4.createCell(29);
                Cell cell4_30 = row4.createCell(30);
                Cell cell4_31 = row4.createCell(31);
                Cell cell4_32 = row4.createCell(32);
                Cell cell4_33 = row4.createCell(33);
                Cell cell4_34 = row4.createCell(34);
                Cell cell4_35 = row4.createCell(35);
                row4.createCell(9).setCellStyle(style0);
                row4.createCell(10).setCellStyle(style0);
                row4.createCell(12).setCellStyle(style0);
                row4.createCell(13).setCellStyle(style0);
                row4.createCell(14).setCellStyle(style0);
                row4.createCell(16).setCellStyle(style0);
                row4.createCell(17).setCellStyle(style0);
                row4.createCell(19).setCellStyle(style0);
                row4.createCell(20).setCellStyle(style0);
                row4.createCell(21).setCellStyle(style0);
                row4.createCell(23).setCellStyle(style0);
                row4.createCell(24).setCellStyle(style0);
                row4.createCell(26).setCellStyle(style0);
                row4.createCell(27).setCellStyle(style0);

                cell4_0.setCellStyle(style0);
                cell4_1.setCellStyle(style0);
                cell4_2.setCellStyle(style0);
                cell4_3.setCellStyle(style1);
                cell4_4.setCellStyle(style0);
                cell4_11.setCellStyle(style0);
                cell4_8.setCellStyle(style0);
                cell4_7.setCellStyle(style0);
                cell4_6.setCellStyle(style0);
                cell4_5.setCellStyle(style0);
                cell4_15.setCellStyle(style0);
                cell4_18.setCellStyle(style0);
                cell4_22.setCellStyle(style0);
                cell4_25.setCellStyle(style0);
                cell4_34.setCellStyle(style0);
                cell4_31.setCellStyle(style0);
                cell4_30.setCellStyle(style0);
                cell4_29.setCellStyle(style0);
                cell4_28.setCellStyle(style0);
                cell4_35.setCellStyle(style0);
                cell4_32.setCellStyle(style0);
                cell4_33.setCellStyle(style0);


                cell4_0.setCellValue("SR.NO.");
                cell4_1.setCellValue("STATE");
                cell4_2.setCellValue("CODE");
                cell4_3.setCellValue("PARTY NAME & TOWN");
                cell4_4.setCellValue("PERMIT NO.");
                cell4_5.setCellValue("PERMIT DATE");
                cell4_6.setCellValue("PPQ");
                cell4_6.setCellStyle(ppColor);
                cell4_7.setCellValue("HP12YO");
                cell4_7.setCellStyle(hpColor);
                cell4_8.setCellValue("100 PIPERS");
                cell4_8.setCellStyle(hp12Color);
                cell4_11.setCellValue("ROYAL STAG");
                cell4_11.setCellStyle(rsColor);
                cell4_15.setCellValue("IMPERIAL BLUE");
                cell4_15.setCellStyle(ibColor);
                cell4_18.setCellValue("BLENDERS PRIDE");
                cell4_18.setCellStyle(bpqColor);
                cell4_22.setCellValue("ROYAL STAG BARREL");
                cell4_22.setCellStyle(rsColor);
                cell4_25.setCellValue("BLENDERS PRIDE RESERVE");
                cell4_25.setCellStyle(bprColor);
                cell4_28.setCellValue("TOTAL CASE");
                cell4_29.setCellValue("VEHICLE TYPE");
                cell4_30.setCellValue("ORDER NO.");
                cell4_31.setCellValue("PASS NO.");
                cell4_32.setCellValue("VEHICLE NO.");
                cell4_33.setCellValue("DRIVER NAME");
                cell4_34.setCellValue("CONT.NO");
                cell4_35.setCellValue("LR NO.");
                row_inc += 1;
                Row row5 = sheet.createRow(row_inc);
                Cell cell5_6 = row5.createCell(6);
                Cell cell5_7 = row5.createCell(7);
                Cell cell5_8 = row5.createCell(8);
                Cell cell5_9 = row5.createCell(9);
                Cell cell5_10 = row5.createCell(10);
                Cell cell5_11 = row5.createCell(11);
                Cell cell5_12 = row5.createCell(12);
                Cell cell5_13 = row5.createCell(13);
                Cell cell5_14 = row5.createCell(14);
                Cell cell5_15 = row5.createCell(15);
                Cell cell5_16 = row5.createCell(16);
                Cell cell5_17 = row5.createCell(17);
                Cell cell5_18 = row5.createCell(18);
                Cell cell5_19 = row5.createCell(19);
                Cell cell5_20 = row5.createCell(20);
                Cell cell5_21 = row5.createCell(21);
                Cell cell5_22 = row5.createCell(22);
                Cell cell5_23 = row5.createCell(23);
                Cell cell5_24 = row5.createCell(24);
                Cell cell5_25 = row5.createCell(25);
                Cell cell5_26 = row5.createCell(26);
                Cell cell5_27 = row5.createCell(27);

                row5.createCell(0).setCellStyle(style0);
                row5.createCell(1).setCellStyle(style0);
                row5.createCell(2).setCellStyle(style0);
                row5.createCell(3).setCellStyle(style0);
                row5.createCell(4).setCellStyle(style0);
                row5.createCell(5).setCellStyle(style0);
                row5.createCell(28).setCellStyle(style0);
                row5.createCell(29).setCellStyle(style0);
                row5.createCell(30).setCellStyle(style0);
                row5.createCell(31).setCellStyle(style0);
                row5.createCell(32).setCellStyle(style0);
                row5.createCell(33).setCellStyle(style0);
                row5.createCell(34).setCellStyle(style0);
                row5.createCell(35).setCellStyle(style0);
                cell5_6.setCellStyle(style0);
                cell5_7.setCellStyle(style0);
                cell5_8.setCellStyle(style0);
                cell5_9.setCellStyle(style0);
                cell5_10.setCellStyle(style0);
                cell5_11.setCellStyle(style0);
                cell5_12.setCellStyle(style0);
                cell5_13.setCellStyle(style0);
                cell5_14.setCellStyle(style0);
                cell5_15.setCellStyle(style0);
                cell5_16.setCellStyle(style0);
                cell5_17.setCellStyle(style0);
                cell5_18.setCellStyle(style0);
                cell5_19.setCellStyle(style0);
                cell5_20.setCellStyle(style0);
                cell5_21.setCellStyle(style0);
                cell5_22.setCellStyle(style0);
                cell5_23.setCellStyle(style0);
                cell5_24.setCellStyle(style0);
                cell5_25.setCellStyle(style0);
                cell5_26.setCellStyle(style0);
                cell5_27.setCellStyle(style0);

                cell5_6.setCellValue("Q");
                cell5_7.setCellValue("Q");
                cell5_8.setCellValue("Q");
                cell5_9.setCellValue("P");
                cell5_10.setCellValue("N");
                cell5_11.setCellValue("Q");
                cell5_12.setCellValue("P");
                cell5_13.setCellValue("N");
                cell5_14.setCellValue("2LT");
                cell5_15.setCellValue("Q");
                cell5_16.setCellValue("P");
                cell5_17.setCellValue("N");
                cell5_18.setCellValue("Q");
                cell5_19.setCellValue("P");
                cell5_20.setCellValue("N");
                cell5_21.setCellValue("2LT");
                cell5_22.setCellValue("Q");
                cell5_23.setCellValue("P");
                cell5_24.setCellValue("N");
                cell5_25.setCellValue("Q");
                cell5_26.setCellValue("P");
                cell5_27.setCellValue("N");
                List<Transport> tp_list = transportRepository.getTransportDetails(day, month + 1, year, state1);
                int ppqq = 0, hptq = 0, hpq = 0, hpp = 0, hpn = 0, rsq = 0, rsp = 0, rsn = 0, rs2l = 0, ibq = 0, ibp = 0, ibn = 0;
                int bpq = 0, bpp = 0, bpn = 0, bp2l = 0, rsbq = 0, rsbp = 0, rsbn = 0, bprq = 0, bprp = 0, bprn = 0;
                int ppqqQ = 0, hptqQ = 0, hpqQ = 0, hppQ = 0, hpnQ = 0, rsqQ = 0, rspQ = 0, rsnQ = 0, rs2lQ = 0, ibqQ = 0, ibpQ = 0, ibnQ = 0;
                int bpqQ = 0, bppQ = 0, bpnQ = 0, bp2lQ = 0, rsbqQ = 0, rsbpQ = 0, rsbnQ = 0, bprqQ = 0, bprpQ = 0, bprnQ = 0;
                for (Transport tranport : tp_list) {


                    List<Purchase> p_list = purchaseRepository.getQuantity(tranport.getOrder_id());

                    for (Purchase sp : p_list) {
                        row_inc++;
                        int srno = 1;
                        Row row6 = sheet.createRow(row_inc);
                        Cell sr_no = row6.createCell(0);
                        Cell stat = row6.createCell(1);
                        Cell code = row6.createCell(2);
                        Cell party_name = row6.createCell(3);
                        Cell permit_no = row6.createCell(4);
                        Cell permit_date = row6.createCell(5);
                        Cell ppq_q = row6.createCell(6);
                        Cell hpt_q = row6.createCell(7);
                        Cell hp_q = row6.createCell(8);
                        Cell hp_p = row6.createCell(9);
                        Cell hp_n = row6.createCell(10);
                        Cell rs_q = row6.createCell(11);
                        Cell rs_p = row6.createCell(12);
                        Cell rs_n = row6.createCell(13);
                        Cell rs_tlt = row6.createCell(14);
                        Cell ib_q = row6.createCell(15);
                        Cell ib_p = row6.createCell(16);
                        Cell ib_n = row6.createCell(17);
                        Cell bp_q = row6.createCell(18);
                        Cell bp_p = row6.createCell(19);
                        Cell bp_n = row6.createCell(20);
                        Cell bp_tlt = row6.createCell(21);
                        Cell rsb_q = row6.createCell(22);
                        Cell rsb_p = row6.createCell(23);
                        Cell rsb_n = row6.createCell(24);
                        Cell bpr_q = row6.createCell(25);
                        Cell bpr_p = row6.createCell(26);
                        Cell bpr_n = row6.createCell(27);
                        Cell total_case = row6.createCell(28);
                        Cell weight = row6.createCell(29);
                        Cell order_no = row6.createCell(30);
                        Cell pass_no = row6.createCell(31);
                        Cell vehicle_no = row6.createCell(32);
                        Cell driver_name = row6.createCell(33);
                        Cell cont_no = row6.createCell(34);
                        Cell lr_no = row6.createCell(35);

                        // cell styling
                        sr_no.setCellStyle(style2);
                        stat.setCellStyle(style2);
                        code.setCellStyle(style2);
                        party_name.setCellStyle(style2);
                        permit_no.setCellStyle(style2);
                        permit_date.setCellStyle(style2);
                        ppq_q.setCellStyle(style2);
                        hpt_q.setCellStyle(style2);
                        hp_p.setCellStyle(style2);
                        hp_n.setCellStyle(style2);
                        rs_q.setCellStyle(style2);
                        rs_p.setCellStyle(style2);
                        rs_n.setCellStyle(style2);
                        rs_tlt.setCellStyle(style2);
                        ib_q.setCellStyle(style2);
                        ib_p.setCellStyle(style2);
                        ib_n.setCellStyle(style2);
                        bp_q.setCellStyle(style2);
                        bp_p.setCellStyle(style2);
                        bp_n.setCellStyle(style2);
                        bp_tlt.setCellStyle(style2);
                        rsb_q.setCellStyle(style2);
                        rsb_p.setCellStyle(style2);
                        rsb_n.setCellStyle(style2);
                        bpr_q.setCellStyle(style2);
                        bpr_p.setCellStyle(style2);
                        bpr_n.setCellStyle(style2);
                        total_case.setCellStyle(style2);
                        weight.setCellStyle(style2);
                        order_no.setCellStyle(style2);
                        pass_no.setCellStyle(style2);
                        vehicle_no.setCellStyle(style2);
                        driver_name.setCellStyle(style2);
                        cont_no.setCellStyle(style2);
                        lr_no.setCellStyle(style2);
                        hp_q.setCellStyle(style2);


                        char[] sku = sp.getSku().toCharArray();
                        String qtyType = "";
                        String brand = "", state = "";
                        for (int i = 0; i < sku.length; i++) {
                            if (i < 3) {
                                brand += sku[i];
                            }
                            if (i == 3) {
                                qtyType += sku[i];
                            }
                            if (i > sku.length - 7) {
                                state += sku[i];
                            }
                        }
                        System.out.println(brand);
                        System.out.println(state);

                        System.out.println(qtyType);

                        //adding data in sheet
                        sr_no.setCellValue(srno++);
                        stat.setCellValue(state);
                        //code.setBlank();
                        party_name.setCellValue(tranport.getParty_name());
                        permit_no.setCellValue(sp.getPermit_no());
                        permit_date.setCellValue("");
                        total_case.setCellValue(tranport.getTotal_qty());
                        weight.setCellValue(tranport.getVehicle_type());
                        order_no.setCellValue(sp.getOrder_id());
                        pass_no.setCellValue("");
                        vehicle_no.setCellValue(tranport.getVehicle_no());
                        driver_name.setCellValue(tranport.getDriver_name());
                        cont_no.setCellValue(tranport.getContact_no());
                        lr_no.setCellValue("");


                        if (brand.equalsIgnoreCase("BPW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                bp_q.setCellValue(sp.getQty());
                                bpq += sp.getQty();
                                if (bpqQ == 0) {
                                    bpqQ = productionRepository.getAllQty("BPWQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                bp_p.setCellValue(sp.getQty());
                                bpp += sp.getQty();
                                if (bppQ == 0) {
                                    bppQ = productionRepository.getAllQty("BPWP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                bp_n.setCellValue(sp.getQty());
                                bpn += sp.getQty();
                                if (bpnQ == 0) {
                                    bpnQ = productionRepository.getAllQty("BPWN");
                                }
                            } else {
                                bp_tlt.setCellValue(sp.getQty());
                                bp2l += sp.getQty();
                                if (bp2lQ == 0) {
                                    bp2lQ = productionRepository.getAllQty("BPW2LT");
                                }
                            }
                        } else if (brand.equalsIgnoreCase("BPR")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                bpr_q.setCellValue(sp.getQty());
                                bprq += sp.getQty();
                                if (bprqQ == 0) {
                                    bprqQ = productionRepository.getAllQty("BPRQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                bpr_p.setCellValue(sp.getQty());
                                bprp += sp.getQty();
                                if (bprpQ == 0) {
                                    bprpQ = productionRepository.getAllQty("BPRP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                bpr_n.setCellValue(sp.getQty());
                                bprn += sp.getQty();
                                if (bprnQ == 0) {
                                    bprnQ = productionRepository.getAllQty("BPRN");
                                }
                            }

                        } else if (brand.equalsIgnoreCase("HPT")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                hpt_q.setCellValue(sp.getQty());
                                hptq += sp.getQty();
                                if (hptqQ == 0) {
                                    hptqQ = productionRepository.getAllQty("HPTQ");
                                }
                            }

                        } else if (brand.equalsIgnoreCase("HPS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                hp_q.setCellValue(sp.getQty());
                                hpq += sp.getQty();
                                if (hpqQ == 0) {
                                    hpqQ = productionRepository.getAllQty("HPSQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                hp_p.setCellValue(sp.getQty());
                                hpp += sp.getQty();
                                if (hppQ == 0) {
                                    hppQ = productionRepository.getAllQty("HPSP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                hp_n.setCellValue(sp.getQty());
                                hpn += sp.getQty();
                                if (hpnQ == 0) {
                                    hpnQ = productionRepository.getAllQty("HPSN");
                                }
                            }

                        } else if (brand.equalsIgnoreCase("RSW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                rs_q.setCellValue(sp.getQty());
                                rsq += sp.getQty();
                                if (rsqQ == 0) {
                                    rsqQ = productionRepository.getAllQty("RSWQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                rs_p.setCellValue(sp.getQty());
                                rsp += sp.getQty();
                                if (rspQ == 0) {
                                    rspQ = productionRepository.getAllQty("RSWP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                rs_n.setCellValue(sp.getQty());
                                rsn += sp.getQty();
                                if (rsnQ == 0) {
                                    rsnQ = productionRepository.getAllQty("RSWN");
                                }
                            } else {
                                rs_tlt.setCellValue(sp.getQty());
                                rs2l += sp.getQty();
                                if (rs2lQ == 0) {
                                    rs2lQ = productionRepository.getAllQty("RSW2LT");
                                }
                            }
                        } else if (brand.equalsIgnoreCase("RSS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                rsb_q.setCellValue(sp.getQty());
                                rsbq += sp.getQty();
                                if (rsbqQ == 0) {
                                    rsbqQ = productionRepository.getAllQty("RSSQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                rsb_p.setCellValue(sp.getQty());
                                rsbp += sp.getQty();
                                if (rsbpQ == 0) {
                                    rsbpQ = productionRepository.getAllQty("RSSP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                rsb_n.setCellValue(sp.getQty());
                                rsbn += sp.getQty();
                                if (rsbnQ == 0) {
                                    rsbnQ = productionRepository.getAllQty("RSSN");
                                }
                            }

                        } else if (brand.equalsIgnoreCase("IBW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                ib_q.setCellValue(sp.getQty());
                                ibq += sp.getQty();
                                if (ibqQ == 0) {
                                    ibqQ = productionRepository.getAllQty("IBWQ");
                                }
                            } else if (qtyType.equalsIgnoreCase("P")) {
                                ib_p.setCellValue(sp.getQty());
                                ibp += sp.getQty();
                                if (ibpQ == 0) {
                                    ibpQ = productionRepository.getAllQty("IBWP");
                                }
                            } else if (qtyType.equalsIgnoreCase("N")) {
                                ib_n.setCellValue(sp.getQty());
                                ibn += sp.getQty();
                                if (ibnQ == 0) {
                                    ibnQ = productionRepository.getAllQty("IBWN");
                                }
                            }

                        } else if (brand.equalsIgnoreCase("PPS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                ppq_q.setCellValue(sp.getQty());
                                ppqq += sp.getQty();
                                if (ppqqQ == 0) {
                                    ppqqQ = productionRepository.getAllQty("PPSQ");
                                }
                            }
                        }
                    }


                }

                row_inc += 1;
                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 0, 5));
                Row row7 = sheet.createRow(row_inc);
                Cell cell7_0 = row7.createCell(0);
                cell7_0.setCellStyle(style0);
                row7.createCell(1).setCellStyle(style0);
                row7.createCell(2).setCellStyle(style0);
                row7.createCell(3).setCellStyle(style0);
                row7.createCell(4).setCellStyle(style0);
                row7.createCell(5).setCellStyle(style0);
                cell7_0.setCellValue("TOTAL - ");
                {
                    Cell cellPpq = row7.createCell(6);
                    cellPpq.setCellStyle(style0);
                    cellPpq.setCellValue(ppqq);
                    Cell cellHpt = row7.createCell(7);
                    cellHpt.setCellStyle(style0);
                    cellHpt.setCellValue(hptq);
                    Cell cellHpq = row7.createCell(8);
                    cellHpq.setCellStyle(style0);
                    cellHpq.setCellValue(hpq);
                    Cell cellHpp = row7.createCell(9);
                    cellHpp.setCellStyle(style0);
                    cellHpp.setCellValue(hpp);
                    Cell cellHpn = row7.createCell(10);
                    cellHpn.setCellStyle(style0);
                    cellHpn.setCellValue(hpn);
                    Cell cellRsq = row7.createCell(11);
                    cellRsq.setCellStyle(style0);
                    cellRsq.setCellValue(rsq);
                    Cell cellRsp = row7.createCell(12);
                    cellRsp.setCellStyle(style0);
                    cellRsp.setCellValue(rsp);
                    Cell cellRsn = row7.createCell(13);
                    cellRsn.setCellStyle(style0);
                    cellRsn.setCellValue(rsn);
                    Cell cellRs2l = row7.createCell(14);
                    cellRs2l.setCellStyle(style0);
                    cellRs2l.setCellValue(rs2l);
                    Cell cellIbq = row7.createCell(15);
                    cellIbq.setCellStyle(style0);
                    cellIbq.setCellValue(ibq);
                    Cell cellIbp = row7.createCell(16);
                    cellIbp.setCellStyle(style0);
                    cellIbp.setCellValue(ibp);
                    Cell cellIbn = row7.createCell(17);
                    cellIbn.setCellStyle(style0);
                    cellIbn.setCellValue(ibn);
                    Cell cellBpq = row7.createCell(18);
                    cellBpq.setCellStyle(style0);
                    cellBpq.setCellValue(bpq);
                    Cell cellBpp = row7.createCell(19);
                    cellBpp.setCellStyle(style0);
                    cellBpp.setCellValue(bpp);
                    Cell cellBpn = row7.createCell(20);
                    cellBpn.setCellStyle(style0);
                    cellBpn.setCellValue(bpn);
                    Cell cellBp2l = row7.createCell(21);
                    cellBp2l.setCellStyle(style0);
                    cellBp2l.setCellValue(bp2l);
                    Cell cellRsbq = row7.createCell(22);
                    cellRsbq.setCellStyle(style0);
                    cellRsbq.setCellValue(rsbq);
                    Cell cellRsbp = row7.createCell(23);
                    cellRsbp.setCellStyle(style0);
                    cellRsbp.setCellValue(rsbp);
                    Cell cellRsbn = row7.createCell(24);
                    cellRsbn.setCellStyle(style0);
                    cellRsbn.setCellValue(rsbn);
                    Cell cellBprq = row7.createCell(25);
                    cellBprq.setCellStyle(style0);
                    cellBprq.setCellValue(bprq);
                    Cell cellBprp = row7.createCell(26);
                    cellBprp.setCellStyle(style0);
                    cellBprp.setCellValue(bprp);
                    Cell cellBprn = row7.createCell(27);
                    cellBprn.setCellStyle(style0);
                    cellBprn.setCellValue(bprn);
                }

                row_inc += 1;
                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 0, 5));
                Row row8 = sheet.createRow(row_inc);
                Cell cell8_0 = row8.createCell(0);
                cell8_0.setCellStyle(style0);
                row8.createCell(1).setCellStyle(style0);
                row8.createCell(2).setCellStyle(style0);
                row8.createCell(3).setCellStyle(style0);
                row8.createCell(4).setCellStyle(style0);
                row8.createCell(5).setCellStyle(style0);
                cell8_0.setCellValue("STOCK IN HAND -");
                {
                    Cell cellPpq = row8.createCell(6);
                    cellPpq.setCellStyle(style0);
                    cellPpq.setCellValue(ppqqQ);
                    Cell cellHpt = row8.createCell(7);
                    cellHpt.setCellStyle(style0);
                    cellHpt.setCellValue(hptqQ);
                    Cell cellHpq = row8.createCell(8);
                    cellHpq.setCellStyle(style0);
                    cellHpq.setCellValue(hpqQ);
                    Cell cellHpp = row8.createCell(9);
                    cellHpp.setCellStyle(style0);
                    cellHpp.setCellValue(hppQ);
                    Cell cellHpn = row8.createCell(10);
                    cellHpn.setCellStyle(style0);
                    cellHpn.setCellValue(hpnQ);
                    Cell cellRsq = row8.createCell(11);
                    cellRsq.setCellStyle(style0);
                    cellRsq.setCellValue(rsqQ);
                    Cell cellRsp = row8.createCell(12);
                    cellRsp.setCellStyle(style0);
                    cellRsp.setCellValue(rspQ);
                    Cell cellRsn = row8.createCell(13);
                    cellRsn.setCellStyle(style0);
                    cellRsn.setCellValue(rsnQ);
                    Cell cellRs2l = row8.createCell(14);
                    cellRs2l.setCellStyle(style0);
                    cellRs2l.setCellValue(rs2lQ);
                    Cell cellIbqQ = row8.createCell(15);
                    cellIbqQ.setCellStyle(style0);
                    cellIbqQ.setCellValue(ibqQ);
                    Cell cellIbp = row8.createCell(16);
                    cellIbp.setCellStyle(style0);
                    cellIbp.setCellValue(ibpQ);
                    Cell cellIbn = row8.createCell(17);
                    cellIbn.setCellStyle(style0);
                    cellIbn.setCellValue(ibnQ);
                    Cell cellBpq = row8.createCell(18);
                    cellBpq.setCellStyle(style0);
                    cellBpq.setCellValue(bpqQ);
                    Cell cellBpp = row8.createCell(19);
                    cellBpp.setCellStyle(style0);
                    cellBpp.setCellValue(bppQ);
                    Cell cellBpn = row8.createCell(20);
                    cellBpn.setCellStyle(style0);
                    cellBpn.setCellValue(bpnQ);
                    Cell cellBp2l = row8.createCell(21);
                    cellBp2l.setCellStyle(style0);
                    cellBp2l.setCellValue(bp2lQ);
                    Cell cellRsbq = row8.createCell(22);
                    cellRsbq.setCellStyle(style0);
                    cellRsbq.setCellValue(rsbqQ);
                    Cell cellRsbp = row8.createCell(23);
                    cellRsbp.setCellStyle(style0);
                    cellRsbp.setCellValue(rsbpQ);
                    Cell cellRsbn = row8.createCell(24);
                    cellRsbn.setCellStyle(style0);
                    cellRsbn.setCellValue(rsbnQ);
                    Cell cellBprq = row8.createCell(25);
                    cellBprq.setCellStyle(style0);
                    cellBprq.setCellValue(bprqQ);
                    Cell cellBprp = row8.createCell(26);
                    cellBprp.setCellStyle(style0);
                    cellBprp.setCellValue(bprpQ);
                    Cell cellBprn = row8.createCell(27);
                    cellBprn.setCellStyle(style0);
                    cellBprn.setCellValue(bprnQ);
                }

                row_inc += 1;
            }
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        responses.setHeader("content-disposition", "attachment;filename=Dispatch_plan_" + sdf.format(date) + ".xls");
        workbook.write(responses.getOutputStream());
    }


    @GetMapping("/generateTransExcel")
    public void creating_sheet(HttpServletResponse responses, @RequestParam("date") String date, @RequestParam("to") String to)
            throws IOException, ParseException {
        Calendar cal = Calendar.getInstance();

        Workbook workbook = new HSSFWorkbook();

        //Current Day Report
        {
            Sheet sheet = workbook.createSheet("Today Dispatch Plan");
            HSSFCellStyle style1 = (HSSFCellStyle) workbook.createCellStyle();
            CellStyle style0 = workbook.createCellStyle();
            CellStyle style2 = workbook.createCellStyle();

            style0.setVerticalAlignment(VerticalAlignment.CENTER);
            style0.setAlignment(HorizontalAlignment.CENTER);

            style0.setBorderBottom(BorderStyle.THIN);
            style0.setBorderTop(BorderStyle.THIN);
            style0.setBorderLeft(BorderStyle.THIN);
            style0.setBorderRight(BorderStyle.THIN);
            style0.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
            style0.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());


            style1.setAlignment(HorizontalAlignment.CENTER);
            style1.setVerticalAlignment(VerticalAlignment.CENTER);
            style1.setBorderBottom(BorderStyle.THIN);
            style1.setBorderTop(BorderStyle.THIN);
            style1.setBorderLeft(BorderStyle.THIN);
            style1.setBorderRight(BorderStyle.THIN);

            style2.setAlignment(HorizontalAlignment.CENTER);
            style2.setVerticalAlignment(VerticalAlignment.CENTER);
            style2.setBorderBottom(BorderStyle.THIN);
            style2.setBorderTop(BorderStyle.THIN);
            style2.setBorderLeft(BorderStyle.THIN);
            style2.setBorderRight(BorderStyle.THIN);


            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 10);

            org.apache.poi.ss.usermodel.Font font1 = workbook.createFont();
            font1.setFontHeightInPoints((short) 10);

            style0.setFont((org.apache.poi.ss.usermodel.Font) font);
            style1.setWrapText(true);
            style0.setWrapText(true);
            style1.setFont((org.apache.poi.ss.usermodel.Font) font);
            style2.setFont((org.apache.poi.ss.usermodel.Font) font1);
            CellStyle rsColor = workbook.createCellStyle();
            rsColor.setFillForegroundColor(IndexedColors.GOLD.getIndex());
            rsColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            rsColor.setBorderBottom(BorderStyle.THIN);
            rsColor.setBorderTop(BorderStyle.THIN);
            rsColor.setBorderLeft(BorderStyle.THIN);
            rsColor.setBorderRight(BorderStyle.THIN);
            rsColor.setAlignment(HorizontalAlignment.CENTER);
            rsColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle ibColor = workbook.createCellStyle();
            ibColor.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            ibColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ibColor.setBorderBottom(BorderStyle.THIN);
            ibColor.setBorderTop(BorderStyle.THIN);
            ibColor.setBorderLeft(BorderStyle.THIN);
            ibColor.setBorderRight(BorderStyle.THIN);
            ibColor.setAlignment(HorizontalAlignment.CENTER);
            ibColor.setVerticalAlignment(VerticalAlignment.CENTER);


            CellStyle hpColor = workbook.createCellStyle();
            hpColor.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            hpColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hpColor.setBorderBottom(BorderStyle.THIN);
            hpColor.setBorderTop(BorderStyle.THIN);
            hpColor.setBorderLeft(BorderStyle.THIN);
            hpColor.setBorderRight(BorderStyle.THIN);
            hpColor.setAlignment(HorizontalAlignment.CENTER);
            hpColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle hp12Color = workbook.createCellStyle();
            hp12Color.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            hp12Color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            hp12Color.setBorderBottom(BorderStyle.THIN);
            hp12Color.setBorderTop(BorderStyle.THIN);
            hp12Color.setBorderLeft(BorderStyle.THIN);
            hp12Color.setBorderRight(BorderStyle.THIN);
            hp12Color.setAlignment(HorizontalAlignment.CENTER);
            hp12Color.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle ppColor = workbook.createCellStyle();
            ppColor.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            ppColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ppColor.setBorderBottom(BorderStyle.THIN);
            ppColor.setBorderTop(BorderStyle.THIN);
            ppColor.setBorderLeft(BorderStyle.THIN);
            ppColor.setBorderRight(BorderStyle.THIN);
            ppColor.setAlignment(HorizontalAlignment.CENTER);
            ppColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle bprColor = workbook.createCellStyle();
            bprColor.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            bprColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bprColor.setBorderBottom(BorderStyle.THIN);
            bprColor.setBorderTop(BorderStyle.THIN);
            bprColor.setBorderLeft(BorderStyle.THIN);
            bprColor.setBorderRight(BorderStyle.THIN);
            bprColor.setAlignment(HorizontalAlignment.CENTER);
            bprColor.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle bpqColor = workbook.createCellStyle();
            bpqColor.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            bpqColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bpqColor.setBorderBottom(BorderStyle.THIN);
            bpqColor.setBorderTop(BorderStyle.THIN);
            bpqColor.setBorderLeft(BorderStyle.THIN);
            bpqColor.setBorderRight(BorderStyle.THIN);
            bpqColor.setAlignment(HorizontalAlignment.CENTER);
            bpqColor.setVerticalAlignment(VerticalAlignment.CENTER);


            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
            Row row = sheet.createRow(0);
            Cell cell0_0 = row.createCell(0);
            Cell cell0_1 = row.createCell(1);
            Cell cell0_2 = row.createCell(2);
            Cell cell0_3 = row.createCell(3);
            Cell cell0_4 = row.createCell(4);

            cell0_0.setCellStyle(style0);
            cell0_1.setCellStyle(style0);
            cell0_2.setCellStyle(style0);
            cell0_3.setCellStyle(style0);
            cell0_4.setCellStyle(style0);

            cell0_0.setCellValue("DISPATCH PLAN	");

            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));
            Row row1 = sheet.createRow(1);
            Cell cell1_0 = row1.createCell(0);
            row1.createCell(1).setCellStyle(style0);
            row1.createCell(2).setCellStyle(style0);
            row1.createCell(3).setCellStyle(style0);
            row1.createCell(4).setCellStyle(style0);
            row1.createCell(5).setCellStyle(style0);
            row1.createCell(6).setCellStyle(style0);
            row1.createCell(7).setCellStyle(style0);
            row1.createCell(8).setCellStyle(style0);
            row1.createCell(9).setCellStyle(style0);
            row1.createCell(10).setCellStyle(style0);
            row1.createCell(11).setCellStyle(style0);
            row1.createCell(12).setCellStyle(style0);
            row1.createCell(13).setCellStyle(style0);
            row1.createCell(14).setCellStyle(style0);
            row1.createCell(15).setCellStyle(style0);
            row1.createCell(16).setCellStyle(style0);
            row1.createCell(17).setCellStyle(style0);

            cell1_0.setCellStyle(style0);
            cell1_0.setCellValue("PERNOD RICARD INDIA PRIVATE LIMITED , GHOLU MAJRA DERABASI PUNJAB");

            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
            Row row2 = sheet.createRow(2);
            Cell cell2_0 = row2.createCell(0);
            Cell cell2_4 = row2.createCell(4);
            cell2_0.setCellStyle(style0);
            row2.createCell(1).setCellStyle(style0);
            row2.createCell(2).setCellStyle(style0);
            row2.createCell(3).setCellStyle(style0);
            cell2_4.setCellStyle(style0);
            cell2_0.setCellValue("Month - ");
            int mn = cal.get(Calendar.MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM");
            cell2_4.setCellValue(sdf1.format(sdf.parse(date)));


            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));
            Row row3 = sheet.createRow(3);
            Cell cell3_0 = row3.createCell(0);
            Cell cell3_4 = row3.createCell(4);
            row3.createCell(1).setCellStyle(style0);
            row3.createCell(2).setCellStyle(style0);
            row3.createCell(3).setCellStyle(style0);
            cell3_0.setCellStyle(style0);
            cell3_4.setCellStyle(style0);

            cell3_0.setCellValue("Date Range - ");
            cell3_4.setCellValue(date + " - " + to);

            // DATA LOOP WILL WORK HERE

            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            System.out.println(day + " " + month);
            List<String> stateList = transportRepository.getTransportDetails(date + " 00:00:00", to + " 23:59:59");
            HashSet<String> uniqueState = new HashSet<>(stateList);
            int row_inc = 4;
            for (String state1 : uniqueState) {

                Row row4 = sheet.createRow(row_inc);
                Cell cell4_0 = row4.createCell(0);
                Cell cell4_1 = row4.createCell(1);
                Cell cell4_2 = row4.createCell(2);
                Cell cell4_3 = row4.createCell(3);
                Cell cell4_4 = row4.createCell(4);
                Cell cell4_5 = row4.createCell(5);
                Cell cell4_6 = row4.createCell(6);
                Cell cell4_7 = row4.createCell(7);
                Cell cell4_8 = row4.createCell(8);
                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 8, 10));

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 11, 14));
                Cell cell4_11 = row4.createCell(11);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 15, 17));
                Cell cell4_15 = row4.createCell(15);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 18, 21));
                Cell cell4_18 = row4.createCell(18);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 22, 24));
                Cell cell4_22 = row4.createCell(22);

                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 25, 27));
                Cell cell4_25 = row4.createCell(25);
                Cell cell4_28 = row4.createCell(28);
                Cell cell4_29 = row4.createCell(29);
                Cell cell4_30 = row4.createCell(30);
                Cell cell4_31 = row4.createCell(31);
                Cell cell4_32 = row4.createCell(32);
                Cell cell4_33 = row4.createCell(33);
                Cell cell4_34 = row4.createCell(34);
                Cell cell4_35 = row4.createCell(35);
                row4.createCell(9).setCellStyle(style0);
                row4.createCell(10).setCellStyle(style0);
                row4.createCell(12).setCellStyle(style0);
                row4.createCell(13).setCellStyle(style0);
                row4.createCell(14).setCellStyle(style0);
                row4.createCell(16).setCellStyle(style0);
                row4.createCell(17).setCellStyle(style0);
                row4.createCell(19).setCellStyle(style0);
                row4.createCell(20).setCellStyle(style0);
                row4.createCell(21).setCellStyle(style0);
                row4.createCell(23).setCellStyle(style0);
                row4.createCell(24).setCellStyle(style0);
                row4.createCell(26).setCellStyle(style0);
                row4.createCell(27).setCellStyle(style0);

                cell4_0.setCellStyle(style0);
                cell4_1.setCellStyle(style0);
                cell4_2.setCellStyle(style0);
                cell4_3.setCellStyle(style1);
                cell4_4.setCellStyle(style0);
                cell4_11.setCellStyle(style0);
                cell4_8.setCellStyle(style0);
                cell4_7.setCellStyle(style0);
                cell4_6.setCellStyle(style0);
                cell4_5.setCellStyle(style0);
                cell4_15.setCellStyle(style0);
                cell4_18.setCellStyle(style0);
                cell4_22.setCellStyle(style0);
                cell4_25.setCellStyle(style0);
                cell4_34.setCellStyle(style0);
                cell4_31.setCellStyle(style0);
                cell4_30.setCellStyle(style0);
                cell4_29.setCellStyle(style0);
                cell4_28.setCellStyle(style0);
                cell4_35.setCellStyle(style0);
                cell4_32.setCellStyle(style0);
                cell4_33.setCellStyle(style0);


                cell4_0.setCellValue("SR.NO.");
                cell4_1.setCellValue("STATE");
                cell4_2.setCellValue("CODE");
                cell4_3.setCellValue("PARTY NAME & TOWN");
                cell4_4.setCellValue("PERMIT NO.");
                cell4_5.setCellValue("PERMIT DATE");
                cell4_6.setCellValue("PPQ");
                cell4_6.setCellStyle(ppColor);
                cell4_7.setCellValue("HP12YO");
                cell4_7.setCellStyle(hpColor);
                cell4_8.setCellValue("100 PIPERS");
                cell4_8.setCellStyle(hp12Color);
                cell4_11.setCellValue("ROYAL STAG");
                cell4_11.setCellStyle(rsColor);
                cell4_15.setCellValue("IMPERIAL BLUE");
                cell4_15.setCellStyle(ibColor);
                cell4_18.setCellValue("BLENDERS PRIDE");
                cell4_18.setCellStyle(bpqColor);
                cell4_22.setCellValue("ROYAL STAG BARREL");
                cell4_22.setCellStyle(rsColor);
                cell4_25.setCellValue("BLENDERS PRIDE RESERVE");
                cell4_25.setCellStyle(bprColor);
                cell4_28.setCellValue("TOTAL CASE");
                cell4_29.setCellValue("VEHICLE TYPE");
                cell4_30.setCellValue("ORDER NO.");
                cell4_31.setCellValue("PASS NO.");
                cell4_32.setCellValue("VEHICLE NO.");
                cell4_33.setCellValue("DRIVER NAME");
                cell4_34.setCellValue("CONT.NO");
                cell4_35.setCellValue("LR NO.");
                row_inc += 1;
                Row row5 = sheet.createRow(row_inc);
                Cell cell5_6 = row5.createCell(6);
                Cell cell5_7 = row5.createCell(7);
                Cell cell5_8 = row5.createCell(8);
                Cell cell5_9 = row5.createCell(9);
                Cell cell5_10 = row5.createCell(10);
                Cell cell5_11 = row5.createCell(11);
                Cell cell5_12 = row5.createCell(12);
                Cell cell5_13 = row5.createCell(13);
                Cell cell5_14 = row5.createCell(14);
                Cell cell5_15 = row5.createCell(15);
                Cell cell5_16 = row5.createCell(16);
                Cell cell5_17 = row5.createCell(17);
                Cell cell5_18 = row5.createCell(18);
                Cell cell5_19 = row5.createCell(19);
                Cell cell5_20 = row5.createCell(20);
                Cell cell5_21 = row5.createCell(21);
                Cell cell5_22 = row5.createCell(22);
                Cell cell5_23 = row5.createCell(23);
                Cell cell5_24 = row5.createCell(24);
                Cell cell5_25 = row5.createCell(25);
                Cell cell5_26 = row5.createCell(26);
                Cell cell5_27 = row5.createCell(27);

                row5.createCell(0).setCellStyle(style0);
                row5.createCell(1).setCellStyle(style0);
                row5.createCell(2).setCellStyle(style0);
                row5.createCell(3).setCellStyle(style0);
                row5.createCell(4).setCellStyle(style0);
                row5.createCell(5).setCellStyle(style0);
                row5.createCell(28).setCellStyle(style0);
                row5.createCell(29).setCellStyle(style0);
                row5.createCell(30).setCellStyle(style0);
                row5.createCell(31).setCellStyle(style0);
                row5.createCell(32).setCellStyle(style0);
                row5.createCell(33).setCellStyle(style0);
                row5.createCell(34).setCellStyle(style0);
                row5.createCell(35).setCellStyle(style0);
                cell5_6.setCellStyle(style0);
                cell5_7.setCellStyle(style0);
                cell5_8.setCellStyle(style0);
                cell5_9.setCellStyle(style0);
                cell5_10.setCellStyle(style0);
                cell5_11.setCellStyle(style0);
                cell5_12.setCellStyle(style0);
                cell5_13.setCellStyle(style0);
                cell5_14.setCellStyle(style0);
                cell5_15.setCellStyle(style0);
                cell5_16.setCellStyle(style0);
                cell5_17.setCellStyle(style0);
                cell5_18.setCellStyle(style0);
                cell5_19.setCellStyle(style0);
                cell5_20.setCellStyle(style0);
                cell5_21.setCellStyle(style0);
                cell5_22.setCellStyle(style0);
                cell5_23.setCellStyle(style0);
                cell5_24.setCellStyle(style0);
                cell5_25.setCellStyle(style0);
                cell5_26.setCellStyle(style0);
                cell5_27.setCellStyle(style0);

                cell5_6.setCellValue("Q");
                cell5_7.setCellValue("Q");
                cell5_8.setCellValue("Q");
                cell5_9.setCellValue("P");
                cell5_10.setCellValue("N");
                cell5_11.setCellValue("Q");
                cell5_12.setCellValue("P");
                cell5_13.setCellValue("N");
                cell5_14.setCellValue("2LT");
                cell5_15.setCellValue("Q");
                cell5_16.setCellValue("P");
                cell5_17.setCellValue("N");
                cell5_18.setCellValue("Q");
                cell5_19.setCellValue("P");
                cell5_20.setCellValue("N");
                cell5_21.setCellValue("2LT");
                cell5_22.setCellValue("Q");
                cell5_23.setCellValue("P");
                cell5_24.setCellValue("N");
                cell5_25.setCellValue("Q");
                cell5_26.setCellValue("P");
                cell5_27.setCellValue("N");
                List<Transport> tp_list = transportRepository.getTransportDetails(date + " 00:00:00", to + " 23:59:59", state1);
                int ppqq = 0, hptq = 0, hpq = 0, hpp = 0, hpn = 0, rsq = 0, rsp = 0, rsn = 0, rs2l = 0, ibq = 0, ibp = 0, ibn = 0;
                int bpq = 0, bpp = 0, bpn = 0, bp2l = 0, rsbq = 0, rsbp = 0, rsbn = 0, bprq = 0, bprp = 0, bprn = 0;
                for (Transport tranport : tp_list) {


                    List<Purchase> p_list = purchaseRepository.getQuantity(tranport.getOrder_id());

                    for (Purchase sp : p_list) {
                        row_inc++;
                        int srno = 1;
                        Row row6 = sheet.createRow(row_inc);
                        Cell sr_no = row6.createCell(0);
                        Cell stat = row6.createCell(1);
                        Cell code = row6.createCell(2);
                        Cell party_name = row6.createCell(3);
                        Cell permit_no = row6.createCell(4);
                        Cell permit_date = row6.createCell(5);
                        Cell ppq_q = row6.createCell(6);
                        Cell hpt_q = row6.createCell(7);
                        Cell hp_q = row6.createCell(8);
                        Cell hp_p = row6.createCell(9);
                        Cell hp_n = row6.createCell(10);
                        Cell rs_q = row6.createCell(11);
                        Cell rs_p = row6.createCell(12);
                        Cell rs_n = row6.createCell(13);
                        Cell rs_tlt = row6.createCell(14);
                        Cell ib_q = row6.createCell(15);
                        Cell ib_p = row6.createCell(16);
                        Cell ib_n = row6.createCell(17);
                        Cell bp_q = row6.createCell(18);
                        Cell bp_p = row6.createCell(19);
                        Cell bp_n = row6.createCell(20);
                        Cell bp_tlt = row6.createCell(21);
                        Cell rsb_q = row6.createCell(22);
                        Cell rsb_p = row6.createCell(23);
                        Cell rsb_n = row6.createCell(24);
                        Cell bpr_q = row6.createCell(25);
                        Cell bpr_p = row6.createCell(26);
                        Cell bpr_n = row6.createCell(27);
                        Cell total_case = row6.createCell(28);
                        Cell weight = row6.createCell(29);
                        Cell order_no = row6.createCell(30);
                        Cell pass_no = row6.createCell(31);
                        Cell vehicle_no = row6.createCell(32);
                        Cell driver_name = row6.createCell(33);
                        Cell cont_no = row6.createCell(34);
                        Cell lr_no = row6.createCell(35);

                        // cell styling
                        sr_no.setCellStyle(style2);
                        stat.setCellStyle(style2);
                        code.setCellStyle(style2);
                        party_name.setCellStyle(style2);
                        permit_no.setCellStyle(style2);
                        permit_date.setCellStyle(style2);
                        ppq_q.setCellStyle(style2);
                        hpt_q.setCellStyle(style2);
                        hp_p.setCellStyle(style2);
                        hp_n.setCellStyle(style2);
                        rs_q.setCellStyle(style2);
                        rs_p.setCellStyle(style2);
                        rs_n.setCellStyle(style2);
                        rs_tlt.setCellStyle(style2);
                        ib_q.setCellStyle(style2);
                        ib_p.setCellStyle(style2);
                        ib_n.setCellStyle(style2);
                        bp_q.setCellStyle(style2);
                        bp_p.setCellStyle(style2);
                        bp_n.setCellStyle(style2);
                        bp_tlt.setCellStyle(style2);
                        rsb_q.setCellStyle(style2);
                        rsb_p.setCellStyle(style2);
                        rsb_n.setCellStyle(style2);
                        bpr_q.setCellStyle(style2);
                        bpr_p.setCellStyle(style2);
                        bpr_n.setCellStyle(style2);
                        total_case.setCellStyle(style2);
                        weight.setCellStyle(style2);
                        order_no.setCellStyle(style2);
                        pass_no.setCellStyle(style2);
                        vehicle_no.setCellStyle(style2);
                        driver_name.setCellStyle(style2);
                        cont_no.setCellStyle(style2);
                        lr_no.setCellStyle(style2);
                        hp_q.setCellStyle(style2);


                        char[] sku = sp.getSku().toCharArray();
                        String qtyType = "";
                        String brand = "", state = "";
                        for (int i = 0; i < sku.length; i++) {
                            if (i < 3) {
                                brand += sku[i];
                            }
                            if (i == 3) {
                                qtyType += sku[i];
                            }
                            if (i > sku.length - 7) {
                                state += sku[i];
                            }
                        }
                        System.out.println(brand);
                        System.out.println(state);

                        System.out.println(qtyType);

                        //adding data in sheet
                        sr_no.setCellValue(srno++);
                        stat.setCellValue(state);
                        //code.setBlank();
                        party_name.setCellValue(tranport.getParty_name());
                        permit_no.setCellValue(sp.getPermit_no());
                        permit_date.setCellValue("");
                        total_case.setCellValue(tranport.getTotal_qty());
                        weight.setCellValue(tranport.getVehicle_type());
                        order_no.setCellValue(sp.getOrder_id());
                        pass_no.setCellValue("");
                        vehicle_no.setCellValue(tranport.getVehicle_no());
                        driver_name.setCellValue(tranport.getDriver_name());
                        cont_no.setCellValue(tranport.getContact_no());
                        lr_no.setCellValue("");


                        if (brand.equalsIgnoreCase("BPW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                bp_q.setCellValue(sp.getQty());
                                bpq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                bp_p.setCellValue(sp.getQty());
                                bpp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                bp_n.setCellValue(sp.getQty());
                                bpn += sp.getQty();

                            } else {
                                bp_tlt.setCellValue(sp.getQty());
                                bp2l += sp.getQty();

                            }
                        } else if (brand.equalsIgnoreCase("BPR")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                bpr_q.setCellValue(sp.getQty());
                                bprq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                bpr_p.setCellValue(sp.getQty());
                                bprp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                bpr_n.setCellValue(sp.getQty());
                                bprn += sp.getQty();

                            }

                        } else if (brand.equalsIgnoreCase("HPT")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                hpt_q.setCellValue(sp.getQty());
                                hptq += sp.getQty();

                            }

                        } else if (brand.equalsIgnoreCase("HPS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                hp_q.setCellValue(sp.getQty());
                                hpq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                hp_p.setCellValue(sp.getQty());
                                hpp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                hp_n.setCellValue(sp.getQty());
                                hpn += sp.getQty();

                            }

                        } else if (brand.equalsIgnoreCase("RSW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                rs_q.setCellValue(sp.getQty());
                                rsq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                rs_p.setCellValue(sp.getQty());
                                rsp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                rs_n.setCellValue(sp.getQty());
                                rsn += sp.getQty();

                            } else {
                                rs_tlt.setCellValue(sp.getQty());
                                rs2l += sp.getQty();

                            }
                        } else if (brand.equalsIgnoreCase("RSS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                rsb_q.setCellValue(sp.getQty());
                                rsbq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                rsb_p.setCellValue(sp.getQty());
                                rsbp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                rsb_n.setCellValue(sp.getQty());
                                rsbn += sp.getQty();

                            }

                        } else if (brand.equalsIgnoreCase("IBW")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                ib_q.setCellValue(sp.getQty());
                                ibq += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("P")) {
                                ib_p.setCellValue(sp.getQty());
                                ibp += sp.getQty();

                            } else if (qtyType.equalsIgnoreCase("N")) {
                                ib_n.setCellValue(sp.getQty());
                                ibn += sp.getQty();

                            }

                        } else if (brand.equalsIgnoreCase("PPS")) {
                            if (qtyType.equalsIgnoreCase("Q")) {
                                ppq_q.setCellValue(sp.getQty());
                                ppqq += sp.getQty();

                            }
                        }
                    }


                }

                row_inc += 1;
                sheet.addMergedRegion(new CellRangeAddress(row_inc, row_inc, 0, 5));
                Row row7 = sheet.createRow(row_inc);
                Cell cell7_0 = row7.createCell(0);
                cell7_0.setCellStyle(style0);
                row7.createCell(1).setCellStyle(style0);
                row7.createCell(2).setCellStyle(style0);
                row7.createCell(3).setCellStyle(style0);
                row7.createCell(4).setCellStyle(style0);
                row7.createCell(5).setCellStyle(style0);
                cell7_0.setCellValue("TOTAL - ");
                {
                    Cell cellPpq = row7.createCell(6);
                    cellPpq.setCellStyle(style0);
                    cellPpq.setCellValue(ppqq);
                    Cell cellHpt = row7.createCell(7);
                    cellHpt.setCellStyle(style0);
                    cellHpt.setCellValue(hptq);
                    Cell cellHpq = row7.createCell(8);
                    cellHpq.setCellStyle(style0);
                    cellHpq.setCellValue(hpq);
                    Cell cellHpp = row7.createCell(9);
                    cellHpp.setCellStyle(style0);
                    cellHpp.setCellValue(hpp);
                    Cell cellHpn = row7.createCell(10);
                    cellHpn.setCellStyle(style0);
                    cellHpn.setCellValue(hpn);
                    Cell cellRsq = row7.createCell(11);
                    cellRsq.setCellStyle(style0);
                    cellRsq.setCellValue(rsq);
                    Cell cellRsp = row7.createCell(12);
                    cellRsp.setCellStyle(style0);
                    cellRsp.setCellValue(rsp);
                    Cell cellRsn = row7.createCell(13);
                    cellRsn.setCellStyle(style0);
                    cellRsn.setCellValue(rsn);
                    Cell cellRs2l = row7.createCell(14);
                    cellRs2l.setCellStyle(style0);
                    cellRs2l.setCellValue(rs2l);
                    Cell cellIbq = row7.createCell(15);
                    cellIbq.setCellStyle(style0);
                    cellIbq.setCellValue(ibq);
                    Cell cellIbp = row7.createCell(16);
                    cellIbp.setCellStyle(style0);
                    cellIbp.setCellValue(ibp);
                    Cell cellIbn = row7.createCell(17);
                    cellIbn.setCellStyle(style0);
                    cellIbn.setCellValue(ibn);
                    Cell cellBpq = row7.createCell(18);
                    cellBpq.setCellStyle(style0);
                    cellBpq.setCellValue(bpq);
                    Cell cellBpp = row7.createCell(19);
                    cellBpp.setCellStyle(style0);
                    cellBpp.setCellValue(bpp);
                    Cell cellBpn = row7.createCell(20);
                    cellBpn.setCellStyle(style0);
                    cellBpn.setCellValue(bpn);
                    Cell cellBp2l = row7.createCell(21);
                    cellBp2l.setCellStyle(style0);
                    cellBp2l.setCellValue(bp2l);
                    Cell cellRsbq = row7.createCell(22);
                    cellRsbq.setCellStyle(style0);
                    cellRsbq.setCellValue(rsbq);
                    Cell cellRsbp = row7.createCell(23);
                    cellRsbp.setCellStyle(style0);
                    cellRsbp.setCellValue(rsbp);
                    Cell cellRsbn = row7.createCell(24);
                    cellRsbn.setCellStyle(style0);
                    cellRsbn.setCellValue(rsbn);
                    Cell cellBprq = row7.createCell(25);
                    cellBprq.setCellStyle(style0);
                    cellBprq.setCellValue(bprq);
                    Cell cellBprp = row7.createCell(26);
                    cellBprp.setCellStyle(style0);
                    cellBprp.setCellValue(bprp);
                    Cell cellBprn = row7.createCell(27);
                    cellBprn.setCellStyle(style0);
                    cellBprn.setCellValue(bprn);
                }


                row_inc += 1;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        responses.setHeader("content-disposition", "attachment;filename=Dispatch_plan_" + date + ".xls");
        workbook.write(responses.getOutputStream());
    }


    @GetMapping("/getTransDetails")
    public Map<String, List<Transport>> getTransportDetails() {
        List<Transport> getTransportList = transportRepository.getTransportDetails();
        List<Transport> newList = new ArrayList<>();
        for (Transport transport : getTransportList) {
            List<Purchase> checkOrder = purchaseRepository.getQuantity(transport.getOrder_id());
            if (checkOrder.size() > 0) {
                newList.add(transport);
            }
        }
        HashMap<String, List<Transport>> hMap = new HashMap<>();
        hMap.put("trans", newList);
        return hMap;
    }

    @PostMapping("/addDriverName")
    public String addDriverName(@RequestBody Transport transport) {
        String message = "{\"message\":\"Unsuccessful\"}";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();


        int insert = commonDataRepository.insertData(transport.getOrder_id(),
        		"order", "1", sdf.format(date),transport.getTruck_bay_no());
        if (insert > 0) {
           
        
            int update = transportRepository.addDriverDetails(transport.getOrder_id(),
                    transport.getDriver_name(), transport.getVehicle_no(),
                    transport.getContact_no(), transport.getTruck_bay_no(), transport.getVehicle_type());
            if (update > 0) {
                try {
                	 message = "{\"message\":\"Successful\"}";
                  
                } catch (Exception e) {
                }
            }}

        return message;
    }


    @GetMapping("getTruckBay")
    public Map<String, Set<String>> getTruckBay() {
        Set<String> tempSet = new HashSet<>();
        Set<String> TruckBay = transportRepository.getTruckBay();
        HashMap<String, Set<String>> hMap = new HashMap<>();
        hMap.put("bay", TruckBay);
        return hMap;

    }

    @GetMapping("/getOrderIdWithTruckBayNo")
    public Map<String,String>getOrderIdWithTruckBayNo(@RequestParam(value = "truck_bay_no")String truck_bay_no){
        Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
       SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list=transportRepository.getOrderIdwithTruckBayNo(truck_bay_no,sdf2.format(date) + " 00:00:00", sdf.format(date));
        HashMap<String,String> hmap=new HashMap<>();
        String orderId="0";
        if(list.size()>0) {
            orderId=list.get(0);
            hmap.put("getOrderIdWithTruckBayNo",orderId );
        }
        return  hmap;
    }
}