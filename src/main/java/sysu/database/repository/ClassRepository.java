package sysu.database.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.coreclass.bean.ClassBean;


public interface ClassRepository extends MongoRepository<ClassBean,String>{
	
	public List<ClassBean> findByVersionID(int versionID);

}
