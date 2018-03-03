package sysu.coreclass.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sysu.coreclass.bean.MethodBean;
import sysu.database.repository.MethodRepository;

@Service
public class MethodService {
	@Autowired
	private MethodRepository methodRepo;
	
	public List<MethodBean> findByVersionIDAndClassID(int versionID,int classID){
		return methodRepo.findByVersionIDAndClassID(versionID, classID);
	}
}
