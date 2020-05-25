package com.vm.test.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import com.vm.test.error.BadRequestAlertException;
import com.vm.test.impl.FileConfig;

@Configuration
public class FileReadWriterConfiguration implements FileConfig{
	
	@Override
	 public boolean write(String str, String taskId) throws IOException {
	      String convertFile = "/tmp/";
	      File fl= new File(convertFile);
	      fl.mkdir();
	      FileOutputStream fout = new FileOutputStream(new File(fl+"/"+taskId+".txt"));
	      fout.write(str.getBytes());
	      fout.close();
	      return true;
	   }
	 
	@Override
	 public  String readFileAsString(String UUID) {
		    String text = "";
		    try {
		      text = new String(Files.readAllBytes(Paths.get("/tmp/"+UUID+".txt")));
		    } catch (IOException e) {
		      throw new BadRequestAlertException("File name is not valid or may be file is not present");
		    }

		    return text;
		  }
}
