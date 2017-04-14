package xyz.haze;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import xyz.haze.removal.Feature;
import xyz.haze.removal.RobustRegression;
import xyz.haze.removal.RobustRegression.Point;

public class PriticeData {
	private Mat inMat;
	private int PMval;
	
	public Mat getImage() {
		return inMat;
	}

	public int getPMval() {
		return PMval;
	}

	public PriticeData(Mat inMat, int PMval){
		this.inMat = inMat;
		this.PMval = PMval;
	}
	
	public List<Point> getPriticePointList(){
		Feature imageFeatureManger = new Feature(inMat);
		Mat transMat = imageFeatureManger.getTransMission2Img();
		int rows = transMat.rows();
		int cols = transMat.cols();
		int len = 9;
		
		List<Point> priticeDataList = new ArrayList<>();
		
		for(int i = 0; i + len < rows; i += len){
			for(int j = 0; j + len < cols; j += len){
				int avg_tras = 0;
				for(int r = 0; r < len; r ++){
					for(int t = 0; t < len; t ++){
						avg_tras += (int)transMat.get(i + r, j + t)[0];
					}
				}
				avg_tras /= len * len;
				priticeDataList.add(new Point(avg_tras, this.PMval));
			}
		}
		return priticeDataList;
	}
}
