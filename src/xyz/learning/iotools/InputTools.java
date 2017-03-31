package xyz.learning.iotools;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class InputTools {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private final static InputTools singleton = new InputTools();

	private InputTools() {}

	public static InputTools getTools() {
		return singleton;
	}

	/**
	 * 这个是打开文件的，将一个 image 变成 Mat
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Mat openFile(String fileName) throws Exception {
		Mat newImage = Imgcodecs.imread(fileName);
		if (newImage.dataAddr() == 0) {
			throw new Exception("Couldn't open file " + fileName);
		}
		return newImage;
	}
}
