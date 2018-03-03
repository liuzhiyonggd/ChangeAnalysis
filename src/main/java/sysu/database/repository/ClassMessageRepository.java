package sysu.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.commconsistency.bean.ClassMessage;


public interface ClassMessageRepository extends MongoRepository<ClassMessage,String>{
	
	public List<ClassMessage> findByVersionID(int versionID);
	public ClassMessage findASingleByVersionIDAndClassID(int versionID,int classID);
	

}
