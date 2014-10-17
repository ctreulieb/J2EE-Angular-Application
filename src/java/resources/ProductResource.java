// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: ProductResource.java
// Purpose: Product Resource for calling add/update/delete/retrieve from the model.

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
    
    /**
     * createProductFromJson
     * @param product - ProductDTO containing new product information
     * @return response object from the model
     */
    @POST
    @Consumes("application/json")
    public Response createProductFromJson(ProductDTO product) {
        ProductModel model = new ProductModel();
        int numProductsAdded = model.addProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numProductsAdded).build();
    }
    
    /**
     * getProductsJson
     * @return all products in JSON format
     */
    @GET
    @Produces("application/json")
    public ArrayList<ProductDTO> getProductsJson() {
        ProductModel model = new ProductModel();
        return model.getProducts(ds);
    }
    
    /**
     * getVendorProductsJson
     * @param vendorno - Vendor no of vendor who's product info you want
     * @return all products for specified vendor in JSON format
     */
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public ArrayList<ProductDTO> getVendorProductsJson(@PathParam("vendorno") int vendorno) {
        ProductModel model = new ProductModel();
        return model.getAllProductsForVendor(vendorno, ds);
    }
    
    /**
     * updateProductFromJson
     * @param product - ProductDTO containing product information to be updated
     * @return Response from model
     */
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductDTO product) {
        ProductModel model = new ProductModel();
        int numRowsUpdated = model.updateProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numRowsUpdated).build();
    }
    
    /**
     * deleteProductFromJson
     * @param productcode - product code to be deleted
     * @return Response from model
     */
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
