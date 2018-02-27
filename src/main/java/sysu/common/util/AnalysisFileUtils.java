package sysu.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import sysu.commconsistency.bean.ClassMessage;
import sysu.commconsistency.bean.CommentEntry;

public class AnalysisFileUtils {
	
	public static void getClassesAndComments(File file,List<CommentEntry> commentList,List<ClassMessage> classList) throws ZipException, IOException {
	    Map<String,List<String>> newFileMap = new HashMap<String,List<String>>();
	    Map<String,List<String>> oldFileMap = new HashMap<String,List<String>>();
	    
	    getFiles(file,newFileMap,oldFileMap);
	}
	
	public static void unZip(String filePath, String unZipPath) {

	    File file = new File(filePath);

	    ZipFile zipFile = null;
	    try {
	        zipFile = new ZipFile(file,Charset.forName("UTF-8"));//设置编码格式
	        @SuppressWarnings("rawtypes")
			Enumeration e = zipFile.entries();
	        while(e.hasMoreElements()){
	            ZipEntry zipEntry = (ZipEntry) e.nextElement();
	            String name = zipEntry.getName();
	            if(zipEntry.isDirectory()){
	                name = name.substring(0,name.length()-1);
	                File file2 = new File(unZipPath+File.separator+name);   
	                file2.mkdirs();
	            }else {
	                File file2 = new File(unZipPath+File.separator+name);
	                file2.getParentFile().mkdirs();
	                file2.createNewFile();
	                InputStream in = zipFile.getInputStream(zipEntry);
	                FileOutputStream out = new FileOutputStream(file2);
	                int length = 0;
	                byte[] readByte =new byte[1024];
	                try {
	                    while((length=in.read(readByte,0,1024))!=-1){
	                        out.write(readByte, 0, length);  
	                    }                   
	                } catch (Exception e2) {
	                }finally {                  
	                    in.close();
	                    out.close();
	                }
	            }
	        }

	    } catch (IOException e) {
	    	
	    }
	    }
	
	public static void main(String[] args) throws ZipException, IOException {
		System.out.println(File.separator);
		unZip("/home/angel/文档/注释一致性实验/100.zip","/home/angel/文档/注释一致性实验/100");
	}
	
	private static void getFiles(File file,Map<String,List<String>> newFileMap,Map<String,List<String>> oldFileMap) throws ZipException, IOException {
		@SuppressWarnings("resource")
		ZipFile zf = new ZipFile(file);
	    InputStream in = new BufferedInputStream(new FileInputStream(file));  
        @SuppressWarnings("resource")
		ZipInputStream zin = new ZipInputStream(in);  
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {  
            if (ze.isDirectory()) {
            	
            } else if(ze.getName().endsWith(".java")){
            	BufferedReader br = new BufferedReader(  
                        new InputStreamReader(zf.getInputStream(ze)));
            	String line;
            	List<String> lines = new ArrayList<String>();
                while ((line = br.readLine()) != null) {  
                    lines.add(line);
                }  
                if(ze.getName().split("/")[0].equals("new")){
                	newFileMap.put(ze.getName().split("/")[2], lines);
                }else {
                	oldFileMap.put(ze.getName().split("/")[2], lines);
                }
                br.close();
            }  
        }  
        zin.closeEntry();
    }
}