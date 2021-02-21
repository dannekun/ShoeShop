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
    public void connectToAndQueryDatabase(String username, String
            password) throws SQLException {
        Statement stmt;

        try {
            con = DriverManager.getConnection("jdbc:myDriver:database_webshop?serverTimezone=UTC&useSSL=false", username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT förnamn from kund");
        while (rs.next()) {
            int x = rs.getInt("förnamn");
            String s = rs.getString("b");
            float f = rs.getFloat("c");
        }
    }


    public void setupDatabaseConnection(){

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("Properties.properties"));

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT förnamn from kund");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void Mainmeny() throws SQLException {

        Scanner in = new Scanner(System.in);
        String temp1;
        String temp2;

        System.out.println("Välkommen till Shoeshop!");
        System.out.println("Användarnamn?");
        temp1 = in.next();
        System.out.println("Lösenord?");
        temp2 = in.next();

connectToAndQueryDatabase("root", "lolica123");



    }


}
