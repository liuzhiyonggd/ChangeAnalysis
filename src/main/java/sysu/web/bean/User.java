package sysu.web.bean;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/*用户表*/
@Document(collection="user")
public class User {

    @Id
    private String id;

    @Field("user_name")
    private String userName;
    private String password;
    
    private List<String> roles;

    public User(){}

    public User(User user){
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.roles = user.roles;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
    
}

