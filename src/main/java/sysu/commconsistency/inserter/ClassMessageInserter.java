package sysu.commconsistency.inserter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;

import sysu.commconsistency.bean.ClassMessage;
import sysu.commconsistency.bean.Code;
import sysu.commconsistency.bean.CodeComment;
import sysu.commconsistency.bean.DiffType;
import sysu.commconsistency.bean.Token;
import sysu.commconsistency.util.ChangeAnalysis;
import sysu.commconsistency.util.Parser2;
import sysu.commconsistency.util.Tokenizer2;
import sysu.database.repository.ClassMessageRepository;
import sysu.database.repository.RepositoryFactory;


public class ClassMessageInserter {
	
	private static ClassMessageRepository classMessageRepo = RepositoryFactory.getClassMessageRepository();

	public static void insertClass(String newClassPath,String oldClassPath,int versionID,int classID) {

		String splitSeparator = File.separator;
		if(splitSeparator.equals("\\")) {
			splitSeparator = "\\\\";
		}
		String className = "";
		if(newClassPath!=null&&!newClassPath.equals("")) {
			String[] temps = newClassPath.split(splitSeparator);
			className = temps[temps.length-1].substring(0, temps[temps.length-1].indexOf(".java"));
		}else {
			String[] temps = oldClassPath.split(splitSeparator);
			className = temps[temps.length-1].substring(0, temps[temps.length-1].indexOf(".java"));
		}
		
		CompilationUnit newUnit = null;
		CompilationUnit oldUnit = null;
		boolean isNew = false;
		boolean isChange = false;
		boolean isDelete = false;

		String newSource = "";
		String oldSource = "";

		File newFile = new File(newClassPath);
		File oldFile = new File(oldClassPath);
		if (newFile.exists() && oldFile.exists()) {
			isChange = true;
		} else if (newFile.exists() && !oldFile.exists()) {
			isNew = true;
		} else if (!newFile.exists() && oldFile.exists()) {
			isDelete = true;
		}

		if (isNew || isChange) {
			try {
				newSource = fileToString(newClassPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (isChange || isDelete) {
			try {
				oldSource = fileToString(oldClassPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);
		Tokenizer2 ntk = null, otk = null;
		List<CodeComment> newCommentList = null;
		List<CodeComment> oldCommentList = null;
		if (isNew || isChange) {
			char[] newContent = newSource.toCharArray();
			parser.setSource(newContent);
			parser.setUnitName(newClassPath);
			newUnit = (CompilationUnit) parser.createAST(null);
			ntk = Parser2.parseAST2Tokens(newUnit);
			newCommentList = comment2CodeComment(newUnit);
		}

		if (isChange || isDelete) {
			char[] oldContent = oldSource.toCharArray();
			parser.setSource(oldContent);
			parser.setUnitName(oldClassPath);
			oldUnit = (CompilationUnit) parser.createAST(null);
			otk = Parser2.parseAST2Tokens(oldUnit);
			oldCommentList = comment2CodeComment(oldUnit);
		}
		List<DiffType> diffList = null;

		String type = "";
		if (isChange) {
			type = "change";
			try {
				diffList = ChangeAnalysis.changeDistill(newClassPath, oldClassPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (isNew) {
			type = "new";
		} else if (isDelete) {
			type = "delete";
		}

		Code code = new Code(newClassPath, oldClassPath);

		ClassMessage classMessage = new ClassMessage();
		classMessage.setClassName(className);
		classMessage.setType(type);
		classMessage.setClassID(classID);
		classMessage.setVersionID(versionID);

		classMessage.setNewCode(code.getNewLines());
		classMessage.setOldCode(code.getOldLines());
		if (ntk == null) {
			classMessage.setNewTokenList(new ArrayList<Token>());
		} else {
			classMessage.setNewTokenList(ntk.getTokenList());
		}
		if (otk == null) {
			classMessage.setOldTokenList(new ArrayList<Token>());
		} else {
			classMessage.setOldTokenList(otk.getTokenList());
		}
		classMessage.setDiffList(diffList);
		classMessage.setNewComment(newCommentList);
		classMessage.setOldComment(oldCommentList);
		
		if(diffList!=null&&diffList.size()>0) {
		    classMessageRepo.insert(classMessage);
		}

		

	}

	/* Convert a file into a String */
	private static String fileToString(String path) throws IOException {

		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	private static List<CodeComment> comment2CodeComment(CompilationUnit unit) {
		List<CodeComment> codeCommentList = new ArrayList<CodeComment>();

		List<Comment> unitCommentList = unit.getCommentList();
		for (Comment comm : unitCommentList) {
			CodeComment ccomment = new CodeComment();

			int startLine = unit.getLineNumber(comm.getStartPosition());
			int endLine = unit.getLineNumber(comm.getStartPosition() + comm.getLength() - 1);
			ccomment.setStartLine(startLine);
			ccomment.setEndLine(endLine);

			if (comm instanceof Javadoc) {
				ccomment.setType("Javadoc");
			} else if (comm instanceof BlockComment) {
				ccomment.setType("Block");
			} else {
				ccomment.setType("Line");
			}

			codeCommentList.add(ccomment);
		}

		return codeCommentList;
	}

	public static void insertVersion(String filePath, int versionID) {

			File oldDir = new File(filePath+"/old");
			File newDir = new File(filePath+"/new");

			if (oldDir.exists() && newDir.exists() && oldDir.isDirectory() && newDir.isDirectory()) {
				List<String> newFileList = new ArrayList<String>();
				List<String> oldFileList = new ArrayList<String>();

				Queue<File> oldDirQueue = new LinkedList<File>();
				oldDirQueue.add(oldDir);
				while(!oldDirQueue.isEmpty()) {
				    File dir = oldDirQueue.poll();
				    File[] files = dir.listFiles();
				    for(File file:files) {
				    	if(file.isDirectory()) {
				    		oldDirQueue.add(file);
				    	}else if(file.getName().endsWith(".java")){
				    		oldFileList.add(file.getAbsolutePath());
				    	}
				    }
				}
				
				Queue<File> newDirQueue = new LinkedList<File>();
				newDirQueue.add(newDir);
				while(!newDirQueue.isEmpty()) {
				    File dir = newDirQueue.poll();
				    File[] files = dir.listFiles();
				    for(File file:files) {
				    	if(file.isDirectory()) {
				    		newDirQueue.add(file);
				    	}else if(file.getName().endsWith(".java")){
				    		newFileList.add(file.getAbsolutePath());
				    	}
				    }
				}
				
				int classID = 0;
				String splitSeparator = File.separator;
				if(splitSeparator.equals("\\")) {
					splitSeparator = "\\\\";
				}
				for(String str:newFileList) {
					System.out.println(str);
					
					String[] temps = str.split(splitSeparator);
					String oldPath = "";
					for(int i=0;i<temps.length-1;i++) {
						if(temps[i].equals("new")) {
							oldPath += "old"+File.separator;
						}else {
							oldPath += temps[i]+File.separator;
						}
					}
					oldPath += temps[temps.length-1];
					
					insertClass(str, oldPath, versionID, classID);
					classID ++;
				}
			}
	}
}