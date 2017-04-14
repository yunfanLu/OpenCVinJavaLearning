package xyz.learning.darkChannel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import xyz.haze.removal.Feature;
import xyz.learning.iotools.ImageViewer;
import xyz.learning.iotools.InputTools;

public class MasterTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private static String path = "./image/output/";

	public static void main(String[] args) throws Exception {
		String fileName = "./image/e.png";
		Mat originalImage = InputTools.openFile(fileName);

		Feature master = new Feature(originalImage);
		
//		new ImageViewer().show(master.getMinChannelMat(),"getMinChannelMat");
		new ImageViewer().show(master.getTransMission2Img(),"getTransMission2Img");
//		new ImageViewer().show(master.getDarkChannel(),"getDarkChannel");
//		new ImageViewer().show(master.getDarkChannel(),"getDarkChannel");
//		OutputTools outputTools = new OutputTools();
//		outputTools.Mat2Image(master.getMinChannelMat(), path, "minChanel_e.png");
//		outputTools.Mat2Image(master.getDarkChannel(), path, "darkPanne_9_e.png");
//		outputTools.Mat2Image(master.getTransMission2Img(), path, "tram_e.png");

		System.err.println(master.getAtomsphericLight());

		System.out.println("ends");
	}
}
