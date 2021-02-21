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
            con = DriverManager.getConnection("jdbc:mysql://localhost/database_webshop", username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT namn from produkt");
        while (rs.next()) {
            String x = rs.getString("namn");
            System.out.println(x);
        }


    }




    public void setupDatabaseConnection(){

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("src/Properties.properties"));

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(p.getProperty("connectionString"), p.getProperty("name"), p.getProperty("password"));

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT namn from produkt");

            while (rs.next()){
                String temp = rs.getString("namn");
                System.out.println(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void LogIn() throws SQLException {

        Scanner in = new Scanner(System.in);
        String username;
        String password;

//        System.out.println("Välkommen till Shoeshop!");
//        System.out.println("Användarnamn?");
//        username = in.next();
//        System.out.println("Lösenord?");
//        password = in.next();

connectToAndQueryDatabase("danne", "danne");


    }


}
