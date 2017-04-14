package xyz.learning.darkChannel;

import xyz.haze.removal.RobustRegression;
import xyz.haze.removal.RobustRegression.Point;

public class LineRegressionTest {
	public static void main(String[] args) {
		RobustRegression rRegression = new RobustRegression();
		for (int i = 0; i < 10; i++) {
			rRegression.addPoint(new Point(i, i + 1));
		}
		System.out.println(rRegression.toString());
		System.out.println(rRegression.getValue(90990).getY());
	}
}
