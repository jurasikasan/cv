package sample.my.cv.facade;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import sample.my.cv.model.User;

@Stateless
@Path("/authentication")
public class AuthenticationEndpoint {

    @Inject
    private UserFacadeImpl users;
    private static Map<String, Integer> sessions = new HashMap<>();

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
            @FormParam("password") String password) {

        try {

            // Authenticate the user using the credentials provided
            int id = authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(id);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private int authenticate(String username, String password) throws Exception {
        for (User user : users.findAll()) {
            if (user.getLogin().equals(username) && user.getPassword().equals(password)) {
                return user.getId();
            }
        }
        throw new Exception("UNAUTHORIZED");
    }

    private String issueToken(int userId) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        sessions.put(token, userId);
        return token;
    }

    public int getUserByTocken(String token) throws Exception {
        if (sessions.containsKey(token)) {
            return sessions.get(token);
        }
        throw new Exception("UNAUTHORIZED");
    }

}
