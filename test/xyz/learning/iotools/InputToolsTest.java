package xyz.learning.iotools;

import org.opencv.core.Mat;

public class InputToolsTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String fileName = "a.png";
		String path = "./image/";
		
		Mat mat = InputTools.openFile(path + fileName);
		System.out.println(mat);
	}

}
