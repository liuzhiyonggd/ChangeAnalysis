package sysu.coreclass.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sysu.database.repository.VersionRepository;
import sysu.web.bean.Version;

@Service
public class VersionService {

	@Autowired
	private VersionRepository versionRepo;
	
	public List<Version> findByUsername(String username){
		return versionRepo.findByUsername(username);
	}
}
