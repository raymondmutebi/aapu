package org.pahappa.systems.api;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.utils.SortField;

@Path("/Payments")
public class ApiPaymentService {

    SortField sortField = new SortField("name", "name", false);

    @POST
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response packages(@Context HttpServletRequest request, @FormParam("sessionId") String sessionId, @FormParam("serviceCode") String serviceCode, @FormParam("phoneNumber") String phoneNumber, @FormParam("text") String text) throws JSONException, IOException, ValidationFailedException {

        String result, churchCode, packageCode;
        String[] codes = null;

        System.out.println("sessionId: " + sessionId);
        System.out.println("serviceCode: " + serviceCode);
        System.out.println("phoneNumber: " + phoneNumber);
        System.out.println("text: " + text);

        if (serviceCode == null || phoneNumber == null) {
            result = "Not a valid request";
            return Response.status(403).entity(result).build();
        }

        if (text == null || text.isEmpty()) {
            result = "END Enter Church Code to continue. 0 to cancel \n";
            return Response.status(200).entity(result).build();

        }

        result = "END Invalid request";
        return Response.status(200).entity(result).build();
    }

    public static String[] getCodes(String fullString) {
        String withoutHash = fullString;
        // String withoutHash = fullString.replace("#", "");
        return withoutHash.split("\\*");
    }

}
