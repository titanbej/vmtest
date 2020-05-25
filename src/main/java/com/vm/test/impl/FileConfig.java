package com.vm.test.impl;

import java.io.IOException;

public interface FileConfig {
	public boolean write(String str, String taskId) throws IOException;
	public  String readFileAsString(String UUID);

}
