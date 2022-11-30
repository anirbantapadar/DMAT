package com.api.dmat.service;

//import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.api.dmat.exception.ParseException;
import com.api.dmat.responses.downloadreportAPIResponse.DownloadReportResponseClass;


import org.apache.poi.ss.usermodel.Row;



@Component
public class UserExcelExporterService {
	
	@Autowired
	DownloadReportAPIService sras;
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<DownloadReportResponseClass> listUsers;
	
	
	public UserExcelExporterService(List<DownloadReportResponseClass> listUsers) {
		this.listUsers = listUsers;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		Row row = (Row) sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());

		createCell(row, 0, "Assessment ID", style); 
//		createCell(row, 0, "User ID", style); 
        createCell(row, 1, "Email Id", style);       
        createCell(row, 2, "Project Id", style);    
        createCell(row, 3, "Project Name", style);
        createCell(row, 4, "BU Name", style);
        createCell(row, 5, "Assessment Status", style);
        createCell(row, 6, "Created At", style);
	
		
	}
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = ( (Row) row).createCell(columnCount);
//        cell.setCellValue((String) value);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
	
	private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(8);
        style.setFont(font);
                 
        for (DownloadReportResponseClass user : listUsers) {
            Row row = (Row) sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, user.getAssessmentid(), style);
            createCell(row, columnCount++, user.getEmailid(), style);
            createCell(row, columnCount++, user.getProjectid(), style);
            createCell(row, columnCount++, user.getProjectName(), style);
            createCell(row, columnCount++, user.getBuname(), style);
            createCell(row, columnCount++, user.getAssessementStatus(), style);
            createCell(row, columnCount++, user.getCreatedAt(), style);
            
             
        }
    }
	
	
	public String export(HttpServletResponse response) throws IOException, FileNotFoundException, ParseException{
		writeHeaderLine();
        writeDataLines();
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String fileName = "DMATReport_"+ currentDateTime +".xlsx";
		response.setHeader(headerKey, fileName);
		
//		  String fileLocation = "C:\\Users\\anirban_tapadar\\Downloads\\dmatProject\\dmat\\FileDirectory\\"+fileName;
//        System.out.println("...........................");
//        System.out.println(fileLocation);            
//        FileOutputStream outputStream;
		ServletOutputStream outputStream;
        try {
        	outputStream = response.getOutputStream();
        	//outputStream = new FileOutputStream(fileLocation);
        } catch(java.io.FileNotFoundException e1) {
        	throw new FileNotFoundException("UnknownErrorOccured filenotfound");
        }
        try {
			workbook.write(outputStream);
		} catch (java.io.IOException e) {
			throw new IOException("Unknown Error Occured");
		} 
        try {
			outputStream.close();
		} catch (java.io.IOException e) {
			throw new IOException("Unknown Error Occured");
		}
        try {
			workbook.close();
		} catch (java.io.IOException e) {
			throw new IOException("Unknown Error Occured");
		}
        return fileName;

//        FileOutputStream outputStream = new FileOutputStream(fileLocation);
//        workbook.write(outputStream); 
//        outputStream.close();
//        workbook.close();
//		
//		return fileName;
	}
	
}