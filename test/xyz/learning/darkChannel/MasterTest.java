package xyz.learning.darkChannel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import xyz.haze.removal.Master;
import xyz.learning.iotools.ImageViewer;
import xyz.learning.iotools.InputTools;
import xyz.learning.iotools.OutputTools;

public class MasterTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static String path = "./image/output/";
	public static void main(String[] args) throws Exception {
		String fileName = "./image/c.png";
		InputTools input = InputTools.getTools();
		Mat originalImage = input.openFile(fileName);
	
		Master master = new Master();
		
		Mat m2 = master.getMinChannel(originalImage);
		Mat dark_4 = master.getDarkChannel(m2, 4);
		int atomsphericLight = master.getAtomsphericLight(dark_4, originalImage);
		Mat tran = master.getTransmission(dark_4, atomsphericLight);
		Mat tran_img = master.TransMission2Img(tran);
		
		OutputTools outputTools = new OutputTools();
		outputTools.Mat2Image(m2, path, "minChanel.png");
		outputTools.Mat2Image(dark_4, path, "darkPanne_9.png");
		outputTools.Mat2Image(tran_img,path, "tram.png");
		
		
		System.err.println(master.getAtomsphericLight(dark_4, originalImage));
		
		
		System.out.println("ends");
	}
}
