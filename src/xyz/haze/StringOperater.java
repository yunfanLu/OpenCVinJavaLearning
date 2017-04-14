package xyz.haze;

import java.util.ArrayList;
import java.util.List;

public class StringOperater {
	public static int getPMValFromFileName(String fileName){
		List<Integer> _index = new ArrayList<>();
		for(int i = 0; i < fileName.length(); i ++){
			if(fileName.charAt(i) == '_'){
				_index.add(i);
			}
		}
		int beg = _index.get(1);
		int end = _index.get(2);
		String PMString = fileName.substring(beg + 1, end);
		
		int res = Integer.valueOf(PMString);
		return res;
	}
}
