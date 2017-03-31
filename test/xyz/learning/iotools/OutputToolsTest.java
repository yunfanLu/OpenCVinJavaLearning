package xyz.learning.iotools;

import java.awt.Frame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class OutputToolsTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		Mat mat = new Mat();
		int rows = 8;
		int cols = 8;
		mat.create(new Size(rows, cols), CvType.CV_8UC3);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				mat.put(i, j, new byte[] { (byte) ((i * i + 123) % 256), (byte) ((j * j + 90) % 256), (byte) (j * i + 189) });
			}
		}

		// ImageViewer imageViewer = new ImageViewer();
		// imageViewer.show(mat);

		// Imgcodecs.imwrite("./image/output/b.png", mat);
		OutputTools outputTools = new OutputTools();
		outputTools.Mat2Image(mat, "./image/output/");
		outputTools.Mat2Image(mat, "./image/output/", "xa.png");
//		outputTools.Mat2Image(mat, "./image/output/", "ty");
		System.out.println("END");
	}
}
