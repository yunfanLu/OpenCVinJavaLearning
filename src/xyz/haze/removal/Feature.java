package xyz.haze.removal;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * 
 * @author yunfanlu
 *
 */
public class Feature {
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
	 * inMat：输入矩阵。 minChannelMat：最小值矩阵，每个像素点的值是输入图片r g b里面最小的。
	 * darkChannelMat：暗通道图，由最小值矩阵，用一个 n * n 的矩阵滤波得到的。
	 * atomsphericLight：最大光照强度，暗通道图求出来的。 
	 * transmissionMat：透射率图[0.0 ~ 1.0] *
	 * 0.95暗通道得值比上最大光照强度。 
	 * transMission2ImgMat：将透射率图转化成灰度图像。乘 255
	 */

	private Mat inMat;
	private Mat minChannelMat;
	private Mat darkChannelMat;
	private int atomsphericLight;
	private Mat transmissionMat;
	private Mat transmission2ImgMat;

	public Feature(Mat inMat) {
		this.inMat = inMat;
		this.minChannelMat = null;
		this.darkChannelMat = null;
		this.atomsphericLight = Integer.MIN_VALUE;
		this.transmissionMat = null;
		this.transmission2ImgMat = null;
	}

	public Mat getInMat() {
		return this.inMat;
	}
	
	public void exit(){
		this.minChannelMat.release();
		this.darkChannelMat.release();
		this.transmissionMat.release();
		this.transmission2ImgMat.release();
	}

	/**
	 *  将一幅彩色点图片转化成灰度图像，每个像素点的值是RGB中最小的值。得到暗通道图。
	 * 
	 * @param inMat
	 *            这里应该是原图像
	 * @return
	 */
	public Mat getMinChannelMat() {
		if (minChannelMat == null) {
			int rows = inMat.rows();
			int cols = inMat.cols();
			minChannelMat = new Mat(rows, cols, CvType.CV_8U);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					double[] pixel = inMat.get(i, j);
					int theMinChannel = (int) pixel[0];
					for (int k = 1; k < pixel.length; k++) {
						if (theMinChannel < pixel[k]) {
							theMinChannel = (int) pixel[k];
						}
					}
					minChannelMat.put(i, j, theMinChannel);
					System.err.println("1");
				}
			}
		}
		return minChannelMat;
	}

	public Mat getDarkChannel() {
		return getDarkChannel(3);
	}

	/**
	 *
	 * 对暗通道进行最小值滤波。
	 * 
	 * @param darkMat
	 *            输入的矩阵，这是一个灰度图像，图像的每个点都是原来图像 RGB 里面的最小值。
	 * @param r
	 *            滤波半径，如果半径是3就是以这个点为中心，左三右三点矩阵，里面求最小值。
	 * @return
	 */
	public Mat getDarkChannel(int r) {
		if (darkChannelMat == null) {
			int rows = this.getMinChannelMat().rows();
			int cols = this.getMinChannelMat().cols();
			darkChannelMat = new Mat(rows, cols, CvType.CV_8U);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int minValInSquare = (int) this.getMinChannelMat().get(i, j)[0];
					for (int dx = Math.max(0, i - r); dx < Math.min(rows, i + r + 1); dx++) {
						for (int dy = Math.max(0, j - r); dy < Math.min(cols, j + r + 1); dy++) {
							minValInSquare = Math.min(minValInSquare, (int) this.getMinChannelMat().get(dx, dy)[0]);
						}
					}
					darkChannelMat.put(i, j, minValInSquare);
					System.err.println("2");
				}
			}
		}
		return darkChannelMat;
	}

	/**
	 * 求最大的光照强度，将暗通道中所有的点进行排序求的前 percent 中的值，然后分两种方法。
	 * 
	 * @param darkChannelMat
	 *            暗通道图
	 * @param inMat
	 *            原图
	 * @return
	 */

	public int getAtomsphericLight() {
		return getAtomsphericLight(true, 0.001);
	}

	/**
	 * 求最大的光照强度，将暗通道中所有的点进行排序求的前 percent 中的值，然后分两种方法。
	 * 
	 * @param darkChannelMat
	 * @param inMat
	 * @param meanModel
	 *            是否是用均衡化的方法
	 * @param percent
	 * @return
	 */

	public int getAtomsphericLight(boolean meanModel, double percent) {
		if (this.atomsphericLight != Integer.MIN_VALUE) {
			return this.atomsphericLight;
		}
		ArrayList<PixNode> pixList = new ArrayList<>();
		int rows = darkChannelMat.rows();
		int cols = darkChannelMat.channels();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				pixList.add(new PixNode(i, j, (int) darkChannelMat.get(i, j)[0]));
			}
		}
		pixList.sort((p1, p2) -> p1.compareTo(p2));

		this.atomsphericLight = 0;

		if (percent * rows * cols < 1.0) {
			int tx = pixList.get(0).x;
			int ty = pixList.get(0).y;

			double[] lights = inMat.get(tx, ty);
			for (int i = 0; i < lights.length; i++) {
				this.atomsphericLight = Math.max(this.atomsphericLight, (int) lights[i]);
			}
			return this.atomsphericLight;
		} else {
			int length = (int) (percent * rows * cols);
			if (meanModel == true) {
				for (int i = 0; i < length; i++) {
					int tx = pixList.get(i).x;
					int ty = pixList.get(i).y;

					double[] lights = inMat.get(tx, ty);
					for (int j = 0; j < lights.length; j++) {
						this.atomsphericLight += (int) lights[i];
					}
				}
				this.atomsphericLight /= (percent * rows * cols * 3);

				return this.atomsphericLight;
			} else {
				for (int i = 0; i < length; i++) {
					int tx = pixList.get(i).x;
					int ty = pixList.get(i).y;

					double[] lights = inMat.get(tx, ty);
					for (int j = 0; j < lights.length; j++) {
						this.atomsphericLight = Math.max(this.atomsphericLight, (int) lights[j]);
					}
				}
				return this.atomsphericLight;
			}
		}
	}

	/**
	 * 得到透射率矩阵，每个像素点的透射率是一个 0 - 1.0 的值。 透射率的值 = 暗通道的只 ／ 最大光强度 * w w 是修正值我们对于小于
	 * 0.1 的透射率强制设置成 0.1
	 * 
	 * @param drakImg
	 * @param atomsphericLight
	 * @return
	 */
	public Mat getTransmissionMat() {
		if (this.transmissionMat == null) {
			int rows = this.getDarkChannel().rows();
			int cols = this.getDarkChannel().cols();
			this.transmissionMat = new Mat();
			this.transmissionMat.create(rows, cols, CvType.CV_32FC1);

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					double data = this.getDarkChannel().get(i, j)[0] / this.getAtomsphericLight();
					data = Math.max(0.1, data);
					data = Math.min(1.0, data);
					data *= 0.95;
					this.transmissionMat.put(i, j, data);
					System.err.println(3);
				}
			}
		}
		return this.transmissionMat;
	}

	/**
	 * 讲透射率图转化成灰度图像，这里的转化方法是，用透射率乘255
	 * 
	 * @param trMat
	 * @return
	 */
	public Mat getTransMission2Img() {
		if (this.transmission2ImgMat == null) {
			int rows = this.getTransmissionMat().rows();
			int cols = this.getTransmissionMat().cols();
			transmission2ImgMat = new Mat();
			transmission2ImgMat.create(rows, cols, CvType.CV_8SC1);

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					double data = (255 - (this.getTransmissionMat().get(i, j)[0] * 255));
					// double data = ((trMat.get(i, j)[0] * 255));
					transmission2ImgMat.put(i, j, data);
					System.err.println(4);
				}
			}
		}
		// System.out.println(res.dump());
		return this.transmission2ImgMat;
	}
}
