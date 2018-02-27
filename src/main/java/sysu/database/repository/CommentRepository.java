package sysu.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.commconsistency.bean.CommentEntry;


public interface CommentRepository extends MongoRepository<CommentEntry,String>{
	
	public List<CommentEntry> findByVersionIDAndClassID(int versionID,int classID);
	
	
}
