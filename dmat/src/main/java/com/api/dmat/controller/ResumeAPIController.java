package com.api.dmat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.dmat.exception.AttemptsExceededException;
import com.api.dmat.exception.InvalidAssessmentIdException;
import com.api.dmat.responses.resumeAPIResponse.ResponseRequestObj;
import com.api.dmat.responses.resumeAPIResponse.ResumeAPIResponseClass;
import com.api.dmat.service.ResumeAPIService;

import io.swagger.annotations.ApiOperation;

@RestController
public class ResumeAPIController {

	@Autowired
	ResumeAPIService service;

	@ApiOperation(value = "Resumes the Assessment for the Current User", 
			notes = "Resumes the test by loading the responses data from the respective tables ")
	@PostMapping(value = "resumetest", produces = "application/json")
	public ResponseEntity<ResumeAPIResponseClass> resumeAssessment(@RequestBody ResponseRequestObj request)
			throws InvalidAssessmentIdException, AttemptsExceededException {
		return service.getResponses(request);
	}
}
