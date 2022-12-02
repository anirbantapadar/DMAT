package com.api.dmat.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.dmat.model.Assessment;
import com.api.dmat.model.Users;
import com.api.dmat.repo.AssessmentRepo;
import com.api.dmat.repo.UsersRepo;
import com.api.dmat.responses.downloadreportAPIResponse.DownloadReportResponseClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.api.dmat.exception.ParseException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

@Component
public class DownloadReportAPIService {
	
	@Autowired
	AssessmentRepo assessmentrepository;
	
	@Autowired
	UsersRepo usersrepo;
	
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<DownloadReportResponseClass> listUsers;
	
	
	public DownloadReportAPIService(List<DownloadReportResponseClass> listUsers) {
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
//      cell.setCellValue((String) value);
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
		
		ServletOutputStream outputStream;
      try {
      	outputStream = response.getOutputStream();
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
	}
	
	
	public List fillentries() {
    	// Getting Current Date
    	
    	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Date date = new Date();
    	String currentDateTime = dateFormatter.format(date);
    	System.out.println("Current Date " +currentDateTime);
    	
    	
    	// subtracting 24 hrs from current date time
    	
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	c.add(Calendar.DATE,-1);
    	Date currentDateMinusOneDay = c.getTime();
    	String PreviousDay = dateFormatter.format(currentDateMinusOneDay);
    	System.out.println("Previous Day " + PreviousDay);
    	
    	// Got Previous Day in PreviousDay
    	
    	List<Assessment> allAssessmentEntries = assessmentrepository.findAll();
    	List<DownloadReportResponseClass> entriesToBeSendAsReport = new ArrayList<>();
    	for(int i=0; i<allAssessmentEntries.size();i++) {
    		System.out.println(allAssessmentEntries.get(i).getCreationdatetime().toString());
    		String sample = allAssessmentEntries.get(0).getCreationdatetime().toString();
    		
    		//Comparing the present date with previous date
    		int compareTo = sample.compareTo(PreviousDay);
    		System.out.println(compareTo);
    		if(compareTo==-1) {
    			System.out.println("Gone case");
    		}
    		else {
    			int userid = allAssessmentEntries.get(i).getUserid();
    			Users findByUserid = usersrepo.findByUserid(userid);
    			Date dates = new Date(allAssessmentEntries.get(i).getCreationdatetime().getTime());
                String assessmentcreationdate = dates.toString();
    			DownloadReportResponseClass srrh = new DownloadReportResponseClass(allAssessmentEntries.get(i).getAssessmentid(),findByUserid.getEmailid(),findByUserid.getProjectid(),findByUserid.getProjectname(),findByUserid.getBuname(),allAssessmentEntries.get(i).getStatus(),assessmentcreationdate);
    			entriesToBeSendAsReport.add(srrh);
    			
    		}
    	}
    	for(int i=0; i<entriesToBeSendAsReport.size();i++) {
    		System.out.println(entriesToBeSendAsReport.get(i).getAssessmentid());
    	}
    	return entriesToBeSendAsReport;  	
    }
	
	
}
