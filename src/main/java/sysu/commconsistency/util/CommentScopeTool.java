package sysu.commconsistency.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import sysu.commconsistency.bean.CodeComment;




public class CommentScopeTool {

	public static int computeCommentScope(CodeComment comment, int commentIndex, List<CodeComment> commentList,
			List<Statement> statements, CompilationUnit unit, int methodEndLine) {

		int scopeEndLine = methodEndLine;
		int currentCommentStartLine = comment.getStartLine();
		int currentCommentEndLine = comment.getEndLine();
		int[] levels = computeCommentLevels(statements, commentList, unit);
		int currentLevel = levels[commentIndex];
		
		boolean isCommentCodeSameLine = false;
		Queue<Statement> queue = new LinkedList<Statement>();
		List<Statement> staList = new ArrayList<Statement>();
		staList.addAll(statements);
		queue.addAll(staList);

		while (!queue.isEmpty()) {
			Statement sta = queue.poll();
			List<Statement> tempList = getBlockStatements(sta);
			if (tempList.size() > 0) {
				staList.addAll(tempList);
				queue.addAll(tempList);
			}
		}
		for (Statement sta : staList) {
			int staStartLine = unit.getLineNumber(sta.getStartPosition());
			if (currentCommentStartLine == staStartLine) {
				return currentCommentStartLine;
			}
		}

		if (!isCommentCodeSameLine) {
			CodeComment nextComment = null;

			for (int i = 0, n = commentList.size(); i < n; i++) {
				if (commentList.get(i).getStartLine() > comment.getStartLine() && levels[i] == currentLevel) {
					nextComment = commentList.get(i);
					break;
				}
			}

			int nextCommentStartLine = Integer.MAX_VALUE;
			if (nextComment != null) {
				nextCommentStartLine = nextComment.getStartLine();
			}
			if (methodEndLine < nextCommentStartLine) {
				scopeEndLine = methodEndLine;
			} else {
				scopeEndLine = nextCommentStartLine - 1;
			}

			for (Statement statement : statements) {
				int statementStartLine = unit.getLineNumber(statement.getStartPosition());
				int statementEndLine = unit.getLineNumber(statement.getStartPosition() + statement.getLength() - 1);
				if (statementStartLine < currentCommentStartLine && statementEndLine > currentCommentEndLine) {
					List<Statement> statementList = getBlockStatements(statement);
					Statement containCommentStatement = findStatementContainComment(statementList, unit,
							currentCommentStartLine, currentCommentEndLine);
					while (containCommentStatement != null) {
						int containCommentStatementEndLine = unit.getLineNumber(
								containCommentStatement.getStartPosition() + containCommentStatement.getLength() - 1);
						if (containCommentStatementEndLine < scopeEndLine) {
							scopeEndLine = containCommentStatementEndLine;
						}
						statementList = getBlockStatements(containCommentStatement);
						containCommentStatement = findStatementContainComment(statementList, unit,
								currentCommentStartLine, currentCommentEndLine);
					}
				}
			}
		}
		return scopeEndLine;
	}

	public static Statement findStatementContainComment(List<Statement> statementList, CompilationUnit unit,
			int commentStartLine, int commentEndLine) {

		for (Statement statement : statementList) {
			int statementStartLine = unit.getLineNumber(statement.getStartPosition());
			int statementEndLine = unit.getLineNumber(statement.getStartPosition() + statement.getLength() - 1);
			if (statementStartLine < commentStartLine && statementEndLine > commentEndLine) {
				return statement;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Statement> getBlockStatements(Statement statement) {

		if (statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement) statement;
			Statement thenStatement = ifStatement.getThenStatement();
			List<Statement> statementList = new ArrayList<Statement>();
			statementList.add(thenStatement);
			if (thenStatement instanceof Block) {
				Block block = (Block) thenStatement;
				statementList.addAll(block.statements());
			}
			Statement elseStatement = ifStatement.getElseStatement();
			if(elseStatement!=null) {
				statementList.add(elseStatement);
			}
			if (elseStatement instanceof Block) {
				Block block = (Block) elseStatement;
				statementList.addAll(block.statements());
			}
			
			return statementList;
		}

		if (statement instanceof WhileStatement) {
			WhileStatement whileStatement = (WhileStatement) statement;
			Statement whileBody = whileStatement.getBody();
			List<Statement> statementList = new ArrayList<Statement>();
			statementList.add(whileBody);
			if (whileBody instanceof Block) {
				Block block = (Block) whileBody;
				statementList.addAll(block.statements());
			} 
			return statementList;
			
		}

		if (statement instanceof ForStatement) {
			ForStatement forStatement = (ForStatement) statement;
			Statement forBody = forStatement.getBody();
			List<Statement> statementList = new ArrayList<Statement>();
			statementList.add(forBody);
			if (forBody instanceof Block) {
				Block block = (Block) forBody;
				statementList.addAll(block.statements());
			} 
			return statementList;
		}

		if (statement instanceof EnhancedForStatement) {
			EnhancedForStatement forStatement = (EnhancedForStatement) statement;
			Statement forBody = forStatement.getBody();
			List<Statement> statementList = new ArrayList<Statement>();
			statementList.add(forBody);
			if (forBody instanceof Block) {
				Block block = (Block) forBody;
				statementList.addAll(block.statements());
			} 
			return statementList;
		}
		
		if(statement instanceof TryStatement) {
			TryStatement tryStatement = (TryStatement)statement;
			Statement tryBody = tryStatement.getBody();
			List<Statement> statementList = new ArrayList<Statement>();
			if(tryBody!=null) {
			    statementList.add(tryBody);
			}
			if(tryBody instanceof Block) {
				Block block = (Block)tryBody;
				statementList.addAll(block.statements());
			}
			List<CatchClause> catchClauses = tryStatement.catchClauses();
			if(catchClauses!=null&&catchClauses.size()>0) {
				for(CatchClause clause:catchClauses) {
					Statement sta = clause.getBody();
					if(sta!=null) {
						statementList.add(sta);
					}
					if(sta instanceof Block) {
						Block block = (Block)sta;
						statementList.addAll(block.statements());
					}
				}
			}
			
			Statement finallyBody = tryStatement.getFinally();
			if(finallyBody!=null) {
				statementList.add(finallyBody);
			}
			if(finallyBody instanceof Block) {
				Block block = (Block)finallyBody;
				statementList.addAll(block.statements());
			}
			return statementList;
		}
		return new ArrayList<Statement>();
	}

	public static int[] computeCommentLevels(List<Statement> statements, List<CodeComment> commentList,
			CompilationUnit unit) {
		int[] levels = new int[commentList.size()];
		for (int i = 0, n = commentList.size(); i < n; i++) {
			CodeComment comment = commentList.get(i);
			int commentLevel = 0;
			int currentCommentStartLine = comment.getStartLine();
			int currentCommentEndLine = comment.getEndLine();

			for (Statement statement : statements) {
				int statementStartLine = unit.getLineNumber(statement.getStartPosition());
				int statementEndLine = unit.getLineNumber(statement.getStartPosition() + statement.getLength() - 1);
				if (statementEndLine > currentCommentEndLine && statementStartLine < currentCommentStartLine) {
					List<Statement> statementList = getBlockStatements(statement);
					Statement containCommentStatement = findStatementContainComment(statementList, unit,
							currentCommentStartLine, currentCommentEndLine);
					while (containCommentStatement != null) {
						commentLevel++;
						statementList = getBlockStatements(containCommentStatement);
						containCommentStatement = findStatementContainComment(statementList, unit,
								currentCommentStartLine, currentCommentEndLine);
					}
				}
			}
			levels[i] = commentLevel;
		}
		return levels;
	}

}
