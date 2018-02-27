package sysu.common.util;

import java.util.ArrayList;
import java.util.List;

public class StringTools {
	public static boolean isCode(List<String> messages){
		
		int lines = messages.size();
		int codeLines = 0;
		for(String message:messages){
			if(message.endsWith(";")||message.endsWith("{")||message.endsWith("}")||message.contains("=")||message.contains("==")//���������ַ�
					||message.matches("[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+\\(.*\\)*")//������������
					||message.matches("if\\s*\\(.*\\)")||message.matches("while\\s*\\(.*\\)")||message.matches("for\\s*\\(.*\\)"))//����if����while���
			{
				codeLines++;
			}
		}
		if(codeLines*1.0d/lines>=0.1){
			
			return true;
		}
		return false;
	}
	
	public static double computeSimilarity(List<String> message1,List<String> message2){
		StringBuffer message1StringBuffer = new StringBuffer();
		StringBuffer message2StringBuffer = new StringBuffer();
		for(String s:message1){
			message1StringBuffer.append(s+" ");
		}
		for(String s:message2){
			message2StringBuffer.append(s+" ");
		}
		String message1String = message1StringBuffer.toString();
		String message2String = message2StringBuffer.toString();
		
		List<String> message1List = WordSpliter.split(message1String);
		List<String> message2List = WordSpliter.split(message2String);
		
		List<String> unionStringList = new ArrayList<String>();
		
		for(String m1:message1List){
			if(!isContains(unionStringList,m1)){
				unionStringList.add(m1);
			}
		}
		for(String m2:message2List){
			if(!isContains(unionStringList,m2)){
				unionStringList.add(m2);
			}
		}
		
		List<String> intersetStringList = new ArrayList<String>();
		for(String m:unionStringList){
			if(isContains(message1List,m)&&isContains(message2List,m)){
				intersetStringList.add(m);
			}
		}
		
		return intersetStringList.size()*1.0d/unionStringList.size();
		
	}
	
	public static double computeCodeCommentSimilarity(List<String> commentList,List<String> codeList){
		double result = 0d;
		
		StringBuffer message1StringBuffer = new StringBuffer();
		StringBuffer message2StringBuffer = new StringBuffer();
		for(String s:commentList){
			message1StringBuffer.append(s+" ");
		}
		for(String s:codeList){
			message2StringBuffer.append(s+" ");
		}
		String message1String = message1StringBuffer.toString();
		String message2String = message2StringBuffer.toString();
		
		List<String> message1List = WordSpliter.split(message1String);
//		for(String m:message1List){
//			System.out.print(m+" ");
//		}
//		System.out.println();
		List<String> message2List = WordSpliter.split(message2String);
//		for(String m:message2List){
//			System.out.print(m+" ");
//		}
//		System.out.println();
		List<String> intersetStringList = new ArrayList<String>();
		for(String m:message1List){
			if(isContains(message2List,m)){
				intersetStringList.add(m);
			}
		}
		if(message1List.size()!=0){
			result = intersetStringList.size()*1.0d/message1List.size();
		}
		return result;
	}
	
	private static boolean isContains(List<String> list,String m){
		
		for(String s:list){
			if(s.equals(m)){
				return true;
			}
		}
		return false;
	}
}
