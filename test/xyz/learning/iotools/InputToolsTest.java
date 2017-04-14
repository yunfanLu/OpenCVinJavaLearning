package xyz.learning.iotools;

import java.util.List;

import xyz.haze.StringOperater;

public class InputToolsTest {

	public static void main(String[] args) throws Exception {
		String path = "/Users/yunfanlu/WorkPlace/EclipseJava/OpenCVinJavaLearning/image/pritice_set/";
		List<String> imagesName = InputTools.getPriticeFilesName(path);
		for(String fileName:imagesName){
			System.out.println(fileName);
			System.out.println(StringOperater.getPMValFromFileName(fileName));
		}
	}
}
