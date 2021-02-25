package com.fajar.livestreaming.service;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebRequest;
import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.service.entity.CustomWorkbook;
import com.fajar.livestreaming.service.entity.EntityReportService;
import com.fajar.livestreaming.service.entity.MasterDataService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExcelReportService {  
	@Autowired
	private EntityReportService entityReportService;
	@Autowired
	private ProgressService progressService;
	@Autowired
	private MasterDataService entityService;
	
	private void initProgress(HttpServletRequest httpRequest) {

		progressService.sendProgress(1, 1, 20, true, httpRequest);
	}

 
	///////////////////////////////////////////////////////////////////////////////////////////////

	public CustomWorkbook generateEntityReport(WebRequest request, HttpServletRequest httpRequest) throws Exception {
		Objects.requireNonNull(request);
		log.info("generateEntityReport, request: {}", request); 

		WebResponse response = entityService.filter(request, null); 
		initProgress(httpRequest);

		CustomWorkbook file = entityReportService.getEntityReport(response.getEntities(), response.getEntityClass(), httpRequest);
		return file;
	}
 
	

}
