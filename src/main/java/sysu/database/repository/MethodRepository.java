package sysu.database.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.coreclass.bean.MethodBean;



public interface MethodRepository extends MongoRepository<MethodBean,String>{
	
	public List<MethodBean> findByVersionIDAndClassID(int versionID,int classID);
	
}
