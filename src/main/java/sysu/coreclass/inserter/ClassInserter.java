package sysu.coreclass.inserter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import sysu.coreclass.bean.ClassBean;
import sysu.coreclass.bean.MethodBean;
import sysu.coreclass.bean.MethodInvoke;
import sysu.coreclass.bean.StatementBean;
import sysu.coreclass.relation.ClassChange;
import sysu.coreclass.relation.InvokeAnalysis;
import sysu.coreclass.relation.VersionChange;
import sysu.coreclass.relation.VersionComparator;
import sysu.database.repository.ClassRepository;
import sysu.database.repository.MethodRepository;
import sysu.database.repository.RepositoryFactory;


public class ClassInserter {

	public static int insert(String oldFileDir, String newFileDir, String userName,int versionID) {
		
		ClassRepository classRepo = RepositoryFactory.getClassRepository();
		MethodRepository methodRepo = RepositoryFactory.getMethodRepository();
		VersionComparator comp = new VersionComparator();
		comp.compareProject(oldFileDir, newFileDir);
		VersionChange change = comp.getChange();
		change.compareChangType4Direct60(newFileDir+"/");

		List<ClassChange> addClassList = change.getAddedClassList();
		
		
		List<ClassBean> classBeanList = new ArrayList<ClassBean>();
		List<MethodBean> methodBeanList = new ArrayList<MethodBean>();
		for (int i = 0; i < addClassList.size(); i++) {
			List<MethodInvoke> methodInvokeList = InvokeAnalysis.findInvoke(addClassList.get(i),
					change.getAcClassList(), "new", change.getAddedClassList().size());
			
			List<Integer> addStatementNodeTypeList = new ArrayList<Integer>();
			List<Integer> changeStatementNodeTypeList = new ArrayList<Integer>();
			List<Integer> deleteStatementNodeTypeList = new ArrayList<Integer>();
			
			ClassBean classBean = new ClassBean();
			classBean.setClassID(i+1);
			classBean.setVersionID(versionID);
			classBean.setUserName(userName);
			classBean.setClassName(addClassList.get(i).getNewClass().getName().getFullyQualifiedName());

			classBean.setCodes(addClassList.get(i).getCodes());
			classBean.setNewCodeLines(addClassList.get(i).getNewCodeLines());
			classBean.setOldCodeLines(addClassList.get(i).getOldCodeLines());
			classBean.setNewMethodNum(addClassList.get(i).getNewMethodNum());
			classBean.setOldMethodNum(addClassList.get(i).getOldMethodNum());
			classBean.setAddMethodNum(addClassList.get(i).getNewClass().getMethods().length);
			if (addClassList.get(i).getNewClass().getSuperclassType() != null) {
				classBean.setParentName(addClassList.get(i).getNewClass().getSuperclassType().toString());
			} else {
				classBean.setParentName("Object");
			}
			classBean.setClassType("new");
			List<String> interfaces = new ArrayList<String>();
			for (int j = 0; j < addClassList.get(i).getNewClass().superInterfaceTypes().size(); j++) {
				interfaces.add(addClassList.get(i).getNewClass().superInterfaceTypes().get(j).toString());
			}
			classBean.setInterfaceList(interfaces);
			classBean.setOutterCount(
					computeOuterCount(classBean, addClassList.get(i).getNewClass(), change.getAcClassList()));
			classBean.setMethodInvokeList(methodInvokeList);
			
			
			for (int j = 0; j < addClassList.get(i).getNewClass().getMethods().length; j++) {
				MethodDeclaration method = addClassList.get(i).getNewClass().getMethods()[j];
				MethodBean methodBean = new MethodBean();
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setVersionID(versionID);
				methodBean.setMethodID(j);
				methodBean.setMethodType("new");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				
				List<StatementBean> statementList = new ArrayList<StatementBean>();
				if(method.getBody()!=null){
					for(int l=0,n=method.getBody().statements().size();l<n;l++){
						StatementBean statementBean = new StatementBean();
						Statement sta = (Statement)method.getBody().statements().get(l);
						statementBean.setStatement(sta.toString());
						statementBean.setStatementeType("add");
						statementBean.setStatementID(l);
						statementBean.setNodeType(sta.getNodeType());
						statementList.add(statementBean);
						addStatementNodeTypeList.add(statementBean.getNodeType());
					}
				}
				methodBean.setStatementList(statementList);
				methodBeanList.add(methodBean);
			}
			
			classBean.setAddStatementNodeTypeList(addStatementNodeTypeList);
			classBean.setChangeStatementNodeTypeList(changeStatementNodeTypeList);
			classBean.setDeleteStatementNodeTypeList(deleteStatementNodeTypeList);
			classBeanList.add(classBean);
		}

		List<ClassChange> changeClassList = change.getChangedClassList();
		for (int i = 0; i < changeClassList.size(); i++) {
			List<MethodInvoke> methodInvokeList = InvokeAnalysis.findInvoke(changeClassList.get(i),
					change.getAcClassList(), "new", change.getAddedClassList().size());
			
			List<Integer> addStatementNodeTypeList = new ArrayList<Integer>();
			List<Integer> changeStatementNodeTypeList = new ArrayList<Integer>();
			List<Integer> deleteStatementNodeTypeList = new ArrayList<Integer>();

			ClassBean classBean = new ClassBean();
			classBean.setClassID(addClassList.size() + i);
			classBean.setVersionID(versionID);
			classBean.setUserName(userName);
			classBean.setClassName(changeClassList.get(i).getNewClass().getName().getFullyQualifiedName());

			classBean.setNewCodeLines(changeClassList.get(i).getNewCodeLines());
			classBean.setOldCodeLines(changeClassList.get(i).getOldCodeLines());
			classBean.setNewMethodNum(changeClassList.get(i).getNewMethodNum());
			classBean.setOldMethodNum(changeClassList.get(i).getOldMethodNum());
			classBean.setCodes(changeClassList.get(i).getCodes());
			classBean.setChangeMethodNum(changeClassList.get(i).getChangedMethods().size());
			classBean.setAddMethodNum(changeClassList.get(i).getAddedMethods().size());
			classBean.setDeleteMethodNum(changeClassList.get(i).getDeletedMethods().size());
			if (changeClassList.get(i).getNewClass().getSuperclassType() != null) {
				classBean.setParentName(changeClassList.get(i).getNewClass().getSuperclassType().toString());
			} else {
				classBean.setParentName("Object");
			}
			classBean.setClassType("change");
			List<String> interfaces = new ArrayList<String>();
			for (int j = 0; j < changeClassList.get(i).getNewClass().superInterfaceTypes().size(); j++) {
				interfaces.add(changeClassList.get(i).getNewClass().superInterfaceTypes().get(j).toString());
			}
			classBean.setInterfaceList(interfaces);
			classBean.setMethodInvokeList(methodInvokeList);
			classBean.setOutterCount(
					computeOuterCount(classBean, changeClassList.get(i).getNewClass(), change.getAcClassList()));
			
			
			for (int j = 0; j < changeClassList.get(i).getAddedMethods().size(); j++) {
				MethodDeclaration method = changeClassList.get(i).getAddedMethods().get(j);
				MethodBean methodBean = new MethodBean();
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setVersionID(versionID);
				methodBean.setMethodID(j);
				methodBean.setMethodType("new");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				
				List<StatementBean> statementList = new ArrayList<StatementBean>();
				if(method.getBody()!=null){
					for(int l=0,n=method.getBody().statements().size();l<n;l++){
						StatementBean statementBean = new StatementBean();
						Statement sta = (Statement)method.getBody().statements().get(l);
						statementBean.setStatement(sta.toString());
						statementBean.setStatementeType("add");
						statementBean.setStatementID(l);
						statementBean.setNodeType(sta.getNodeType());
						addStatementNodeTypeList.add(sta.getNodeType());
						statementList.add(statementBean);
					}
				}
				methodBean.setStatementList(statementList);
				methodBeanList.add(methodBean);
			}
			for (int j = 0; j < changeClassList.get(i).getChangedMethods().size(); j++) {
				MethodDeclaration method = changeClassList.get(i).getChangedMethods().get(j).getNewMethod();
				MethodBean methodBean = new MethodBean();
				methodBean.setClassID(classBean.getClassID());
				methodBean.setVersionID(versionID);
				methodBean.setClassName(classBean.getClassName());
				methodBean.setMethodID(changeClassList.get(i).getAddedMethods().size() + j);
				methodBean.setMethodType("change");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				methodBean.setStatementList(changeClassList.get(i).getChangedMethods().get(j).getStatementList());
				
				for(StatementBean statement:methodBean.getStatementList()) {
					if(statement.getStatementeType().equals("change")) {
						changeStatementNodeTypeList.add(statement.getNodeType());
					}else if(statement.getStatementeType().equals("delete")){
						deleteStatementNodeTypeList.add(statement.getNodeType());
					}else {
						addStatementNodeTypeList.add(statement.getNodeType());
					}
				}
				
				methodBeanList.add(methodBean);
			}
			for(int j = 0; j< changeClassList.get(i).getDeletedMethods().size();j++) {
				MethodDeclaration method = changeClassList.get(i).getDeletedMethods().get(j);
				MethodBean methodBean = new MethodBean();
				methodBean.setClassID(classBean.getClassID());
				methodBean.setClassName(classBean.getClassName());
				methodBean.setMethodID(j);
				methodBean.setVersionID(versionID);
				methodBean.setMethodType("delete");
				methodBean.setMethodName(method.getName().getFullyQualifiedName());

				if (method.getReturnType2() != null) {
					methodBean.setReturnType(method.getReturnType2().toString());
				} else {
					methodBean.setReturnType("null");
				}

				List<String> methodParameters = new ArrayList<String>();
				for (int k = 0; k < method.parameters().size(); k++) {
					methodParameters.add(method.parameters().get(k).toString());
				}
				methodBean.setParameters(methodParameters);
				methodBean.setOutterCount(computeMethodOuterCount(methodBean.getMethodName(), methodInvokeList));
				
				List<StatementBean> statementList = new ArrayList<StatementBean>();
				if(method.getBody()!=null){
					for(int l=0,n=method.getBody().statements().size();l<n;l++){
						StatementBean statementBean = new StatementBean();
						Statement sta = (Statement)method.getBody().statements().get(l);
						statementBean.setStatement(sta.toString());
						statementBean.setStatementeType("delete");
						statementBean.setStatementID(l);
						statementBean.setNodeType(sta.getNodeType());
						statementList.add(statementBean);
						
						deleteStatementNodeTypeList.add(sta.getNodeType());
					}
				}
				methodBean.setStatementList(statementList);
				methodBeanList.add(methodBean);
			}
			classBean.setAddStatementNodeTypeList(addStatementNodeTypeList);
			classBean.setChangeStatementNodeTypeList(changeStatementNodeTypeList);;
			classBean.setDeleteStatementNodeTypeList(deleteStatementNodeTypeList);
			classBeanList.add(classBean);
		}
		

		computeInnerCount(classBeanList);
		classRepo.insert(classBeanList);
		methodRepo.insert(methodBeanList);
		
		return versionID;
		
	}


	private static int computeOuterCount(ClassBean classBean, TypeDeclaration class1, List<ClassChange> class2List) {

		int outerCount = 0;
		for (ClassChange cc : class2List) {
			for (MethodDeclaration md : class1.getMethods()) {
				if (md.getBody() != null
						&& md.getBody().toString().contains(cc.getNewClass().getName().getFullyQualifiedName())) {
					classBean.getUseClassList().add(cc.getNewClass().getName().getFullyQualifiedName());
					outerCount++;
					break;
				}
			}
		}
		return outerCount;
	}

	private static void computeInnerCount(List<ClassBean> classBeanList) {

		for (ClassBean classBean1 : classBeanList) {
			int innerCount = 0;
			String classBean1Name = classBean1.getClassName();
			for (ClassBean classBean2 : classBeanList) {
				if (classBean2.getUseClassList().contains(classBean1Name)) {
					innerCount++;
				}
			}
			classBean1.setInnerCount(innerCount);
		}
	}
	
	public static void main(String[] args) {
		List<Integer> idList = new ArrayList<Integer>();
		test(idList);
		System.out.println(idList.size()+"");
	}
	
	private static void test(List<Integer> idList) {
		idList.add(1);
		idList.add(2);
	}
	
	private static int computeMethodOuterCount(String methodName,List<MethodInvoke> methodInvokeList){
		int result = 0;
		for(MethodInvoke methodInvoke:methodInvokeList){
			if(methodInvoke.getMethod1().equals(methodName)){
				result++;
			}
		}
		return result;
	}
	
	private static void computeMethodInnerCount(List<MethodBean> methodBeanList,List<ClassBean> classBeanList){
		for(MethodBean method:methodBeanList){
			int innerCount = 0;
			for(ClassBean clazz:classBeanList){
				for(MethodInvoke methodInvoke:clazz.getMethodInvokeList()){
					if(methodInvoke.getClass2().equals(method.getClassName())&&methodInvoke.getMethod2().equals(method.getMethodName())){
						innerCount++;
					}
				}
			}
			method.setInnerCount(innerCount);
		}
	}
}
				
