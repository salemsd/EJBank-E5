package com.ejbank.api;

import com.ejbank.api.payload.UserPayload;
import com.ejbank.beans.UserBean;
import com.ejbank.entities.User;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    @EJB
    private UserBean userBean;

    @GET
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@PathParam("user_id") int userId) {
        User user = userBean.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Utilisateur introuvable").build();
        }
        return Response.ok(new UserPayload(user.getFirstname(), user.getLastname())).build();
    }
}