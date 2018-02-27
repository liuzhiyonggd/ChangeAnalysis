package sysu.commconsistency.util;

import java.util.ArrayList;
import java.util.List;

import sysu.commconsistency.bean.Token;

public class Tokenizer2 {
	
	private List<Token> tokenList = new ArrayList<Token>();

	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public void addToken(String tokenName,String keyword,int startLine,int endLine,int nodeType){
		Token token = new Token();
		token.setTokenName(tokenName);
		token.setKeyword(keyword);
		token.setStartLine(startLine);
		token.setEndLine(endLine);
		
		final int prime = 31;
        int result = 1;
        result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
        result = prime * result + nodeType;
		token.setHashNumber(result);
		
		tokenList.add(token);
		
	}

}
