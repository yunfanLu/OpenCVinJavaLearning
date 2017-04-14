package xyz.haze.removal;

import java.util.ArrayList;
import java.util.List;

public class RobustRegression {
	/**
	 * Point 类，二维图像上的一个点，有 X，Y 两个坐标。
	 * 
	 * @author yunfanlu
	 *
	 */
	public static class Point {
		private double x;
		private double y;

		/**
		 * Point 的初始化类
		 * 
		 * @param x
		 *            X坐标
		 * @param y
		 *            Y坐标
		 */

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}
	}

	private List<Point> points;
	private double avgX;
	private double avgY;
	private double A;
	private double B;
	private boolean isLastData = false;

	public RobustRegression() {
		points = new ArrayList<>();
	}

	@Override
	public String toString() {
		pritace();
		return "y = " + this.A + " * x + " + this.B;
	}

	public void addPoint(List<Point> pList) {
		for (Point p : pList) {
			points.add(p);
		}
	}

	public void addPoint(Point p) {
		points.add(p);
		isLastData = false;
	}

	/**
	 * 对外对接口。
	 * 
	 * @param x
	 * @return
	 */
	public Point getValue(double x) {
		if (isLastData == false) {
			pritace();
		}
		double y = A + B * x;
		return new Point(x, y);
	}

	/**
	 * 训练求用最小二乘法求参数。
	 */
	private void pritace() {
		if (isLastData == true) {
			return;
		}
		int size = points.size();
		double sumX = 0;
		double sumY = 0;
		for (Point p : points) {
			sumX += p.x;
			sumY += p.y;
		}
		avgX = sumX / size;
		avgY = sumY / size;

		double sumVarianceXY = 0;
		double sumVarianceXX = 0;
		for (Point p : points) {
			sumVarianceXY += (p.x - avgX) * (p.y - avgY);
			sumVarianceXX += (p.x - avgX) * (p.x - avgX);
		}
		B = sumVarianceXY / sumVarianceXX;
		A = avgY - B * avgX;
		isLastData = true;
	}
}
