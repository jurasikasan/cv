package sample.my.cv.beans;


import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sample.my.cv.facade.UserFacadeImpl;
import sample.my.cv.model.User;

@Named
@SessionScoped
public class Users implements Serializable{
//
//    @Inject
//    @Auth
//    protected String login;
    
    @Inject
    private UserFacadeImpl users;

    private String login1;
    private String password1;

    public String getLogin() {
        return login1;
    }

    public void setLogin(String login) {
        this.login1 = login;
    }

    public String getPassword() {
        return password1;
    }

    public void setPassword(String password) {
        this.password1 = password;
    }

    public UserFacadeImpl getUsersManager() {
        return users;
    }

    public void save() {
        User user = new User();
        user.setLogin(login1);
        user.setPassword(password1);
        users.save( user);
    }


}
