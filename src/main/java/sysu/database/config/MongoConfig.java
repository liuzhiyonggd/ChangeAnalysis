package sysu.database.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;


@Configuration
@ComponentScan
@EnableMongoRepositories(basePackages="sysu.database.repository")
public class MongoConfig extends AbstractMongoConfiguration{

	@Override
	protected String getDatabaseName() {
		return "sourcebase2";
	}

	@Override
	public Mongo mongo() throws Exception {
//		MongoCredential credential = MongoCredential.createMongoCRCredential("zhiyong", "sourcebase", "liu888888".toCharArray());
		return new MongoClient(new ServerAddress("192.168.1.55",27017));
//		return new MongoClient("localhost");
	}
	
	
}
