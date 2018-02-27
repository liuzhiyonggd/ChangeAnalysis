package sysu.commconsistency.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sysu.commconsistency.bean.ClassMessage;
import sysu.database.repository.ClassMessageRepository;

@Service
public class ClassMessageService {
	
	@Autowired
	private ClassMessageRepository classMessageRepo;
	
	public List<ClassMessage> findByVersionID(int versionID){
		return classMessageRepo.findByVersionID(versionID);
	}

}
