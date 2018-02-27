package sysu.coreclass.classify;

import java.util.ArrayList;
import java.util.List;



import sysu.coreclass.bean.ClassBean;
import sysu.coreclass.bean.MethodBean;
import sysu.database.repository.ClassRepository;
import sysu.database.repository.MethodRepository;
import sysu.database.repository.RepositoryFactory;

public class DataGenerator {
	public static List<List<Integer>> generate(int versionID) {
		ClassRepository classRepo = RepositoryFactory.getClassRepository();
		MethodRepository methodRepo = RepositoryFactory.getMethodRepository();
		List<ClassBean> classList = classRepo.findByVersionID(versionID);
		
		List<List<Integer>> vectorList = new ArrayList<List<Integer>>();

		int changeMethodsNum = 0;
		int newMethodsNum = 0;
		int deleteMethodsNum = 0;
		int statementsNum = 0;
		int newStatementsNum = 0;
		int changeStatementsNum = 0;
		int deleteStatementsNum = 0;

		int maxInnerCount = 0;
		int maxOuterCount = 0;
		int aveInnerCount = 0;
		int aveOuterCount = 0;

		int maxStatementNum = 0;
		int aveStatementNum = 0;

		for (ClassBean clazz : classList) {

			int classInnerCount = clazz.getInnerCount();
			int classOuterCount = clazz.getOutterCount();
			aveInnerCount += classInnerCount;
			aveOuterCount += classOuterCount;
			if (maxInnerCount < classInnerCount) {
				maxInnerCount = classInnerCount;
			}
			if (maxOuterCount < classOuterCount) {
				maxOuterCount = classOuterCount;
			}

			newMethodsNum += clazz.getNewMethodNum();
			changeMethodsNum += clazz.getChangeMethodNum();
			deleteMethodsNum += clazz.getDeleteMethodNum();

			int statementNum = clazz.getAddStatementNodeTypeList().size()
					+ clazz.getChangeStatementNodeTypeList().size() + clazz.getDeleteStatementNodeTypeList().size();
			int newStatementNum = clazz.getAddStatementNodeTypeList().size();
			int changeStatementNum = clazz.getChangeStatementNodeTypeList().size();
			int deleteStatementNum = clazz.getDeleteStatementNodeTypeList().size();

			newStatementsNum += newStatementNum;
			changeStatementsNum += changeStatementNum;
			deleteStatementsNum += deleteStatementNum;

			aveStatementNum += statementNum;
			if (statementNum > maxStatementNum) {
				maxStatementNum = statementNum;
			}
		}

		int methodsNum = changeMethodsNum + newMethodsNum + deleteMethodsNum;
		statementsNum = newStatementsNum + changeStatementsNum + deleteStatementsNum;
		for (ClassBean clazz:classList) {
			int classIndex = clazz.getClassIndex();
			List<Integer> vector = new ArrayList<Integer>();
			int classInnerCount = clazz.getInnerCount();
			int classOuterCount = clazz.getOutterCount();

			// 6.
			if (classInnerCount == 0) {
				vector.add(0);
			} else if (classInnerCount == 1) {
				vector.add(1);
			} else if (classInnerCount == 2) {
				vector.add(2);
			} else if (classInnerCount == 3) {
				vector.add(3);
			} else if (classInnerCount == 4) {
				vector.add(4);
			} else if (classInnerCount == 5) {
				vector.add(5);
			} else {
				vector.add(6);
			}

			// 7.
			if (classOuterCount == 0) {
				vector.add(0);
			} else if (classOuterCount == 1) {
				vector.add(1);
			} else if (classOuterCount == 2) {
				vector.add(2);
			} else if (classOuterCount == 3) {
				vector.add(3);
			} else if (classOuterCount == 4) {
				vector.add(4);
			} else if (classOuterCount == 5) {
				vector.add(5);
			} else {
				vector.add(6);
			}

			// 8:classInnerCount:maxInnerCount
			if (maxInnerCount == 0) {
				vector.add(0);
			} else {
				vector.add(classInnerCount * 5 / maxInnerCount);
			}

			// 9:classInnerCount:aveInnerCount
			int classInnerCount_aveInnerCount = 0;
			if (aveInnerCount > 0) {
				classInnerCount_aveInnerCount = classInnerCount * classList.size() / aveInnerCount;
			}
			if (classInnerCount_aveInnerCount < 5) {
				vector.add(classInnerCount_aveInnerCount);
			} else {
				vector.add(5);
			}

			// 10:classOuterCount:maxOuterCount
			if (maxOuterCount == 0) {
				vector.add(0);
			} else {
				vector.add(classOuterCount * 5 / maxOuterCount);
			}

			// 11:classOuterCount:aveOuterCount
			int classOuterCount_aveOuterCount = 0;
			if (aveOuterCount > 0) {
				classOuterCount_aveOuterCount = classOuterCount * classList.size() / aveOuterCount;
			}
			if (classOuterCount_aveOuterCount < 5) {
				vector.add(classOuterCount_aveOuterCount);
			} else {
				vector.add(5);
			}

			// 12.ClassType
			if (clazz.getClassType().equals("change")) {
				vector.add(1);
			} else {
				vector.add(0);
			}

			// 13:classIndex
			if (classIndex < 5) {
				vector.add(classIndex);
			} else {
				vector.add(5);
			}
			// 14.isCoreType1
			if (classInnerCount == 0 && classOuterCount > 0) {
				vector.add(1);
			} else if (classInnerCount > 0 && classOuterCount > 0) {
				vector.add(2);
			} else if (classInnerCount == 0 && classOuterCount == 0) {
				vector.add(3);
			} else {
				vector.add(4);
			}

			// 15.isCoreType2
			if (classOuterCount == 0 && classInnerCount > 1) {
				vector.add(1);
			} else {
				vector.add(0);
			}

			// 16.isCoreType3
			if (classInnerCount + classOuterCount > 3) {
				vector.add(1);
			} else {
				vector.add(0);
			}

			// 17.isCoreType4
			if (classOuterCount - classInnerCount > 2) {
				vector.add(1);
			} else if (classOuterCount - classInnerCount == 2) {
				vector.add(2);
			} else if (classOuterCount - classInnerCount == 1) {
				vector.add(3);
			} else if (classOuterCount - classInnerCount == 0) {
				vector.add(4);
			} else if (classOuterCount - classInnerCount == -1) {
				vector.add(5);
			} else if (classOuterCount - classInnerCount == -2) {
				vector.add(6);
			} else {
				vector.add(7);
			}

			// 18.isCoreType5
			if (classInnerCount - classOuterCount > 2) {
				vector.add(1);
			} else {
				vector.add(0);
			}

			// 19.
			for (int i = 0; i < 1; i++) {
				double classInnerWeight = classInnerCount * 1.0d / classList.size();
				if (classInnerWeight == 0) {
					vector.add(0);
				} else if (classInnerWeight <= 0.1) {
					vector.add(1);
				} else if (classInnerWeight <= 0.2) {
					vector.add(2);
				} else if (classInnerWeight <= 0.3) {
					vector.add(3);
				} else if (classInnerWeight <= 0.4) {
					vector.add(4);
				} else if (classInnerWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 20.
			double classOuterWeight = classOuterCount * 1.0d / classList.size();
			if (classOuterWeight == 0) {
				vector.add(0);
			} else if (classOuterWeight <= 0.1) {
				vector.add(1);
			} else if (classOuterWeight <= 0.2) {
				vector.add(2);
			} else if (classOuterWeight <= 0.3) {
				vector.add(3);
			} else if (classOuterWeight <= 0.4) {
				vector.add(4);
			} else if (classOuterWeight <= 0.5) {
				vector.add(5);
			} else {
				vector.add(6);
			}

			// 21.
			if (classList.size() == 2) {
				vector.add(0);
			} else if (classList.size() == 3) {
				vector.add(1);
			} else if (classList.size() == 4) {
				vector.add(2);
			} else if (classList.size() == 5) {
				vector.add(3);
			} else if (classList.size() > 5 && classList.size() <= 10) {
				vector.add(4);
			} else {
				vector.add(5);
			}

			int newMethodNum = (Integer) clazz.getNewMethodNum();
			int changeMethodNum = (Integer) clazz.getChangeMethodNum();
			int deleteMethodNum = (Integer) clazz.getDeleteMethodNum();

			int methodNum = newMethodNum + changeMethodNum + deleteMethodNum;

			// 22.
			if (methodNum <= 5) {
				vector.add(methodNum);
			} else if (methodNum <= 10) {
				vector.add(6);
			} else if (methodNum <= 20) {
				vector.add(7);
			} else {
				vector.add(8);
			}

			// 23.
			if (methodsNum == 0) {
				vector.add(0);
			} else {
				double methodNumWeight = methodNum * 1.0d / methodsNum;
				if (methodNumWeight == 0) {
					vector.add(0);
				} else if (methodNumWeight <= 0.1) {
					vector.add(1);
				} else if (methodNumWeight <= 0.2) {
					vector.add(2);
				} else if (methodNumWeight <= 0.3) {
					vector.add(3);
				} else if (methodNumWeight <= 0.4) {
					vector.add(4);
				} else if (methodNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 24.
			if (newMethodNum <= 5) {
				vector.add(newMethodNum);
			} else if (newMethodNum <= 10) {
				vector.add(6);
			} else {
				vector.add(7);
			}

			// 25.
			if (newMethodsNum == 0) {
				vector.add(0);
			} else {
				double newMethodNumWeight = newMethodNum * 1.0d / newMethodsNum;
				if (newMethodNumWeight == 0) {
					vector.add(0);
				} else if (newMethodNumWeight <= 0.1) {
					vector.add(1);
				} else if (newMethodNumWeight <= 0.2) {
					vector.add(2);
				} else if (newMethodNumWeight <= 0.3) {
					vector.add(3);
				} else if (newMethodNumWeight <= 0.4) {
					vector.add(4);
				} else if (newMethodNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 26.
			if (changeMethodNum <= 5) {
				vector.add(changeMethodNum);
			} else if (changeMethodNum <= 10) {
				vector.add(6);
			} else {
				vector.add(7);
			}

			// 27.
			if (changeMethodsNum == 0) {
				vector.add(0);
			} else {
				double changeMethodNumWeight = changeMethodNum * 1.0d / changeMethodsNum;
				if (changeMethodNumWeight == 0) {
					vector.add(0);
				} else if (changeMethodNumWeight <= 0.1) {
					vector.add(1);
				} else if (changeMethodNumWeight <= 0.2) {
					vector.add(2);
				} else if (changeMethodNumWeight <= 0.3) {
					vector.add(3);
				} else if (changeMethodNumWeight <= 0.4) {
					vector.add(4);
				} else if (changeMethodNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 28.
			if (deleteMethodNum <= 3) {
				vector.add(deleteMethodNum);
			} else if (deleteMethodNum <= 5) {
				vector.add(4);
			} else if (deleteMethodNum <= 10) {
				vector.add(5);
			} else {
				vector.add(6);
			}

			// 29.
			if (deleteMethodsNum == 0) {
				vector.add(0);
			} else {
				double deleteMethodNumWeight = deleteMethodNum * 1.0d / deleteMethodsNum;
				if (deleteMethodNumWeight == 0) {
					vector.add(0);
				} else if (deleteMethodNumWeight <= 0.1) {
					vector.add(1);
				} else if (deleteMethodNumWeight <= 0.2) {
					vector.add(2);
				} else if (deleteMethodNumWeight <= 0.3) {
					vector.add(3);
				} else if (deleteMethodNumWeight <= 0.4) {
					vector.add(4);
				} else if (deleteMethodNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}


			int methodsInnerCount = 0;
			int methodsOuterCount = 0;
			int changeStatementNum = clazz.getChangeStatementNodeTypeList().size();
			int newStatementNum = clazz.getAddStatementNodeTypeList().size();
			int deleteStatementNum = clazz.getDeleteStatementNodeTypeList().size();
			int statementNum = newStatementNum + changeStatementNum + deleteStatementNum;
			
			List<MethodBean> methodList = methodRepo.findByVersionIDAndClassID(versionID,clazz.getClassID());
			for (MethodBean method:methodList) {
				methodsInnerCount += method.getInnerCount();
				methodsOuterCount += method.getOutterCount();

			}
			

			// 30.
			if (methodsInnerCount == 0) {
				vector.add(0);
			} else if (methodsInnerCount <= 3) {
				vector.add(1);
			} else if (methodsInnerCount <= 6) {
				vector.add(2);
			} else {
				vector.add(3);
			}

			// 31.
			if (methodsOuterCount == 0) {
				vector.add(0);
			} else if (methodsOuterCount <= 3) {
				vector.add(1);
			} else if (methodsOuterCount <= 6) {
				vector.add(2);
			} else {
				vector.add(3);
			}

			// 32.
			if (statementNum <= 5) {
				vector.add(statementNum);
			} else if (statementNum <= 10) {
				vector.add(6);
			} else if (statementNum <= 20) {
				vector.add(7);
			} else if (statementNum <= 30) {
				vector.add(8);
			} else if (statementNum <= 40) {
				vector.add(9);
			} else {
				vector.add(10);
			}

			// 33.
			if (statementsNum == 0) {
				vector.add(0);
			} else {
				double statementNumWeight = statementNum * 1.0d / statementsNum;
				if (statementNumWeight == 0) {
					vector.add(0);
				} else if (statementNumWeight <= 0.1) {
					vector.add(1);
				} else if (statementNumWeight <= 0.2) {
					vector.add(2);
				} else if (statementNumWeight <= 0.3) {
					vector.add(3);
				} else if (statementNumWeight <= 0.4) {
					vector.add(4);
				} else if (statementNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 34. statementNum/maxStatementNum
			if (maxStatementNum == 0) {
				vector.add(0);
			} else {
				vector.add(statementNum * 5 / maxStatementNum);
			}

			// 35. statementNum/aveStatementNum
			int statementNum_aveStatementNum = 0;
			if (aveStatementNum > 0) {
				statementNum_aveStatementNum = statementNum * classList.size() / aveStatementNum;
			}
			if (statementNum_aveStatementNum < 5) {
				vector.add(statementNum_aveStatementNum);
			} else {
				vector.add(5);
			}

			// 36
			if (newStatementNum == 0) {
				vector.add(0);
			} else if (newStatementNum <= 5) {
				vector.add(newStatementNum);
			} else if (newStatementNum <= 10) {
				vector.add(6);
			} else if (newStatementNum <= 20) {
				vector.add(7);
			} else {
				vector.add(8);
			}

			// 37
			if (newStatementsNum == 0) {
				vector.add(0);
			} else {
				double newStatementNumWeight = newStatementNum * 1.0d / newStatementsNum;
				if (newStatementNumWeight == 0) {
					vector.add(0);
				} else if (newStatementNumWeight <= 0.1) {
					vector.add(1);
				} else if (newStatementNumWeight <= 0.2) {
					vector.add(2);
				} else if (newStatementNumWeight <= 0.3) {
					vector.add(3);
				} else if (newStatementNumWeight <= 0.4) {
					vector.add(4);
				} else if (newStatementNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 38
			if (changeStatementNum == 0) {
				vector.add(0);
			} else if (changeStatementNum <= 5) {
				vector.add(changeStatementNum);
			} else if (changeStatementNum <= 10) {
				vector.add(6);
			} else {
				vector.add(7);
			}

			// 39
			if (changeStatementsNum == 0) {
				vector.add(0);
			} else {
				double changeStatementNumWeight = changeStatementNum * 1.0d / changeStatementsNum;
				if (changeStatementNumWeight == 0) {
					vector.add(0);
				} else if (changeStatementNumWeight <= 0.1) {
					vector.add(1);
				} else if (changeStatementNumWeight <= 0.2) {
					vector.add(2);
				} else if (changeStatementNumWeight <= 0.3) {
					vector.add(3);
				} else if (changeStatementNumWeight <= 0.4) {
					vector.add(4);
				} else if (changeStatementNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			// 40
			if (deleteStatementNum == 0) {
				vector.add(0);
			} else if (deleteStatementNum <= 3) {
				vector.add(deleteStatementNum);
			} else if (deleteStatementNum <= 10) {
				vector.add(4);
			} else {
				vector.add(5);
			}

			// 41
			if (deleteStatementsNum == 0) {
				vector.add(0);
			} else {
				double deleteStatementNumWeight = deleteStatementNum * 1.0d / deleteStatementsNum;
				if (deleteStatementNumWeight == 0) {
					vector.add(0);
				} else if (deleteStatementNumWeight <= 0.1) {
					vector.add(1);
				} else if (deleteStatementNumWeight <= 0.2) {
					vector.add(2);
				} else if (deleteStatementNumWeight <= 0.3) {
					vector.add(3);
				} else if (deleteStatementNumWeight <= 0.4) {
					vector.add(4);
				} else if (deleteStatementNumWeight <= 0.5) {
					vector.add(5);
				} else {
					vector.add(6);
				}
			}

			vector.add(0);
			vectorList.add(vector);
		}
		return vectorList;
	}
}
