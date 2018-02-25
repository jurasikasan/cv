package sample.my.cv.beans;

import java.io.IOException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import sample.my.cv.facade.AuthenticationEndpoint;
import sample.my.cv.facade.UserFacadeImpl;

@Named
@SessionScoped
public class LoginUser implements Serializable {

    private String userName;
    private String password;

    @Inject
    private AuthenticationEndpoint usersManager;

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

    public void login() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            Response authenticateUser = usersManager.authenticateUser(userName, password);
            if (authenticateUser.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new Exception("UNAUTHORIZED");
            }
            token = (String) authenticateUser.getEntity();
            handler.performNavigation("users?faces-redirect=true");
        } catch (Exception ex) {
            FacesMessage facesMessage = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    @Produces
    @Dependent
    @Auth
    private String token;

    public void checkLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (token==null) {
            System.out.println("checkLoggedIn");
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) context.getApplication().getNavigationHandler();
            handler.performNavigation("/");
        }
    }

    public void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {

        }
        token = userName = password = null;
    }
}
