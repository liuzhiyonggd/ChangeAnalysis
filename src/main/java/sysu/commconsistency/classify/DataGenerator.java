package sysu.commconsistency.classify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sysu.commconsistency.bean.ClassMessage;
import sysu.commconsistency.bean.CommentEntry;
import sysu.commconsistency.bean.CommentWord;
import sysu.commconsistency.bean.DiffType;
import sysu.commconsistency.bean.Token;
import sysu.database.repository.ClassMessageRepository;
import sysu.database.repository.CommentRepository;
import sysu.database.repository.CommentWordRepository;
import sysu.database.repository.RepositoryFactory;

public class DataGenerator {

	public static List<List<Double>> generate(int versionID) throws IOException {

		List<List<Double>> data = new ArrayList<List<Double>>();
		CommentRepository commentRepository = RepositoryFactory.getCommentRepository();

		List<CommentEntry> commentList = commentRepository.findByVersionID(versionID);
		for (CommentEntry comment : commentList) {
			List<Double> vector = extractVector(comment);
			data.add(vector);
		}
		return data;
	}

	public static List<Double> extractVector(CommentEntry comment) throws IOException {

		CommentWordRepository wordRepository = RepositoryFactory.getCommentWordRepository();
		ClassMessageRepository classRepository = RepositoryFactory.getClassMessageRepository();

		List<Double> vector = new ArrayList<Double>();

		ClassMessage clazz = classRepository.findASingleByVersionIDAndClassID(comment.getVersionID(),
				comment.getClassID());
		List<Token> newTokenList = clazz.getNewTokenList();
		List<Token> oldTokenList = clazz.getOldTokenList();

		List<Token> commentNewTokenList = new ArrayList<Token>();
		List<Token> commentOldTokenList = new ArrayList<Token>();

		for (Token token : newTokenList) {
			if (token.getStartLine() >= comment.getNew_scope_startLine()
					&& token.getEndLine() <= comment.getNew_scope_endLine()) {
				commentNewTokenList.add(token);
			}
		}

		for (Token token : oldTokenList) {
			if (token.getStartLine() >= comment.getOld_scope_startLine()
					&& token.getEndLine() <= comment.getOld_scope_endLine()) {
				commentOldTokenList.add(token);
			}
		}

		int insertStatement = 0;
		int deleteStatement = 0;
		int updateStatement = 0;
		int parentChange = 0;
		int orderingChange = 0;
		int conditionChange = 0;
		int insertAlternative = 0;
		int deleteAlternative = 0;

		int diffSize = 0;
		int changeLine = 0;
		int newAllLine = comment.getNew_scope_endLine() - comment.getNew_comment_endLine();
		int oldAllLine = comment.getOld_scope_endLine() - comment.getOld_comment_endLine();
		if (newAllLine <= 0) {
			newAllLine = 1;
		}
		if (oldAllLine <= 0) {
			oldAllLine = 1;
		}

		int firstDiffLocation = 0;

		for (DiffType diff : comment.getDiffList()) {

			if (diff.getType().equals("STATEMENT_INSERT")) {
				insertStatement++;
				changeLine += diff.getNewEndLine() - diff.getNewStartLine() + 1;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getNewStartLine() - comment.getNew_comment_endLine();
				}
				continue;
			}
			if (diff.getType().equals("STATEMENT_DELETE")) {
				deleteStatement++;
				changeLine += diff.getOldEndLine() - diff.getOldStartLine() + 1;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getOldStartLine() - comment.getOld_comment_endLine();
				}
				continue;
			}
			if (diff.getType().equals("STATEMENT_UPDATE")) {
				updateStatement++;
				changeLine += diff.getNewEndLine() - diff.getNewStartLine() + 1;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getNewStartLine() - comment.getNew_comment_endLine();
				}
				continue;
			}
			if (diff.getType().equals("STATEMENT_PARENT_CHANGE")) {
				parentChange++;
				continue;
			}
			if (diff.getType().equals("STATEMENT_ORDERING_CHANGE")) {
				orderingChange++;
				continue;
			}
			if (diff.getType().equals("CONDITION_EXPRESSION_CHANGE")) {
				conditionChange++;
				changeLine++;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getNewStartLine() - comment.getNew_comment_endLine();
				}
				continue;
			}
			if (diff.getType().equals("ALTERNATIVE_PART_INSERT")) {
				insertAlternative++;
				changeLine += diff.getNewEndLine() - diff.getNewStartLine() + 1;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getNewStartLine() - comment.getNew_comment_endLine();
				}
				continue;
			}
			if (diff.getType().equals("ALTERNATIVE_PART_DELETE")) {
				deleteAlternative++;
				changeLine += diff.getOldEndLine() - diff.getOldStartLine() + 1;
				if (firstDiffLocation == 0) {
					firstDiffLocation = diff.getOldStartLine() - comment.getOld_comment_endLine();
				}
				continue;
			}
		}
		diffSize = insertStatement + deleteStatement + updateStatement + conditionChange + insertAlternative
				+ deleteAlternative;

		// 1. diffSize
		vector.add((double) discridizeNum(diffSize));

		// 2. insertStatement
		vector.add((double) discridizeNum(insertStatement));

		// 3. insertStatementWeight
		vector.add((double) discridizeWeight(insertStatement * 100 / (diffSize + 1)));

		// 4. deleteStatement
		vector.add((double) discridizeNum(deleteStatement));

		// 5. deleteStatementWeight
		vector.add((double) discridizeWeight(deleteStatement * 100 / (diffSize + 1)));

		// 6. updateStatement
		vector.add((double) discridizeNum(updateStatement));

		// 7. updateStatementWeight
		vector.add((double) discridizeWeight(updateStatement * 100 / (diffSize + 1)));

		// 8. parentChange
		vector.add((double) discridizeNum(parentChange));

		// 9. parentChangeWeight
		vector.add((double) discridizeWeight(parentChange * 100 / (diffSize + 1)));

		// 10. orderingChange
		vector.add((double) discridizeNum(orderingChange));

		// 11. orderingChangeWeight
		vector.add((double) discridizeWeight(orderingChange * 100 / (diffSize + 1)));

		// 12. conditionChange
		vector.add((double) discridizeNum(conditionChange));

		// 13. conditionChangeWeight
		vector.add((double) discridizeWeight(conditionChange * 100 / (diffSize + 1)));

		// 14. insertAlternative
		vector.add((double) discridizeNum(insertAlternative));

		// 15. insertAlternativeWeight
		vector.add((double) discridizeWeight(insertAlternative * 100 / (diffSize + 1)));

		// 16. deleteAlternative
		vector.add((double) discridizeNum(deleteAlternative));

		// 17. deleteAlternativeWeight
		vector.add((double) discridizeWeight(deleteAlternative * 100 / (diffSize + 1)));

		// 18. newAllLine
		vector.add((double) discridizeNum(newAllLine));

		// 19. oldAllLine
		vector.add((double) discridizeNum(oldAllLine));

		// 20. subLine
		vector.add((double) discridizeNum(Math.abs(newAllLine - oldAllLine)));

		// 21. changeLine
		vector.add((double) discridizeNum(changeLine));

		// 22. changeLineWeight
		vector.add((double) discridizeWeight(changeLine * 100 / newAllLine));

		// 23. firstDiffLocation
		vector.add((double) discridizeNum(firstDiffLocation));

		// 24. tokenChange
		boolean tokenChange = true;
		if (commentNewTokenList.size() != commentOldTokenList.size()) {
			tokenChange = false;
		} else {
			for (int i = 0, n = commentNewTokenList.size(); i < n; i++) {
				if (!commentNewTokenList.get(i).getTokenName().equals(commentOldTokenList.get(i).getTokenName())) {
					tokenChange = false;
					break;
				}
			}
		}
		vector.add((double) (tokenChange ? 1 : 0));

		CommentWord commentWord = wordRepository.findASingleByCommentID(comment.getCommentID());

		List<String> oldCommentWordList = commentWord.getOldCommentWords();

		// 25. wordSize
		vector.add((double) discridizeNum(oldCommentWordList.size()));

		StringBuilder sb = new StringBuilder();
		for (String str : comment.getOldComment()) {
			sb.append(str + " ");
		}
		for (String str : comment.getOldCode()) {
			sb.append(str + " ");
		}

		int codeSimilarity = (int) (commentWord.getCodeSimilarity() * 100);
		int oldCodeOldCommentSimilarity = (int) (commentWord.getOldCodeOldCommentSimilarity() * 100);
		int newCodeOldCommentSimilarity = (int) (commentWord.getNewCodeOldCommentSimilarity() * 100);
		int addWordNumber = commentWord.getAddWords().size();
		int deleteWordNumber = commentWord.getDeleteWords().size();

		// 26
		vector.add((double) discridizeWeight(codeSimilarity));
		// 27
		vector.add((double) discridizeWeight(oldCodeOldCommentSimilarity));
		// 28
		vector.add((double) discridizeWeight(newCodeOldCommentSimilarity));
		// 29
		vector.add((double) Math.abs(oldCodeOldCommentSimilarity - newCodeOldCommentSimilarity));
		// 30
		vector.add((double) discridizeNum(addWordNumber));
		// 31
		vector.add((double) discridizeNum(deleteWordNumber));
		// 32
		vector.add(0.0d);

		return vector;
	}

	private static int discridizeNum(int num) {
		if (num == 0) {
			return 0;
		}
		if (num == 1) {
			return 1;
		}
		if (num == 2) {
			return 2;
		}
		if (num == 3) {
			return 3;
		}
		if (num == 4) {
			return 4;
		}
		if (num == 5) {
			return 5;
		}
		if (num > 5 && num <= 10) {
			return 6;
		}
		if (num > 10 && num <= 20) {
			return 7;
		}
		if (num > 20) {
			return 8;
		}

		return 0;

	}

	private static int discridizeWeight(int num) {
		if (num == 0) {
			return 0;
		}
		if (num > 0 && num <= 10) {
			return 1;
		}
		if (num > 10 && num <= 20) {
			return 2;
		}
		if (num > 20 && num <= 30) {
			return 3;
		}
		if (num > 30 && num <= 40) {
			return 4;
		}
		if (num > 40 && num <= 50) {
			return 5;
		}
		if (num > 50 && num <= 60) {
			return 6;
		}
		if (num > 60 && num <= 70) {
			return 7;
		}
		if (num > 70 && num <= 80) {
			return 8;
		}
		if (num > 80 && num <= 90) {
			return 9;
		}
		if (num > 90 && num < 100) {
			return 10;
		}
		if (num == 100) {
			return 11;
		}
		return 0;
	}

	public static void main(String[] args) throws IOException {

	}

}
