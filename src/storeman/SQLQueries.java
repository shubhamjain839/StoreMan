/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storeman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.TableModel;

/**
 *
 * @author shubh
 */
public class SQLQueries {

    private static Connection Conn = null;

    public static Connection getConnection() {
        try {
            if (Conn == null) {
                Conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\shubh\\StoreMan\\db\\data.db");
            }
            return Conn;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static ResultSet fillCategory() {
        try {
            Connection con = getConnection();
            String selectQuery = "select CATEGORY_NAME from categories";
            Statement st = con.createStatement();
            return st.executeQuery(selectQuery);
        } catch (SQLException s) {
            System.out.print(s.getMessage());
        }
        return null;
    }

    public static TableModel populateTable(String category) {
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement("select * from products where PRODUCT_CATEGORY = ?");
            pst.setString(1, category);
            ResultSet rs = pst.executeQuery();
            GetTable gt = new GetTable();
            return (gt.setRowCol(rs));
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return null;
    }

    public static boolean[] Authentication(String id, String password) {
        try {
            Connection con = getConnection();
            boolean[] res = new boolean[2];
            String query = "select password,admin from credentials where id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next() || !password.equals(rs.getString(1))) {
                res[0] = false;
                res[1] = false;
            } else {
                res[1] = Boolean.parseBoolean(rs.getString(2));
                res[0] = true;
            }
            return res;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return new boolean[]{false, false};
    }

    public static ResultSet fillProductByCategory(String category) {
        try {
            Connection con = getConnection();
            String getData = "select * from products where PRODUCT_CATEGORY = ?";
            PreparedStatement pst = con.prepareStatement(getData);
            pst.setString(1, category);
            return pst.executeQuery();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return null;
    }

    public static ResultSet fillProductInformation(String productName) {
        try {
            Connection con = getConnection();
            String getData = "select * from products where PRODUCT_NAME = ?";
            PreparedStatement pst = con.prepareStatement(getData);
            pst.setString(1, productName);
            return pst.executeQuery();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return null;
    }

    public static boolean addToCategoryTable(String category) {
        Connection con = getConnection();
        try {
            String Query = "insert into categories (CATEGORY_NAME) values(?)";
            PreparedStatement st = con.prepareStatement(Query);
            st.setString(1, category.toUpperCase());
            return !st.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static TableModel fillTableAllProducts() {
        try {
            Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from products");
            GetTable gt = new GetTable();
            return gt.setRowCol(rs);
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return null;
    }

    public static int getInvoiceNumber() {
        Connection con = getConnection();
        int invoice = 0;
        try {
            String query = "select count(*) from bills";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            invoice = Integer.parseInt(rs.getString(1));
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return invoice;
    }

    public static void storeInvoice(String amount, String cusName, String date) {
        Connection con = getConnection();
        try {
            String query = "insert into bills (AMOUNT,CUSTOMER_NAME,DATE) values (?,?,?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, amount);
            pst.setString(2, cusName);
            pst.setString(3, date);
            pst.execute();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public static void removeItemFromInventory(String p_name, String quantity) {
        Connection con = getConnection();
        int qts = Integer.parseInt(quantity);
        try {
            String query = "update products set PRODUCT_QUANTITY = PRODUCT_QUANTITY - ? where PRODUCT_NAME = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, qts);
            pst.setString(2, p_name);
            pst.executeUpdate();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public static boolean checkQuantity(String valueOf, int qty) {
        try {
            Connection con = getConnection();
            String query = "select product_quantity from products where product_name=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, valueOf);
            ResultSet rs = pst.executeQuery();
            int quant = rs.getInt(1);
            if (qty <= quant) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean authen(String oldPass, String userName) {
        Connection con = getConnection();
        try {
            String query = "select password from credentials where id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            if (oldPass.endsWith(rs.getString(1))) {
                return true;
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    public static boolean changePassword(String newPass, String userName) {
        Connection con = getConnection();
        try {
            String query = "update credentials set password = ? where id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newPass);
            pst.setString(2, userName);
            int check = pst.executeUpdate();
            if (check > 0) {
                return true;
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    public static boolean addUser(String newUserName, String newUserPass, String contact) {
        Connection con = getConnection();
        try {
            String query = "insert into credentials (id,password,contact) values (?,?,?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newUserName);
            pst.setString(2, newUserPass);
            pst.setString(3, contact);
            boolean check = pst.execute();
            return !check;
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    public static boolean addToProductTable(String name, int price, int quantity, String category) {
        try {
            Connection con = getConnection();
            String AddQuery = "insert into products(PRODUCT_CATEGORY,PRODUCT_NAME,PRODUCT_RATE,PRODUCT_QUANTITY) values (?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(AddQuery);
            pst.setString(1, category);
            pst.setString(2, name);
            pst.setInt(3, price);
            pst.setInt(4, quantity);
            return !pst.execute();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    public static int productTableUpdate(String name, int price, int quantity) {
        try {
            Connection con = getConnection();
            String getData = "update products set PRODUCT_RATE = ?, PRODUCT_QUANTITY = ?  where PRODUCT_NAME= ?";
            PreparedStatement pst = con.prepareStatement(getData);
            pst.setInt(1, price);
            pst.setInt(2, quantity);
            pst.setString(3, name);
            return pst.executeUpdate();
        } catch (NumberFormatException nfe) {
            System.out.print(nfe.getMessage());
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return 0;
    }

    public static int productQuantityUpdate(String name, int price, int quantity) {
        try {
            Connection con = getConnection();
            String query = "update products set PRODUCT_QUANTITY = ? + PRODUCT_QUANTITY,PRODUCT_RATE = ? where PRODUCT_NAME = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, quantity);
            pst.setInt(2, price);
            pst.setString(3, name);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return 0;
    }
    public static ResultSet getCredentialsTableData(String currentUser){
        try{
            Connection con = getConnection();
            String query = "select * from credentials where id <> ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,currentUser);
            return pst.executeQuery();
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }
    public static int deleteUser(String userName){
        try{
            Connection con = getConnection();
            String query = "delete from credentials where id == ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,userName);
            return pst.executeUpdate();
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return 0;
    }
    public static TableModel getInvoiceData(){
        try{
            Connection con = getConnection();
            String query = "select * from bills";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            GetTable gt = new GetTable();
            return gt.setRowCol(rs);
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }

    public static ResultSet getTotalItems() {
         try{
            Connection con = getConnection();
            String query = "select sum(PRODUCT_QUANTITY) from products";
            Statement st = con.createStatement();
            return st.executeQuery(query);
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }

    static ResultSet getTotalProducts() {
        try{
            Connection con = getConnection();
            String query = "select count(PRODUCT_NAME) from products";
            Statement st = con.createStatement();
            return st.executeQuery(query);
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }

    static ResultSet getTotalCategories() {
        try{
            Connection con = getConnection();
            String query = "select count(CATEGORY_NAME) from categories";
            Statement st = con.createStatement();
            return st.executeQuery(query);
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }

    static ResultSet getInventoryValue() {
        try{
            Connection con = getConnection();
            String query = "select sum(PRODUCT_QUANTITY*PRODUCT_RATE) from products";
            Statement st = con.createStatement();
            return st.executeQuery(query);
        }
        catch(SQLException se){ System.out.println(se.getMessage());}
        return null;
    }
}
