package service;

import entity.Address;
import entity.User;
import io.smallrye.mutiny.Multi;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelHelper {
    static String[] HEADERs = {"Name", "UserName", "Address"};
    static String SHEET = "UserList";

    public static ByteArrayInputStream tutorialsToExcel(List<User> userMulti) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            AtomicInteger rowIdx = new AtomicInteger(1);
            for (User user : userMulti) {
                Row row = sheet.createRow(rowIdx.getAndIncrement());
                row.createCell(0).setCellValue(user.getName());
                row.createCell(1).setCellValue(user.getUserName());
                for (Address address : user.getAddresses()) {
                    row = sheet.createRow(rowIdx.getAndIncrement());
                    row.createCell(2).setCellValue(address.getDistrict() + address.getProvince() + address.getTown());
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

}
