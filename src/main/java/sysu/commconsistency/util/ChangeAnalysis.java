package sysu.commconsistency.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.ast.FileUtils;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import sysu.commconsistency.bean.DiffType;


public class ChangeAnalysis {
	
	
	public static List<DiffType> changeDistill(String file1, String file2) throws Exception {
		
		List<DiffType> diffList = new ArrayList<DiffType>();
		File left = new File(file1);
		File right = new File(file2);
		if(!left.exists() || !right.exists()){
			System.err.println("File do not exists:");
		}
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		try {
		    distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch(Exception e) {
		    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
		       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
		       attach the full stack trace along with the two files that you tried to distill. */
		    System.out.println("Warning: error while change distilling. " + e.getMessage());
		    System.out.println(file1);
		    System.out.println(file2);
		}

		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes != null) {
		    for(SourceCodeChange change : changes) {
		    	
		    	//�仯�����λ��
		    	int oldStart = -1,oldEnd = -1,newStart = -1,newEnd = -1;
		    	
		    	//�仯���������
		    	int newStartLine = 0, newEndLine = 0,oldStartLine = 0,oldEndLine=0;
		    	
		    	//���������ģ���ֻ��¼��������
		    	if (change instanceof Insert) {
		    		Insert insert = (Insert)change;
		    		newStart = insert.getChangedEntity().getStartPosition();
		    		newEnd = insert.getChangedEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    	} 
		    	//���Ǹı�ģ������ļ��;��ļ��ı仯������ж�Ҫ��¼
		    	else if (change instanceof Update){
		    		Update update = (Update)change;
		    		newStart = update.getNewEntity().getStartPosition();
		    		newEnd = update.getNewEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    		oldStart = update.getChangedEntity().getStartPosition();
		    		oldEnd = update.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	
		    	}
		    	//����ɾ���ģ���ֻ��¼���ļ�����
		    	else if(change instanceof Delete){
		    		Delete delete = (Delete)change;
		    		oldStart = delete.getChangedEntity().getStartPosition();
		    		oldEnd = delete.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	}
		    	//�����ƶ��ģ������ļ��;��ļ��ı仯������ж�Ҫ��¼
		    	else if( change instanceof Move){
		    		Move move = (Move)change;
		    		newStart = move.getNewEntity().getStartPosition();
		    		newEnd = move.getNewEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    		oldStart = move.getChangedEntity().getStartPosition();
		    		oldEnd = move.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	} else {
		    		System.out.println("Error no type");
		    	}
		    	
		    	//���仯ʵ����ӵ������б���
		    	ChangeType ct = change.getChangeType();
		    	DiffType diff = new DiffType();
		    	diff.setType(ct.name());
		    	diff.setNewStartLine(newStartLine);
		    	diff.setNewEndLine(newEndLine);
		    	diff.setOldStartLine(oldStartLine);
		    	diff.setOldEndLine(oldEndLine);
		    	diffList.add(diff);
		    }
		}
		return diffList;
	}

	public static int getLineNumber(String file, int position) throws Exception{
		int lineNum = 1;
		String fileContent = FileUtils.getContent(new File(file));
		char[] charArray = fileContent.toCharArray();
		for(int i=0; i<position&&i<charArray.length; i++){
			if(charArray[i]=='\n'){
				lineNum++;
			}
		}		
		return lineNum;
	}
	
	public static void main(String[] args){
		
		String newPath = "E:\\log\\jhotdraw\\100\\new\\src\\AbstractFigure.java";
		String oldPath = "E:\\log\\jhotdraw\\100\\old\\src\\AbstractFigure.java";
		List<DiffType> diffList = null;
		
		try {
			diffList = ChangeAnalysis.changeDistill(oldPath, newPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(diffList!=null){
			for(DiffType d:diffList){
				System.out.println(d.getType()+",old:"+d.getOldStartLine()+"-"+d.getOldEndLine()+",new:"+d.getNewStartLine()+"-"+d.getNewEndLine());
			}
		}
	}
	
	
	
	
}
