package models;

import dtos.VendorDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import javax.enterprise.context.RequestScoped;  
import javax.inject.Named;
import javax.sql.DataSource;
import java.lang.String;

/*
 * VendorModel.java
 *
 * Created on Aug 31, 2013, 3:03 PM
 *  Purpose:    Contains methods for supporting db access for vendor information
 *              Usually consumed by the ViewModel Class via DTO
 *  Revisions: 
 */
@Named(value = "vendorModel")  
@RequestScoped
public class VendorModel implements Serializable {

    public VendorModel() {
    }
    public int addVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int vendorNo = -1;

        String sql = "INSERT INTO Vendors (Address1,City,Province,PostalCode,"
                + "Phone,VendorType,Name,Email) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.execute();


            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                vendorNo = rs.getInt(1);
            }
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
        return vendorNo;
    }
    
    public ArrayList<VendorDTO> getVendors(DataSource ds) {
        ArrayList<VendorDTO> vendors = new ArrayList<>();
        Connection con = null;
        String sql = "SELECT * FROM VENDORS";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                VendorDTO vdto = new VendorDTO();
                vdto.setAddress1(rs.getString("Address1"));
                vdto.setCity(rs.getString("City"));
                vdto.setProvince(rs.getString("Province"));
                vdto.setPostalCode(rs.getString("PostalCode"));
                vdto.setPhone(rs.getString("Phone"));
                vdto.setType(rs.getString("VendorType"));
                vdto.setName(rs.getString("Name"));
                vdto.setEmail(rs.getString("Email"));
                vdto.setVendorno(rs.getInt("Vendorno"));
                vendors.add(vdto);
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
        return vendors;
    }
    
    public VendorDTO getVendorById(int vendorno, DataSource ds) {
        VendorDTO vendor = new VendorDTO();
        Connection con = null;
        String sql = "SELECT * FROM VENDORS WHERE VENDORNO = ?";
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
                vendor.setAddress1(rs.getString("Address1"));
                vendor.setCity(rs.getString("City"));
                vendor.setProvince(rs.getString("Province"));
                vendor.setPostalCode(rs.getString("PostalCode"));
                vendor.setPhone(rs.getString("Phone"));
                vendor.setType(rs.getString("VendorType"));
                vendor.setName(rs.getString("Name"));
                vendor.setEmail(rs.getString("Email"));
                vendor.setVendorno(rs.getInt("Vendorno"));
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
        return vendor;
    }
    
    public int updateVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numRows = -1;


        String sql = "UPDATE Vendors SET Address1=?,City=?,Province=?,PostalCode=?,"
                + "Phone=?,VendorType=?,Name=?,Email=?"
                + " WHERE VendorNo=?";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.setInt(9, details.getVendorno());
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

    public int deleteVendor(int vendorno, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numRows = -1;
        String sql = "DELETE FROM VENDORS WHERE VENDORNO = ?";
        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);
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