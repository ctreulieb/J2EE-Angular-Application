package models;

import dtos.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import javax.sql.DataSource;
/**
 *
 * @author Craig
 */
public class ProductModel {
    
    public ProductModel() {
    }
    public int addProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int rowsAdded = -1;
        
        String sql = "INSERT INTO Products (productcode,vendorno,vendorsku,productname,costprice,"
                + "msrp,rop,eoq,qoh,qoo)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        try{
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getProductcode());
            pstmt.setInt(2, details.getVendorno());
            pstmt.setString(3, details.getVendorsku());
            pstmt.setString(4, details.getProductname());
            pstmt.setDouble(5, details.getCostprice());
            pstmt.setDouble(6, details.getMsrp());
            pstmt.setInt(7, details.getRop());
            pstmt.setInt(8, details.getEoq());
            pstmt.setInt(9, details.getQoh());
            pstmt.setInt(10, details.getQoo());
            pstmt.execute();
            
            rowsAdded = pstmt.getUpdateCount();
            
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return rowsAdded;
    }
    
    public ArrayList<ProductDTO> getProducts(DataSource ds){
        ArrayList<ProductDTO> products = new ArrayList<>();
        Connection con = null;
        String sql = "SELECT * FROM PRODUCTS";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                ProductDTO pdto = new ProductDTO();
                pdto.setCostprice(rs.getDouble("costprice"));
                pdto.setQoh(rs.getInt("qoh"));
                pdto.setEoq(rs.getInt("eoq"));
                pdto.setQoo(rs.getInt("qoo"));
                pdto.setRop(rs.getInt("rop"));
                pdto.setVendorno(rs.getInt("vendorno"));
                pdto.setProductcode(rs.getString("productcode"));
                pdto.setVendorsku(rs.getString("vendorsku"));
                pdto.setProductname(rs.getString("productname"));
                pdto.setMsrp(rs.getDouble("msrp"));
                products.add(pdto);
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return products;
    }
    
    public ArrayList<ProductDTO> getAllProductsForVendor(int vendorno, DataSource ds) {
        ArrayList<ProductDTO> products = new ArrayList<>();
        Connection con = null;
        String sql = "SELECT * FROM PRODUCTS WHERE vendorno = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, vendorno);
            rs = pstmt.executeQuery();
            while(rs.next()){
                ProductDTO pdto = new ProductDTO();
                pdto.setCostprice(rs.getDouble("costprice"));
                pdto.setQoh(rs.getInt("qoh"));
                pdto.setEoq(rs.getInt("eoq"));
                pdto.setQoo(rs.getInt("qoo"));
                pdto.setRop(rs.getInt("rop"));
                pdto.setVendorno(rs.getInt("vendorno"));
                pdto.setProductcode(rs.getString("productcode"));
                pdto.setVendorsku(rs.getString("vendorsku"));
                pdto.setProductname(rs.getString("productname"));
                pdto.setMsrp(rs.getDouble("msrp"));
                products.add(pdto);
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return products;
    }
    
    public int updateProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numRows = -1;
        
        String sql = "UPDATE Products SET qoo=?,vendorno=?,vendorsku=?,productname=?,"
                + "costprice=?,msrp=?,rop=?,eoq=?,qoh=?"
                + "WHERE productcode=?";
        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, details.getQoo()); 
            pstmt.setInt(2, details.getVendorno());
            pstmt.setString(3, details.getVendorsku());
            pstmt.setString(4, details.getProductname());
            pstmt.setDouble(5, details.getCostprice());
            pstmt.setDouble(6, details.getMsrp());
            pstmt.setInt(7, details.getRop());
            pstmt.setInt(8, details.getEoq());
            pstmt.setInt(9, details.getQoh());            
            pstmt.setString(10, details.getProductcode());
            pstmt.execute();
            
            numRows = pstmt.getUpdateCount();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return numRows;
    }
    
    public int deleteProduct(String productcode, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numRows = -1;
        String sql = "DELETE FROM PRODUCTS WHERE PRODUCTCODE = ?";
        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, productcode);
            pstmt.execute();
            
            numRows = pstmt.getUpdateCount();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        
        return numRows;
    }
    
}
