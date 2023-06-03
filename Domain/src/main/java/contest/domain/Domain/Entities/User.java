package contest.domain.Domain.Entities;


import contest.domain.Domain.Entity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "users")
public class User extends Entity<String> {
    private String name;
    private String password;
    private String username;

    public User(String name, String username, String password) {
        super(username);
        this.name = name;
        this.password = password;
    }

    public User() {
        this.setId("");
        this.name="";
        this.username="";
        this.password="";
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 
     */
    public String getUsername() {
        return getId();
    }
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String password) {
        this.username= password;
    }
}
