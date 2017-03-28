package xyz.learning.darkChannel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Master {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 *  将一幅彩色点图片转化成灰度图像，每个像素点的值是RGB中最小的值。
	 * @param inMat
	 * @return
	 */
	public Mat getTheMinChannel(final Mat inMat) {
		int rows = inMat.rows();
		int cols = inMat.cols();
		Mat res = new Mat(rows, cols, CvType.CV_8U);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double[] pixel = inMat.get(i, j);
				int theMinChannel = (int) pixel[0];
				for (int k = 1; k < pixel.length; k++) {
					if (theMinChannel < pixel[k]) {
						theMinChannel = (int) pixel[k];
					}
				}
				res.put(i, j, theMinChannel);
				System.err.println("1");
			}
		}
//		inMat.release();
		return res;
	}

	/**
	 * 最小值滤波
	 * @param inMat 输入的矩阵，这是一个灰度图像，图像的每个点都是原来图像 RGB 里面的最小值。
	 * @param r 滤波半径，如果半径是3就是以这个点为中心，左三右三点矩阵，里面求最小值。
	 * @return
	 */
	public Mat MinValFilter(final Mat inMat, int r) {
		int rows = inMat.rows();
		int cols = inMat.cols();
		Mat res = new Mat(rows, cols, CvType.CV_8U);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int minValInSquare = (int)inMat.get(i, j)[0];
				for(int dx = Math.max(0, i - r); dx < Math.min(rows, i + r + 1); dx ++){
					for(int dy = Math.max(0, j - r); dy < Math.min(cols, j + r + 1); dy ++){
						minValInSquare = Math.min(minValInSquare, (int)inMat.get(dx, dy)[0]);
					}
				}
				res.put(i, j, minValInSquare);
				System.err.println("2");
			}
		}
//		inMat.release();
		return res;
	}
}
