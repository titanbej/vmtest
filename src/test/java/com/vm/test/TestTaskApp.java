package com.vm.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.test.config.FileReadWriterConfiguration;
import com.vm.test.service.dto.TaskClaim;

public class TestTaskApp  extends VmtestApplicationTests{

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@MockBean
	FileReadWriterConfiguration fileReadWriterConfiguration;
	
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	@Test
	public void testTask() throws Exception {
		TaskClaim  tc= new TaskClaim();
		tc.setGoal(5);
		tc.setStep(2);
		Mockito.when(fileReadWriterConfiguration.write("bcd", "bcd")).thenReturn(true);
		mockMvc.perform(post("/api/generate").content(new ObjectMapper().writeValueAsString(tc)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isAccepted());
	}
	@Test
	public void testTaskWithIncorrectData() throws Exception {
		TaskClaim  tc= new TaskClaim();
		tc.setGoal(1);
		tc.setStep(2);
		Mockito.when(fileReadWriterConfiguration.write("bcd", "bcd")).thenReturn(true);
		mockMvc.perform(post("/api/generate").content(new ObjectMapper().writeValueAsString(tc)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isAccepted());
				
	}
	@Test
	public void testTaskForStatusWithInvalidString() throws Exception {
		Mockito.when(fileReadWriterConfiguration.readFileAsString("abcd")).thenReturn("abcd");
		mockMvc.perform(get("/api/tasks/{UUID}/status", "abcd"))
		.andExpect(status().isBadRequest());
				
	}
	@Test
	public void testTaskForStatusWithvalidString() throws Exception {
		Mockito.when(fileReadWriterConfiguration.readFileAsString("abcd")).thenReturn("{ \"result\":\"SUCCESS\"}");
		mockMvc.perform(get("/api/tasks/{UUID}/status", "abcd"))
		.andExpect(status().isOk());
				
	}
	
	@Test
	public void testTaskForResultWithInvalidString() throws Exception {
		Mockito.when(fileReadWriterConfiguration.readFileAsString("abcd")).thenReturn("abcd");
		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
				requestParams.add("UUID", "abcd");
		mockMvc.perform(get("/api/tasks").params(requestParams)).andDo(print())
		.andExpect(status().isBadRequest());
				
	}
	
	@Test
	public void testTaskForResultWithvalidString() throws Exception {
		Mockito.when(fileReadWriterConfiguration.readFileAsString("bcd")).thenReturn("{ \"result\":\"10,8,6,4,2,0\"}");
		mockMvc.perform(get("/api/tasks").param("UUID", "bcd") )
		.andExpect(status().isOk());
				
	}
}
