package xyz.learning.iotools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * The App class will be responsible for loading the file, 
 * while ImageViewer will display it.
 * @author yunfanlu
 *
 */
public class ImageViewerTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	

	public static void main(String[] args) throws Exception {
		String filePath = "./image/a.png";
		Mat newImage = Imgcodecs.imread(filePath);
		if (newImage.dataAddr() == 0) {
			System.out.println("Couldn't open file " + filePath);
		} else {
			ImageViewer imageViewer = new ImageViewer();
			imageViewer.show(newImage, "Loaded image");
		}
	}
}




















