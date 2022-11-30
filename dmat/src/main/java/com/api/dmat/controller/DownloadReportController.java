package com.api.dmat.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dmat.exception.FileNotFoundException;
import com.api.dmat.exception.ParseException;
import com.api.dmat.responses.downloadreportAPIResponse.DownloadReportAPIResponseClass;
import com.api.dmat.service.DownloadReportAPIService;
import com.api.dmat.service.UserExcelExporterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiOperation;

@RestController
public class DownloadReportController {
	
	@Autowired
	DownloadReportAPIService report;
	
	@Autowired
	UserExcelExporterService service;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DownloadReportController.class);
	
	@ApiOperation(value = "Generates an excel report of the entries for the day",
			      notes = "It is a Get request that will generate an excel report")
	@GetMapping(value = "/downloadreport", produces = "application/json")
	public ResponseEntity<?> sendReport(HttpServletResponse response) throws IOException, FileNotFoundException, ParseException { 		
		
		response.setContentType("application/vnd.ms-excel");
		Long startTime;
		LOGGER.info("DownloadReport API Started");
		startTime = System.currentTimeMillis();
		List fillentries = report.fillentries();
		if(fillentries.isEmpty()) {
			//IF DATA IS NOT FOUND IN EXCEL REPORT
			//Logged the error and time taken
			LOGGER.error("No Entries Found");
			LOGGER.info("Time Taken by DownloadReportAPI " + (System.currentTimeMillis() - startTime) + "ms");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DownloadReportAPIResponseClass("No Entries Found!",
					HttpStatus.BAD_REQUEST ,HttpStatus.BAD_REQUEST.value()));
		}else {
			// IF DATA IS FOUND IN EXCEL REPORT
			UserExcelExporterService excelExporter = new UserExcelExporterService(fillentries); 
			excelExporter.export(response);
			
			//Logged the success and time taken
			LOGGER.info("Successfull Downloading");
			LOGGER.info("Time Taken by DownloadReportAPI " + (System.currentTimeMillis() - startTime) + "ms");
			
			//All success
			return ResponseEntity.status(HttpStatus.OK).body(new DownloadReportAPIResponseClass("Successfull Downloading",
					HttpStatus.OK ,HttpStatus.OK.value()));
				

		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	return ResponseEntity.ok()
//  .header("Content_Disposotion", "attachment; filename="+excelExporter.export(response))
//  .contentType(MediaType.APPLICATION_JSON)
//  .body(excelExporter.export(response));
	
	
//	@GetMapping(value = "/downloadreport", produces = "application/json")
//    public void exportToExcel(HttpServletResponse response) throws IOException, FileNotFoundException, ParseException {
//		 response.setContentType("application/vnd.ms-excel");
//		 
////		 DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
////		 String currentDateTime = dateFormatter.format(new Date());
////		 
////		 String headerKey = "Content-Disposition";
////	     String fileName = "DMATReport_"+ currentDateTime +".xlsx";
////	     response.setHeader(headerKey, fileName);
//	     
//	     List fillentries = report.fillentries();
//	     if(fillentries!=null) {
//		     UserExcelExporterService excelExporter = new UserExcelExporterService(fillentries);
//			 excelExporter.export(response);	
//	     }
//	     else
//	     {	
//	    	 
//	    	 return ;
//	     }
//	}
//		 
		 
	
		
		
		
		
		
//		UserExcelExporterService excelExporter = new UserExcelExporterService(fillentries);
//		String file = excelExporter.export();
			
//		return ResponseEntity.status(HttpStatus.OK).body(new DownloadReportAPIResponseClass("Successfull Downloading",
//						HttpStatus.OK.name() ,
//						HttpStatus.OK.value()));

}
