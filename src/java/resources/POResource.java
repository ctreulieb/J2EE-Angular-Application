// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: POResource.java
// Purpose: Purchase Order Resource for calling add/update/delete/retrieve from the model.
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
    
    /**
     * createPo - Creates Purchase order out of provided PurchaseOrderDTO
     * @param po - PurchaseOrderDTO containing all info about Purchase Order to be added
     * @return - Response Object from model
     */
    @POST
    @Consumes("application/json")
    public Response createPO(PurchaseOrderDTO po) {
        PurchaseOrderModel model = new PurchaseOrderModel();
        String msg = model.purchaseOrderAdd(po.getTotal(), po.getVendorno(), po.getItems(), ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(msg).build();
    }
    
}