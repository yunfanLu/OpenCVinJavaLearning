package xyz.haze.removal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.DoubleToLongFunction;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.video.KalmanFilter;

public class Master {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * PixNode 求通道的时候，我们对像素点排序时用的类。
	 * 
	 * @author yunfanlu
	 *
	 */
	class PixNode {
		int x;
		int y;
		int val;

		/**
		 * 
		 * @param x
		 *            是像素点的 x 坐标
		 * @param y
		 *            是像素点的 y 坐标
		 * @param val
		 *            是像素点的灰度值，应该在[0,256)之间
		 */

		PixNode(int x, int y, int val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ") = " + val;
		}

		public int compareTo(PixNode p2) {
			return val > p2.val ? 1 : 0;
		}
	}

	/**
	 *  将一幅彩色点图片转化成灰度图像，每个像素点的值是RGB中最小的值。
	 * 
	 * @param inMat
	 *            这里应该是原图像
	 * @return
	 */
	public Mat getMinChannel(final Mat inMat) {
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
		// inMat.release();
		return res;
	}

	/**
	 *
	 * 最小值滤波
	 * 
	 * @param darkMat
	 *            输入的矩阵，这是一个灰度图像，图像的每个点都是原来图像 RGB 里面的最小值。
	 * @param r
	 *            滤波半径，如果半径是3就是以这个点为中心，左三右三点矩阵，里面求最小值。
	 * @return
	 */
	public Mat getDarkChannel(final Mat darkMat, int r) {
		int rows = darkMat.rows();
		int cols = darkMat.cols();
		Mat res = new Mat(rows, cols, CvType.CV_8U);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int minValInSquare = (int) darkMat.get(i, j)[0];
				for (int dx = Math.max(0, i - r); dx < Math.min(rows, i + r + 1); dx++) {
					for (int dy = Math.max(0, j - r); dy < Math.min(cols, j + r + 1); dy++) {
						minValInSquare = Math.min(minValInSquare, (int) darkMat.get(dx, dy)[0]);
					}
				}
				res.put(i, j, minValInSquare);
				System.err.println("2");
			}
		}
		// inMat.release();
		return res;
	}

	/**
	 * 得到透射率矩阵，每个像素点的透射率是一个 0 - 1.0 的值。 透射率的值 = 暗通道的只 ／ 最大光强度 * w w 是修正值 我吗对于小于
	 * 0.1 的透射率强制设置成 0.1
	 * 
	 * @param drakImg
	 * @param atomsphericLight
	 * @return
	 */
	public Mat getTransmission(Mat drakImg, int atomsphericLight) {
		int rows = drakImg.rows();
		int cols = drakImg.cols();
		Mat res = new Mat();
		res.create(rows, cols, CvType.CV_32FC1);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double data = drakImg.get(i, j)[0] / atomsphericLight;
				data = Math.max(0.1, data);
				data = Math.min(1.0, data);
				data *= 0.95;
				res.put(i, j, data);
				System.err.println(3);
			}
		}
		// System.out.println(res.dump());
		return res;
	}

	/**
	 * 讲透射率图转化成灰度图像，这里的转化方法是，用透射率乘255
	 * 
	 * @param trMat
	 * @return
	 */
	public Mat TransMission2Img(Mat trMat) {
		int rows = trMat.rows();
		int cols = trMat.cols();
		Mat res = new Mat();
		res.create(rows, cols, CvType.CV_8SC1);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double data = (trMat.get(i, j)[0] * 256);
				res.put(i, j, data);
				System.err.println(4);
			}
		}
		// System.out.println(res.dump());
		return res;
	}

	/**
	 * 求最大的光照强度，将暗通道中所有的点进行排序求的前 percent 中的值，然后分两种方法。
	 * 
	 * @param darkChannel
	 *            暗通道图
	 * @param inMat
	 *            原图
	 * @return
	 */

	public int getAtomsphericLight(Mat darkChannel, Mat inMat) {
		return getAtomsphericLight(darkChannel, inMat, true, 0.001);
	}

	/**
	 * 
	 * @param darkChannel
	 * @param inMat
	 * @param meanModel
	 *            是否是用均衡化的方法
	 * @param percent
	 * @return
	 */

	public int getAtomsphericLight(Mat darkChannel, Mat inMat, boolean meanModel, double percent) {
		ArrayList<PixNode> pixList = new ArrayList<>();
		int rows = darkChannel.rows();
		int cols = darkChannel.channels();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				pixList.add(new PixNode(i, j, (int) darkChannel.get(i, j)[0]));
			}
		}
		pixList.sort((p1, p2) -> p1.compareTo(p2));

		int atomsphericLight = 0;

		if (percent * rows * cols < 1.0) {
			int tx = pixList.get(0).x;
			int ty = pixList.get(0).y;

			double[] lights = inMat.get(tx, ty);
			for (int i = 0; i < lights.length; i++) {
				atomsphericLight = Math.max(atomsphericLight, (int) lights[i]);
			}
			return atomsphericLight;
		} else {
			int length = (int) (percent * rows * cols);
			if (meanModel == true) {
				for (int i = 0; i < length; i++) {
					int tx = pixList.get(i).x;
					int ty = pixList.get(i).y;

					double[] lights = inMat.get(tx, ty);
					for (int j = 0; j < lights.length; j++) {
						atomsphericLight += (int) lights[i];
					}
				}
				atomsphericLight /= (percent * rows * cols * 3);

				return atomsphericLight;
			} else {
				for (int i = 0; i < length; i++) {
					int tx = pixList.get(i).x;
					int ty = pixList.get(i).y;

					double[] lights = inMat.get(tx, ty);
					for (int j = 0; j < lights.length; j++) {
						atomsphericLight = Math.max(atomsphericLight, (int) lights[j]);
					}
				}
				return atomsphericLight;
			}
		}
	}
}
