/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dtos.PurchaseOrderDTO;
import models.PurchaseOrderModel;
import java.net.URI;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("po")
public class POResource {
    
    @Context
    private UriInfo context;
    
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;
    
    public POResource() {
    }
    
    
    @POST
    @Consumes("application/json")
    public Response createPO(PurchaseOrderDTO po) {
        PurchaseOrderModel model = new PurchaseOrderModel();
        String msg = model.purchaseOrderAdd(po.getTotal(), po.getVendorno(), po.getItems(), ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
}