/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resources;

import dtos.ProductDTO;
import java.net.URI;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.ProductModel;

@Path("product")
public class ProductResource {
    
    @Context
    private UriInfo context;
    
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;
    
    public ProductResource() {
    }
    
    
    @POST
    @Consumes("application/json")
    public Response createProductFromJson(ProductDTO product) {
        ProductModel model = new ProductModel();
        int numProductsAdded = model.addProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numProductsAdded).build();
    }
    
    @GET
    @Produces("application/json")
    public ArrayList<ProductDTO> getProductsJson() {
        ProductModel model = new ProductModel();
        return model.getProducts(ds);
    }
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public ArrayList<ProductDTO> getVendorProductsJson(@PathParam("vendorno") int vendorno) {
        ProductModel model = new ProductModel();
        return model.getAllProductsForVendor(vendorno, ds);
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductDTO product) {
        ProductModel model = new ProductModel();
        int numRowsUpdated = model.updateProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{productcode}")
    @Consumes("application/json")
    public Response deleteProductFromJson(@PathParam("productcode")String productcode) {
        ProductModel  model = new ProductModel();
        int numRowsDeleted = model.deleteProduct(productcode, ds);
        URI uri = context.getAbsolutePath();
        System.out.println("number of rows deleted " + numRowsDeleted);
        return Response.created(uri).entity(numRowsDeleted).build();
    }
    
}
