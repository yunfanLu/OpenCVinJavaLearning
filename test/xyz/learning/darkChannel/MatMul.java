package xyz.learning.darkChannel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class MatMul {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static void main(String[] args) {
		Mat m1 = new Mat(10, 10, CvType.CV_8UC1, new Scalar(1));
		System.out.println("m1\r\n" + m1.dump());
		
		Mat m2 = new Mat(1,1,CvType.CV_32FC1, new Scalar(0.95));
		Mat m3 = m1.mul(m2);
		
		System.out.println("m2\r\n" + m2.dump());
		System.out.println("m3\r\n" + m3.dump());
	}
}
