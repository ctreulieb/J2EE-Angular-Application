package models;

import dtos.PurchaseOrderLineItemDTO;
import dtos.PurchaseOrderDTO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList; 
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;import java.sql.ResultSet;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Craig
 */
public class PurchaseOrderModel {
    
    public PurchaseOrderModel(){}
    
    public String purchaseOrderAdd(double total, int vendorno, ArrayList<PurchaseOrderLineItemDTO> items, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";
        Date date = new Date();
        java.sql.Date sDate = new java.sql.Date(date.getTime());
        int poNum = -1;
        String sql = "INSERT INTO purchaseorders (vendorno,podate,amount) VALUES(?,?,?)";
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);
            pstmt.setDate(2, sDate);
            pstmt.setBigDecimal(3, BigDecimal.valueOf(total));
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            poNum = rs.getInt(1);
            for( PurchaseOrderLineItemDTO item : items) {
                if ( item.getQty() > 0 ) {
                    sql="INSERT INTO PurchaseOrderLineItems (PONumber,prodcd,QTY,Price) VALUES (?,?,?,?)";
                    pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1,poNum);
                    pstmt.setString(2, item.getProductcode());
                    pstmt.setInt(3, item.getQty());
                    pstmt.setBigDecimal(4, BigDecimal.valueOf(item.getMsrp()));
                    pstmt.execute();
                }                
            }
            con.commit();
            msg = Integer.toString(poNum);
            con.close();
        }catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
            msg = "PO not added ! - " + se.getMessage();
            try {
                con.rollback();
            } catch (SQLException sqx) {
                System.out.println("Rollback failed - " + sqx.getMessage());
            }
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
        
        return msg;
    }
    
    public PurchaseOrderDTO getPurchaseInfoByPONumber(int ponum, DataSource ds) {
        PurchaseOrderDTO purchaseOrder = new PurchaseOrderDTO();
        Connection con = null;
        String sql = "SELECT * FROM PURCHASEORDERS WHERE PONUMBER = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ponum);
            rs = pstmt.executeQuery();
            while(rs.next()){
                purchaseOrder.setVendorno(rs.getInt("vendorno"));
                purchaseOrder.setTotal(rs.getDouble("amount"));
                purchaseOrder.setDate(rs.getString("podate"));
                purchaseOrder.setPonumber(rs.getInt("ponumber"));
            }
            sql = "SELECT POL.QTY, POL.PRODCD, POL.PRICE, P.PRODUCTNAME FROM PURCHASEORDERLINEITEMS POL INNER JOIN PRODUCTS P ON POL.PRODCD=P.PRODUCTCODE WHERE POL.PONUMBER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ponum);
            ArrayList<PurchaseOrderLineItemDTO> litems = new ArrayList(){};
            rs = pstmt.executeQuery();
            while(rs.next()) {
                PurchaseOrderLineItemDTO litem = new PurchaseOrderLineItemDTO();
                litem.setMsrp(rs.getDouble("price"));
                litem.setProductcode(rs.getString("prodcd"));
                litem.setProductname(rs.getString("productname"));
                litem.setQty(rs.getInt("qty"));
                litems.add(litem);               
            }
            purchaseOrder.setItems(litems);
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
        return purchaseOrder;
    }
}
