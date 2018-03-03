package sysu.commconsistency.inserter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sysu.commconsistency.bean.ClassMessage;
import sysu.commconsistency.bean.CodeComment;
import sysu.commconsistency.bean.CommentEntry;
import sysu.commconsistency.bean.CommentWord;
import sysu.commconsistency.bean.Token;
import sysu.common.util.StringTools;
import sysu.common.util.WordSpliter;
import sysu.database.repository.ClassMessageRepository;
import sysu.database.repository.CommentRepository;
import sysu.database.repository.CommentWordRepository;
import sysu.database.repository.RepositoryFactory;

public class CommentWordInserter {

	public static void insert(int versionID) {

		
		CommentRepository commentRepository = RepositoryFactory.getCommentRepository();
		CommentWordRepository commentWordRepository = RepositoryFactory.getCommentWordRepository();
		ClassMessageRepository classRepository = RepositoryFactory.getClassMessageRepository();
		
		List<CommentEntry> commentList = commentRepository.findByVersionID(versionID);
		for (CommentEntry comment:commentList) {
			
			ClassMessage clazz = classRepository.findASingleByVersionIDAndClassID(comment.getVersionID(), comment.getClassID());
			List<CodeComment> newClassCommentList = clazz.getNewComment();
			List<CodeComment> oldClassCommentList = clazz.getOldComment();
			
			Set<Integer> newCommentLineSet = new HashSet<Integer>();
			Set<Integer> oldCommentLineSet = new HashSet<Integer>();
			
			for(CodeComment c:newClassCommentList) {
				if(c.getStartLine()>=comment.getNew_comment_startLine()&&c.getEndLine()<=comment.getNew_scope_endLine()) {
					for(int i=c.getStartLine();i<=c.getEndLine();i++) {
						newCommentLineSet.add(i);
					}
				}
			}
			
			for(CodeComment c:oldClassCommentList) {
				if(c.getStartLine()>=comment.getOld_comment_startLine()&&c.getEndLine()<=comment.getOld_scope_endLine()) {
					for(int i=c.getStartLine();i<=c.getEndLine();i++) {
						oldCommentLineSet.add(i);
					}
				}
			}
			
			List<String> newComment = comment.getNewComment();
			List<String> oldComment = comment.getOldComment();
			List<String> newCode = comment.getNewCode();
			List<String> oldCode = comment.getOldCode();
			
			StringBuffer newCommentStringBuffer = new StringBuffer();
			for (String str : newComment) {
				newCommentStringBuffer.append(str + " ");
			}
			StringBuffer oldCommentStringBuffer = new StringBuffer();
			for (String str : oldComment) {
				oldCommentStringBuffer.append(str + " ");
			}
			StringBuffer newCodeStringBuffer = new StringBuffer();
			for (String str : newCode) {
				newCodeStringBuffer.append(str + " ");
			}
			StringBuffer oldCodeStringBuffer = new StringBuffer();
			for (String str : oldCode) {
				oldCodeStringBuffer.append(str + " ");
			}

			List<String> newCommentWordList = WordSpliter.split(newCommentStringBuffer.toString());
			List<String> oldCommentWordList = WordSpliter.split(oldCommentStringBuffer.toString());
			List<String> newCodeWordList = WordSpliter.split(newCodeStringBuffer.toString());
			List<String> oldCodeWordList = WordSpliter.split(oldCodeStringBuffer.toString());
			
			List<String> addWords = new ArrayList<String>();
			List<String> deleteWords = new ArrayList<String>();
			Set<String> newCodeCommentWords = new HashSet<String>();
			Set<String> oldCodeCommentWords = new HashSet<String>();
			for(String str:oldCommentWordList) {
				if(newCodeWordList.contains(str)) {
					newCodeCommentWords.add(str);
				}
				if(oldCodeWordList.contains(str)) {
					oldCodeCommentWords.add(str);
				}
			}
			for(String str:newCodeCommentWords) {
				if(!oldCodeCommentWords.contains(str)) {
					addWords.add(str);
				}
			}
			
			for(String str:oldCodeCommentWords) {
				if(!newCodeCommentWords.contains(str)) {
					deleteWords.add(str);
				}
			}
			
			double codeSimilarity = StringTools.computeSimilarity(newCodeWordList, oldCodeWordList);
			double newCodeOldCommentSimilarity = StringTools.computeCodeCommentSimilarity(oldCommentWordList, newCodeWordList);
			double oldCodeOldCommentSimilarity = StringTools.computeCodeCommentSimilarity(oldCommentWordList, oldCodeWordList);
			
			CommentWord commentWord = new CommentWord();
			commentWord.setCommentID(comment.getCommentID());
			commentWord.setType(comment.getType());
			commentWord.setNewCommentWords(newCommentWordList);
			commentWord.setOldCommentWords(oldCommentWordList);
			commentWord.setNewCodeWords(newCodeWordList);
			commentWord.setOldCodeWords(oldCodeWordList);
			
			commentWord.setCodeSimilarity(codeSimilarity);
			commentWord.setNewCodeOldCommentSimilarity(newCodeOldCommentSimilarity);
			commentWord.setOldCodeOldCommentSimilarity(oldCodeOldCommentSimilarity);
			
			commentWord.setAddWords(addWords);
			commentWord.setDeleteWords(deleteWords);
			
			commentWordRepository.insert(commentWord);
		}

	}
	
	public static void main(String[] args) throws IOException {

	}

}
