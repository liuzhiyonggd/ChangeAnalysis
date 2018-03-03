package sysu.commconsistency.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sysu.commconsistency.bean.CommentEntry;
import sysu.database.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepo;
	
	public List<CommentEntry> findByVersionID(int versionID){
		return commentRepo.findByVersionID(versionID);
	}

}
