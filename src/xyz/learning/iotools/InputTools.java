package xyz.learning.iotools;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import xyz.haze.PriticeData;
import xyz.haze.StringOperater;

/**
 * 输入工具类，单例模式。
 * 
 * @author yunfanlu
 *
 */
public class InputTools {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private final static InputTools singleton = new InputTools();

	private InputTools() {
	}

	/**
	 * 获得当前 InputTools 的对象。
	 * 
	 * @return
	 */
	public static InputTools getTools() {
		return singleton;
	}

	/**
	 * 这个是打开文件的，将一个 image 变成 Mat
	 * 
	 * @param fileName
	 * @return 返回的是输入图片的 Mat 对象
	 * @throws Exception
	 *             有可能是有文件未找到的异常。
	 */
	public static Mat openFile(String fileName){
		Mat newImage = Imgcodecs.imread(fileName);
		if (newImage.dataAddr() == 0) {
			System.err.println(fileName + " is an error file!");
		}
		return newImage;
	}

	/**
	 * 获得一个文件夹下所有的文件-图片的名称。
	 * 
	 * @param path
	 *            文件夹的路径，这里有一个点需要特别注意，就是文件夹的路径的最后一个字符是 ／
	 * @return 返回当前文件夹下的所有文件名字。对象是一个 ArrayList
	 */
	public static ArrayList<String> getPriticeFilesName(String path) {
		ArrayList<String> filesName = new ArrayList<>();
		File file = new File(path);
		File[] filesList = file.listFiles();

		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isFile() == true) {
				filesName.add(filesList[i].getName());
			}
		}
		return filesName;
	}
	
	public static List<PriticeData> getPriticeDataList(){
		String path = "/Users/yunfanlu/WorkPlace/EclipseJava/OpenCVinJavaLearning/image/pritice_set/";
		List<String> fileNames = getPriticeFilesName(path);
		List<PriticeData> priticeDatas = new ArrayList<>();
		for(String fileName:fileNames){
			Mat image = openFile(path + fileName);
			int PMval = StringOperater.getPMValFromFileName(fileName);
			PriticeData priticeData = new PriticeData(image, PMval);
			priticeDatas.add(priticeData);
		}
		return priticeDatas;
	}
}
