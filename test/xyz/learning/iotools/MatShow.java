package xyz.learning.iotools;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * @author yunfanlu
 *
 */

public class MatShow {
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		Mat mat1 = new Mat(500, 500, CvType.CV_8U);
		for(int i = 0, row = mat1.rows(); i < row; i ++){
			for(int j = 0, col = mat1.cols(); j < col; j ++){
				mat1.put(i, j, i * j * 255 / (row * col));
			}
		}
		ImageViewer imageViewer2 = new ImageViewer();
		imageViewer2.show(mat1);
	}

}
