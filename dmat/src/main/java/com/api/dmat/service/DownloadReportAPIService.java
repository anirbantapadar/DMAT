package com.api.dmat.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.dmat.exception.FileNotFoundException;
import com.api.dmat.model.Assessment;
import com.api.dmat.model.Users;
import com.api.dmat.repo.AssessmentRepo;
import com.api.dmat.repo.UsersRepo;
import com.api.dmat.responses.downloadreportAPIResponse.DownloadReportResponseClass;

@Component
public class DownloadReportAPIService {
	
	@Autowired
	AssessmentRepo assessmentrepository;
	
	@Autowired
	UsersRepo usersrepo;
	
		
	public List fillentries() throws FileNotFoundException  {
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
    	if(entriesToBeSendAsReport.size()!=0) {
    	for(int i=0; i<entriesToBeSendAsReport.size();i++) {
    		System.out.println(entriesToBeSendAsReport.get(i).getAssessmentid());
    	
    	}
    	return entriesToBeSendAsReport;
    	}
    	
    	else
    	{
    		return null;
    	}
    	
    }
	
}
