package xyz.learning.darkChannel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import xyz.learning.iotools.ImageViewer;
import xyz.learning.iotools.InputTools;

public class MasterTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {
		String fileName = "./image/c.png";
		InputTools input = InputTools.getTools();
		Mat originalImage = input.openFile(fileName);
		
		ImageViewer showTool0 = new ImageViewer();
		ImageViewer showTool1 = new ImageViewer();
		ImageViewer showTool2 = new ImageViewer();
	
		Master master = new Master();
		
		Mat m2 = master.getTheMinChannel(originalImage);
		Mat pointImage = master.MinValFilter(m2, 9);
		
		showTool0.show(originalImage,"1");
//		showTool1.show(m2,"2");
		showTool2.show(pointImage,"3");
		
		System.out.println("ends");
	}
}
