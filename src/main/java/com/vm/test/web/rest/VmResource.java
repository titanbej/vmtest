package com.vm.test.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.vm.test.config.FileReadWriterConfiguration;
import com.vm.test.error.BadRequestAlertException;
import com.vm.test.impl.GenerateNum;
import com.vm.test.impl.TaskResultProcess;
import com.vm.test.service.dto.Task;
import com.vm.test.service.dto.TaskClaim;
import com.vm.test.service.dto.TaskResult;
import com.vm.test.service.dto.TaskStatus;

@RestController
@RequestMapping("/api")
public class VmResource {

	
	private final Logger log = LoggerFactory.getLogger(VmResource.class);
	private FileReadWriterConfiguration fileReadWriterConfiguration; 
	private TaskResultProcess taskResultProcess;
	private static ObjectMapper mapper = new ObjectMapper();
	    public VmResource(FileReadWriterConfiguration fileReadWriterConfiguration, TaskResultProcess taskResultProcess) {
		super();
		this.fileReadWriterConfiguration = fileReadWriterConfiguration;
		this.taskResultProcess= taskResultProcess;
	}



		@PostMapping("/generate")
	    public ResponseEntity<Task> activateAccount(@Valid @RequestBody TaskClaim taskClaim) {
			String uuid = Generators.timeBasedGenerator().generate().toString();
			Task tsk= new Task();
	    	tsk.setTask(uuid);
			taskResultProcess.process(taskClaim, uuid,fileReadWriterConfiguration);
			return new ResponseEntity<Task>(tsk, HttpStatus.ACCEPTED);
	        
	    }

		@GetMapping("/tasks/{UUID}/status")
	    public ResponseEntity<TaskStatus>taskStatus(@PathVariable("UUID") String UUID) {
			String result = fileReadWriterConfiguration.readFileAsString(UUID);
			TaskResult taskReult=null;
			try {
				taskReult = mapper.readValue(result, TaskResult.class);
			} catch (Exception e) {
				throw new BadRequestAlertException("invalid json String. ");
			}
			TaskStatus ts = new TaskStatus();
			ts.setResult(taskReult.getStatus());
			return new ResponseEntity<TaskStatus>(ts, HttpStatus.OK);
	        
	    }

		@GetMapping("/tasks")
	    public ResponseEntity<TaskStatus> taskResult(@RequestParam("UUID") String UUID) {
			String result = fileReadWriterConfiguration.readFileAsString(UUID);
			TaskResult taskReult=null;
			try {
				taskReult = mapper.readValue(result, TaskResult.class);
			} catch (Exception e) {
				throw new BadRequestAlertException("invalid json string");
			}
			TaskStatus ts = new TaskStatus();
			ts.setResult(taskReult.getResult());
			return new ResponseEntity<TaskStatus>(ts, HttpStatus.OK);
	        
	    }
		




}
