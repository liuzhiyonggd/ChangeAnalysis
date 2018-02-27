package sysu.commconsistency.util;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import sysu.commconsistency.bean.TokenType;

public class Parser2 {

	/* Convert files that are in String format to AST */
    public static Tokenizer2 parseAST2Tokens(final CompilationUnit unit) {
        
            final AST ast = unit.getAST();

            // Process the main body
            final Tokenizer2 tk = new Tokenizer2();
            try {
                unit.accept(new ASTVisitor() {

                    public boolean	visit(AnonymousClassDeclaration node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        tk.addToken("AnonymousClassDeclaration",null,startLine,endLine,TokenType.AnonymousClassDeclaration.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(ArrayAccess node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        tk.addToken("ArrayAccess",null,startLine,endLine,TokenType.ArrayAccess.ordinal());

                        return true;
                    }

                    public boolean	visit(ArrayCreation node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        tk.addToken("ArrayCreation",null,startLine,endLine,TokenType.ArrayCreation.ordinal());
                        return true;
                    }

                    public boolean	visit(ArrayInitializer node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        tk.addToken("ArrayInitializer",null,startLine,endLine,TokenType.ArrayInitializer.ordinal());
                        return true;
                    }

                    public boolean	visit(ArrayType node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        ITypeBinding binding = node.getElementType().resolveBinding();
                        if (binding != null) {
                            tk.addToken("ArrayType<"+binding.getQualifiedName()+">",binding.getQualifiedName(),startLine,endLine,TokenType.ArrayType.ordinal());
                        }
                        else {
                            tk.addToken("ArrayType",null,startLine,endLine,TokenType.ArrayType.ordinal());
                        }

                        return true;
                    }

                    public boolean	visit(AssertStatement node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("AssertStatement",null,startLine,endLine,TokenType.AssertStatement.ordinal());
                        return true;
                    }

                    public boolean	visit(Assignment node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        String operator = node.getOperator().toString();
                        tk.addToken("Assignment",operator, startLine,endLine,TokenType.Assignment.ordinal());
                        return true;
                    }
                    
                    public boolean	visit(Block node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        tk.addToken("Block", null,startLine,endLine, TokenType.Block.ordinal());
                        return true;
                    }

                    public boolean	visit(BooleanLiteral node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition()+node.getLength()-1);
                        if (node.booleanValue()) {
                            tk.addToken("BooleanLiteral","true",startLine,endLine,TokenType.BooleanLiteral.ordinal());
                        } else {
                            tk.addToken("BooleanLiteral","false",startLine,endLine,TokenType.BooleanLiteral.ordinal());
                        }
                        return true;
                    }

                    public boolean	visit(BreakStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("BreakStatement",null,startLine,endLine,TokenType.BreakStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(CastExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("CastExpression",null,startLine,endLine,TokenType.CastExpression.ordinal()); //reminder
                        return true;
                    }

                    public boolean	visit(CatchClause node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("CatchClause",null,startLine,endLine,TokenType.CatchClause.ordinal());
                        return true;
                    }

                    public boolean	visit(CharacterLiteral node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("CharacterLiteral",node.getEscapedValue(),startLine,endLine,TokenType.CharacterLiteral.ordinal());
                        return true;
                    }

                    public boolean	visit(ClassInstanceCreation node) {


                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("ClassInstanceCreation",null,startLine,endLine,TokenType.ClassInstanceCreation.ordinal());
                        return true;
                    }

                    public boolean	visit(ConditionalExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("ConditionalExpression",null,startLine,endLine,TokenType.ConditionalExpression.ordinal());
                        return true;
                    }

                    public boolean	visit(ConstructorInvocation node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("SuperConstructorInvocation",null,startLine,endLine,TokenType.SuperConstructorInvocation.ordinal());
                        return true;
                    }

                    public boolean	visit(ContinueStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("ContinueStatement","null",startLine,endLine,TokenType.ContinueStatement.ordinal());
                        return true;
                    }

                    public boolean	visit(DoStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());	
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("DoStatement",null,startLine,endLine,TokenType.DoStatement.ordinal());

                        return true;
                    }



                    public boolean	visit(EnhancedForStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("EnhancedForStatement",null,startLine,endLine,TokenType.EnhancedForStatement.ordinal());
                        return true;
                    }


                    public boolean	visit(ExpressionStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("ExpressionStatement",null, startLine, endLine, TokenType.ExpressionStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(FieldAccess node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        String fieldName = node.getName().toString();
                        tk.addToken("FieldAccess",fieldName,startLine,endLine,TokenType.FieldAccess.ordinal());
                        return true;
                    }
                    /*
                     * always together with VariableDeclarationFragment
                     */
                    public boolean	visit(FieldDeclaration node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        Type type = node.getType();
                        tk.addToken("FieldDeclaration", type.toString(), startLine, endLine, TokenType.FieldDeclaration.ordinal());
                        return true;
                    }

                    public boolean	visit(ForStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("ForStatement",null,startLine,endLine,TokenType.ForStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(IfStatement node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        ASTNode parent = node.getParent();
                        boolean if_else_happen = false;
                        if (parent != null) {
                            if (parent.getNodeType() == 25) {
                                Statement else_statement = ((IfStatement) parent).getElseStatement();
                                if (else_statement != null) {
                                    if (else_statement.equals(node)) {
                                        if_else_happen = true;
                                    }
                                }
                            }
                        }

                        if (if_else_happen) {
                            tk.addToken("Elseif",null,startLine,endLine,TokenType.Elseif.ordinal());
                        } else {
                            tk.addToken("IfStatement",null,startLine,endLine,TokenType.IfStatement.ordinal());
                        }

                        return true;
                    }
                    
                    
                    
                    public boolean	visit(InfixExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        String operator = node.getOperator().toString();
                        tk.addToken("InfixExpression",operator,startLine,endLine,TokenType.InfixExpression.ordinal());
                        return true;
                    }


                    public boolean	visit(InstanceofExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("InstanceofExpression",null,startLine,endLine,TokenType.InstanceofExpression.ordinal());
                        return true;
                    }

                    public boolean	visit(LabeledStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("LabeledStatement",null,startLine,endLine,TokenType.LabeledStatement.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(MemberRef node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("MemberRef",node.resolveBinding().getName(),startLine,endLine,TokenType.MemberRef.ordinal());

                        return true;
                    }

                    public boolean	visit(MemberValuePair node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("MemberValuePair",null,startLine,endLine,TokenType.MemberValuePair.ordinal());

                        return true;
                    }

                    public boolean	visit(MethodDeclaration node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("MethodDeclaration",null,startLine,endLine,TokenType.MethodDeclaration.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(MethodInvocation node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("MethodInvocation",null,startLine,endLine,TokenType.MethodInvocation.ordinal());
                        return true;
                    }


                    public boolean	visit(NullLiteral node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("NullLiteral",null,startLine,endLine,TokenType.NullLiteral.ordinal());
                        return true;
                    }

                    public boolean	visit(NumberLiteral node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("NumberLiteral",null,startLine,endLine,TokenType.NumberLiteral.ordinal());

                        return true;
                    }

                    public boolean visit(ImportDeclaration node) {
                        return false;
                    }

                    public boolean	visit(PackageDeclaration node) {
                        return false;
                    }

                    public boolean	visit(ParenthesizedExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());	
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("ParenthesizedExpression",null,startLine,endLine,TokenType.ParenthesizedExpression.ordinal());
                        return true;
                    } 

                    public boolean	visit(PostfixExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("PostfixExpression",node.getOperator().toString(),startLine,endLine,TokenType.PostfixExpression.ordinal());


                        return true;
                    }

                    public boolean	visit(PrefixExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        String operator = node.getOperator().toString();
                        tk.addToken("InfixOperator",operator,startLine,endLine,TokenType.InfixOperator.ordinal());
                        ITypeBinding binding = node.getOperand().resolveTypeBinding();

                        if (binding != null) {
                            tk.addToken("PrefixExpression",binding.getQualifiedName(),startLine,endLine,TokenType.PrefixExpression.ordinal());
                        }
                        else {
                            tk.addToken("PrefixExpression",null,startLine,endLine,TokenType.PrefixExpression.ordinal());
                        }
                        return true;
                    }

                    public boolean	visit(QualifiedName node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("QualifiedName",node.toString(),startLine,endLine,TokenType.QualifiedName.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(ReturnStatement node) {
                        return false;
                    }

                    public boolean visit(SimpleName node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("SimpleName",node.toString(),startLine,endLine,TokenType.SimpleName.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(SingleMemberAnnotation node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("SingleMemberAnnotation",node.getTypeName().toString(),startLine,endLine,TokenType.SingleMemberAnnotation.ordinal());				
                        
                        return true;
                    }

                    public boolean	visit(SingleVariableDeclaration node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        IVariableBinding binding = node.resolveBinding();
                        if (binding != null) {
                            String variableType = binding.getType().getName();
                            tk.addToken("SingleVariableDeclaration",variableType,startLine,endLine,TokenType.SingleVariableDeclaration.ordinal());

                        } else {
                            tk.addToken("SingleVariableDeclaration",null,startLine,endLine,TokenType.SingleVariableDeclaration.ordinal());
                        }
                        return true;
                    }

                    public boolean	visit(StringLiteral node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        String strValue = node.getEscapedValue();
                        strValue = strValue.substring(1,strValue.length() - 1);
                        tk.addToken("StringLiteral",strValue,startLine,endLine,TokenType.StringLiteral.ordinal());

                        return true;
                    }

                    public boolean	visit(SuperConstructorInvocation node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("SuperConstructorInvocation",null,startLine,endLine,TokenType.SuperConstructorInvocation.ordinal());

                        return true;
                    }

                    public boolean	visit(SuperFieldAccess node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("SuperFieldAccess",null,startLine,endLine,TokenType.SuperFieldAccess.ordinal());
                        return true;
                    }

                    public boolean	visit(SuperMethodInvocation node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("SuperMethodInvocation",node.getName().toString(),startLine,endLine,TokenType.SuperMethodInvocation.ordinal());

                        return true;
                    }


                    public boolean	visit(SwitchStatement node) {
                        return false;
                    }

                    public boolean	visit(SynchronizedStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("SynchronizedStatement",null,startLine,endLine,TokenType.SynchronizedStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(TagElement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("TagElement",null,startLine,endLine,TokenType.TagElement.ordinal());
                        return true;
                    }

                    public boolean	visit(TextElement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("TextElement",null,startLine,endLine,TokenType.TextElement.ordinal());

                        return true;
                    }

                    public boolean	visit(ThisExpression node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        tk.addToken("ThisExpression",null,startLine,endLine,TokenType.ThisExpression.ordinal());
                        return true;
                    }

                    public boolean	visit(ThrowStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("ThrowStatement",null,startLine,endLine,TokenType.ThrowStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(TryStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("TryStatement",null,startLine,endLine,TokenType.TryStatement.ordinal());

                        return true;
                    }
                    


                    public boolean visit(VariableDeclarationExpression node) {
                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        Type type = node.getType();
                        tk.addToken("VariableDeclarationExpression",type.toString(),startLine,endLine,TokenType.VariableDeclarationExpression.ordinal());

                        return true;
                    }

                    public boolean visit(VariableDeclarationFragment node) {			   

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("VariableDeclarationFragment",null,startLine,endLine,TokenType.VariableDeclarationFragment.ordinal());

                        return true;
                    }
                    
                    public boolean	visit(VariableDeclarationStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());

                        String message = "";
                        List<VariableDeclarationFragment> fragments = node.fragments();
                        for (VariableDeclarationFragment fragment : fragments) {
                            String varName = fragment.getName().toString();
                            message = message+","+varName;
                        }
                        tk.addToken("VariableDeclarationStatement",message,startLine,endLine,TokenType.VariableDeclarationStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(WhileStatement node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("WhileStatement",null,startLine,endLine,TokenType.WhileStatement.ordinal());

                        return true;
                    }

                    public boolean	visit(WildcardType node) {

                        int startLine = unit.getLineNumber(node.getStartPosition());
                        int endLine = unit.getLineNumber(node.getStartPosition() + node.getLength());
                        tk.addToken("WildcardType",null,startLine,endLine,TokenType.WildcardType.ordinal());
                        return true;
                    }
                    
                });
            } catch (Exception e) {
                System.out.println("Problem : " + e.toString());
                e.printStackTrace();
                System.exit(0);
            }
            return tk;

     
    }
}
