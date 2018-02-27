package sysu.common.util;

import java.util.Set;
import java.util.TreeSet;

public class WordTools {
	
	public static Set<String> getSameWordList(Set<String> wordList1,Set<String> wordList2){
		Set<String> sameWordList = new TreeSet<String>();
		for(String word:wordList1){
			if(wordList2.contains(word)){
				sameWordList.add(word);
			}
		}
		return sameWordList;
	}

}
