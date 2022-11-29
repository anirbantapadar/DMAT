package com.api.dmat.responses.downloadreportAPIResponse;

import org.springframework.http.HttpStatus;

public class DownloadReportAPIResponseClass {
	private String message;
	private HttpStatus status;
	private Integer statusCode;
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	

	public DownloadReportAPIResponseClass(String message, HttpStatus status, Integer statusCode) {
		this.message = message;
		this.status = status;
		this.statusCode = statusCode;
	}



	public DownloadReportAPIResponseClass() {
	}
}

