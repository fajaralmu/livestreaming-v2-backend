package com.fajar.livestreaming.service.entity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.model.BaseModel;
import com.fajar.livestreaming.entity.BaseEntity;
import com.fajar.livestreaming.entity.User;
import com.fajar.livestreaming.entity.setting.EntityProperty;
import com.fajar.livestreaming.service.ProgressService;
import com.fajar.livestreaming.service.SessionValidationService;
import com.fajar.livestreaming.util.EntityPropertyBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EntityReportService {
 
	@Autowired
	private ProgressService progressService;
	@Autowired
	private SessionValidationService sessionValidationService;

	public CustomWorkbook getEntityReport(List<? extends BaseModel> entities, Class<? extends BaseEntity> entityClass,
			HttpServletRequest httpRequest) throws Exception {
		log.info("Generate entity report: {}", entityClass); 
		User currentUser = sessionValidationService.getLoggedUser(httpRequest);
		String requestId = currentUser.getRequestId();
		Class modelClass = BaseEntity.getModelClass(entityClass);
		
		EntityProperty entityProperty = EntityPropertyBuilder.createEntityProperty(modelClass, null);
//		ReportData reportData = ReportData.builder().entities(entities).entityProperty(entityProperty).requestId(requestId).build(); 
	
		EntityReportBuilder reportBuilder = new EntityReportBuilder( entityProperty, entities, requestId);
		reportBuilder.setProgressService(progressService);
		
		progressService.sendProgress(1, 1, 10, false, httpRequest);

		CustomWorkbook file = reportBuilder.buildReport(); 
		
		log.info("Entity Report generated");

		return file;
	}

}
