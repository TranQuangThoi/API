package com.techmarket.api.exportFile;

import com.techmarket.api.constant.UserBaseConstant;
import com.techmarket.api.model.Order;
import com.techmarket.api.repository.OrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
@Service
public class OrderExcelExporter extends ReportAbstract{

    @Autowired
    private OrderRepository orderRepository;
    public void writeTableData(List<Order> data) {
        // data
        List<Order> list = (List<Order>) data;
        // font style content

        Font font = workbook.createFont();
        font.setFontName("Arial"); // Font chữ
        font.setFontHeightInPoints((short) 12);


        CellStyle style = getFontContentExcel();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);


        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setFont(font);
        currencyStyle.setAlignment(HorizontalAlignment.RIGHT);  // Căn phải cho tiền tệ
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("₫#,##0"));

        // starting write on row
        int startRow = 2;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        // write content
        for (Order order : list) {
            Row row = sheet.createRow(startRow++);
            int columnCount = 0;
            createCell(row, columnCount++, order.getId(), style);
            createCell(row, columnCount++, order.getOrderCode(), style);
            createCell(row, columnCount++, order.getCreatedBy(), style);
            createCell(row,columnCount++,dateFormat.format(order.getCreatedDate()),style);
            createCell(row, columnCount++, order.getReceiver(), style);
            createCell(row, columnCount++, order.getTotalMoney(), currencyStyle);
            createCell(row, columnCount++, order.getPhone(), style);
            createCell(row, columnCount++, order.getEmail(), style);
            createCell(row, columnCount++, order.getAddress(), style);
            createCell(row, columnCount++, order.getProvince(), style);
            createCell(row, columnCount++, order.getDistrict(), style);
            createCell(row, columnCount++, order.getWard(), style);
            if(order.getPaymentMethod().equals(UserBaseConstant.PAYMENT_KIND_CASH))
            {
                createCell(row, columnCount++, "Tền mặt", style);
            }else
            {
                createCell(row, columnCount++, "Ví điện tử", style);
            }
            if(order.getIsPaid())
            {
                createCell(row, columnCount++,"Đã thanh toán", style);

            }else {
                createCell(row, columnCount++, "Chưa thanh toán", style);
            }


        }
    }


    public void exportToExcel(HttpServletResponse response, List<Order> data) throws IOException {
        newReportExcel();

        // response  writer to excel
        response = initResponseForExportExcel(response, "order_report");
        ServletOutputStream outputStream = response.getOutputStream();


        // write sheet, title & header
        String[] headers = new String[]{"No", "Code","Create By","Create Date", "Receiver", "Total Money", "Phone", "Email", "Address", "Province", "District", "Ward", "Payment Method","Payment Status"};
        writeTableHeaderExcel("Sheet Order", "Report Order", headers);

        // write content row
        writeTableData(data);

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


}
