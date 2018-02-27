package sysu.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import sysu.web.bean.User;



public interface UserRepository extends MongoRepository<User,String>{
		User findByUserName(String userName);

}
