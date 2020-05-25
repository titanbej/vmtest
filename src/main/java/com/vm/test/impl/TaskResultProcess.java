package com.vm.test.impl;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.test.config.FileReadWriterConfiguration;
import com.vm.test.error.BadRequestAlertException;
import com.vm.test.service.dto.TaskClaim;
import com.vm.test.service.dto.TaskResult;
import com.vm.test.web.rest.VmResource;

@EnableAsync
@Configuration
public class TaskResultProcess {
	private final Logger log = LoggerFactory.getLogger(VmResource.class);
	//HashMap<String, String> map = new HashMap<String, String>();
	private static final String SUCCESS = "SUCCESS";
	private static final String IN_PROGRESS = "IN_PROGRESS";
	private static final String ERROR = "ERROR";
	private static ObjectMapper mapper= new ObjectMapper();

	@Async
	public void process(TaskClaim taskClaim, String uuid, FileReadWriterConfiguration fileWriterConfiguration)  {
			TaskResult tkr = new TaskResult();
	    	tkr.setGoal(taskClaim.getGoal());
	    	tkr.setStep(taskClaim.getStep());
			log.info("uuid : "+uuid);
			if(taskClaim.getGoal() < taskClaim.getStep()) {
				//map.put(uuid, ERROR);
				tkr.setStatus(ERROR);
				tkr.setResult("Invalid request goal value can not be small than step value.");
				try {
					fileWriterConfiguration.write(mapper.writeValueAsString(tkr), uuid);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				throw new BadRequestAlertException("Invalid request goal value can not be less than step value.");
			}
			//map.put(uuid, IN_PROGRESS);
			tkr.setStatus(IN_PROGRESS);
			GenerateNum num = new GenerateNum();
			try {
			String result = num.generateNumber(taskClaim.getGoal() , taskClaim.getStep());
	    	//map.put(uuid, SUCCESS);
	    	tkr.setStatus(SUCCESS);
	    	tkr.setResult(result.substring(0, result.lastIndexOf(",")));
			fileWriterConfiguration.write(mapper.writeValueAsString(tkr), uuid);
			} catch (Exception e) {
				try {
					//map.put(uuid, ERROR);
			    	tkr.setStatus(ERROR);
					fileWriterConfiguration.write(mapper.writeValueAsString(tkr), uuid);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				throw new BadRequestAlertException("not valid");
			}
		}
		
}
