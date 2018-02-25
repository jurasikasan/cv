package sample.my.cv.facade;

import sample.my.cv.dao.UserDao;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import sample.my.cv.model.User;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Stateless
@Secured({Role.ADMIN})
@Path("users")
public class UserFacadeImpl {

    @Inject
    private UserDao dao;

    public static final User SERVICE = new User();

    static {
        SERVICE.setId(-1);
        SERVICE.setLogin("service");
        SERVICE.setPassword("qwedsazxc");
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public int save(User user) {//create
        isValid(user);
        dao.save(user);
        return user.getId();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public User update(User user) {//edit
        isValid(user);
        return dao.update(user);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Integer id) {//remove
        dao.delete(dao.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public User find(@PathParam("id") Integer id) {
        if(id == SERVICE.getId()){
            return SERVICE;
        }
        return dao.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        List<User> users = dao.findAll();
        users.add(SERVICE);
        return users;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return dao.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String count() {
        return String.valueOf(dao.count());
    }

    private void isValid(User user) {

        boolean hasError = false;

        if (user == null) {
            hasError = true;
        }

        if (hasError) {
            throw new IllegalArgumentException();
        }
    }

}
