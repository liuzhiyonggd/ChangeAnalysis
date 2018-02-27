package sysu.coreclass.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sysu.coreclass.bean.ClassBean;
import sysu.database.repository.ClassRepository;

@Service
public class ClassService {
	
	@Autowired
	private ClassRepository classRepo;
	
	public List<ClassBean> findByVersionID(int versionID){
		return classRepo.findByVersionID(versionID);
	}

}
