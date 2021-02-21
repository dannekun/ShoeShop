import javax.management.Query;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;
import java.sql.*;

/**
 * Created by Daniel Bojic
 * Date: 2021-02-21
 * Time: 00:40
 * Project: Inlämningsuppgift 2
 * Copyright: MIT
 */
public class GUI {


    Connection con = null;
    ResultSet rs;
    Statement stmt;

    Scanner in = new Scanner(System.in);

    int purchase = 0;

    String purchaseNr;

    //LAST_INSERT_ID;
    //LAST_INSERT_ID-1;

    String username = "daniel";
    String password;
    public void connectToAndQueryDatabase(String username, String
            password) throws SQLException {


        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/database_webshop", username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stmt = con.createStatement();


        selectAllProducts();

    }

    public void selectAllProducts() throws SQLException {
        int y = 0;
        int product;

        rs = stmt.executeQuery("SELECT namn from produkt");
        while (rs.next()) {
            y++;
            String x = rs.getString("namn");
            System.out.println(y+". " +x);
        }

        product = Integer.parseInt(in.next());

        purchaseProduct(product);


    }



public void purchaseProduct(int produktid) throws SQLException {
    CallableStatement stm;
    if (purchase == 0){
            stm = con.prepareCall("call AddToCart((SELECT id from kund where förnamn = ?), 9 ,?)");
            stm.setString(1, username);
            stm.setInt(2, produktid);
            stm.execute();
        System.out.println("ingen");
        }else {
            stm = con.prepareCall("call AddToCart((SELECT id from kund where förnamn = ?), 9 ,?)");
            stm.setString(1, username);
            stm.setInt(2, produktid);
            stm.execute();
        System.out.println("du har redan");
        }



    System.out.println("Beställning lagd!");
    purchase++;
    rs  = stmt.executeQuery("SELECT köpnr from beställning");
    while (rs.next()){
        int letsgo = rs.getInt("köpnr");
        System.out.println(letsgo);
    }



        selectAllProducts();

}



    public void setupDatabaseConnection() {

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("src/Properties.properties"));

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT namn from produkt");

            while (rs.next()) {
                String temp = rs.getString("namn");
                System.out.println(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void LogIn() throws SQLException {




//        System.out.println("Välkommen till Shoeshop!");
//        System.out.println("Användarnamn?");
//        username = in.next();
//        System.out.println("Lösenord?");
//        password = in.next();

        connectToAndQueryDatabase("daniel", "daniel");


    }


}
