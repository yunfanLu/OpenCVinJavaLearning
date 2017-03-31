package xyz.learning.iotools;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class OutputTools {
	public void Mat2Image(Mat mat, String path, String fileName) {
		Imgcodecs.imwrite(path + fileName, mat);
	}

	public void Mat2Image(Mat mat, String path) {
		String fileName = mat.type() + "_" + mat.hashCode() + "_" + ((int) Math.random() * 100) % 97 + ".png";
		Mat2Image(mat, path, fileName);
	}
}
