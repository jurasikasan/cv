package sample.my.cv.facade;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import sample.my.cv.model.User;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter
        implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    @Inject
    private UserFacadeImpl users;
    @Inject
    private AuthenticationEndpoint authenticationEndpoint;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        try {
            User user = users.find(authenticationEndpoint.getUserByTocken(token));
            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(user, classRoles);
            } else {
                checkPermissions(user, methodRoles);
            }
        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();

        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class
            );
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(User user, List<Role> allowedRoles) throws Exception {
        if (allowedRoles.isEmpty()) {
            return;
        }
        Role userRole = user.getId() == UserFacadeImpl.SERVICE.getId() ? Role.ADMIN : Role.CLIENT;
        if (allowedRoles.contains(userRole)) {
            return;
        }
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
        throw new Exception();
    }
}
