package sysu.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.web.bean.Version;


public interface VersionRepository extends MongoRepository<Version,String>{

	public List<Version> findByUsername(String username);
}
