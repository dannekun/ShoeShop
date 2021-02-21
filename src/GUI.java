import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
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
    importSQLData imp = new importSQLData();


    Connection con = null;
    ResultSet rs;
    Statement stmt;

    Scanner in = new Scanner(System.in);

    int purchase = 0;

    String username;
    String password;

    List<Kund> kunder = imp.getKunder();
    List<Produkt> produkter = imp.getProdukter();

    Kund loggedInKund = new Kund();

    List<Produkt> produkterILager = new ArrayList<>();

    public void refresh() throws SQLException {
        imp.updateData();
        kunder = imp.getKunder();
        produkter = imp.getProdukter();

    }


    public void startServer() throws SQLException {
        connectToAndQueryDatabase("daniel", "daniel");
    }

    public void connectToAndQueryDatabase(String username, String
            password) throws SQLException {


        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/database_webshop", username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stmt = con.createStatement();
        System.out.println("Välkommen till ShoeShop!");
        logInUI();

    }


    public void mainMeny() throws SQLException {
        purchase = 0;
        int alternativ;
        spaaaace();
        System.out.println("Välkommen " + loggedInKund.getFörnamn()+ "!");
        spaaaace();
        System.out.println("Välj ett alternativ!");
        System.out.println("1. Gör en ny beställning");
        System.out.println("2. Kolla tidigare beställningar");

        alternativ = in.nextInt();

        switch (alternativ){
            case 1:
                görBeställning();
                break;
            case 2:
                kollaBeställningar();
                break;

        }

    }

    public void görBeställning() throws SQLException {
        selectAllProducts();
    }

    public void kollaBeställningar(){

    }

    public void logInUI() throws SQLException {
        boolean loggedin = false;

        refresh();

        System.out.println("Användarnamn?");
        username = in.next();
        System.out.println("Lösenord");
        password = in.next();


        for (Kund kund : kunder) {
            if (kund.getFörnamn().equalsIgnoreCase(username) && kund.getLösenord().equalsIgnoreCase(password)){
                loggedin = true;
            loggedInKund = kund;
            }
        }

        if (loggedin){
            mainMeny();

        }else {
            System.out.println("Inloggning misslyckades! Försök igen!");
            logInUI();
        }
        selectAllProducts();
    }


    public void selectAllProducts() throws SQLException {
        int product;

        produkterILager.clear();

        for (Produkt temp : produkter){
            if (temp.getLager() > 0) {
                produkterILager.add(temp);
            }
        }

        int counter = 0;
        System.out.println("0. Tillbaka");
        for (Produkt tempish : produkterILager){
            counter++;
            System.out.println(counter + ". " + tempish.getNamn());
        }

        product = Integer.parseInt(in.next());

        if (product == 0){
            mainMeny();
        }else {
            int ship = produkterILager.get(product-1).getId();

            showProductInfo(ship);
        }

    }

    public void showProductInfo(int id) throws SQLException {
        Produkt currentProduct = new Produkt();

        for (Produkt find : produkterILager){
            if (find.getId() == id){
                currentProduct = find;
            }
        }

        System.out.println(currentProduct.getNamn());
        System.out.println("Pris: "+currentProduct.getPris()+ "kr");
        System.out.println("Storlek: "+currentProduct.getStorlek());
        System.out.println("Färg: "+currentProduct.getFärg());
        System.out.println("Lagerstatus: " + currentProduct.getLager() + "st");
        System.out.println("0. Tillbaka");
        System.out.println("1. Köp");

        String choice = in.next();

        int choicee = Integer.parseInt(choice);

        if (choicee == 0) {
            selectAllProducts();
        }else if (choicee == 1){
            purchaseProduct(currentProduct.getId());
        }

    }

    public void purchaseProduct(int produktid) throws SQLException {
        CallableStatement stm;



            if (purchase == 0) {
                stm = con.prepareCall("call AddToCart((SELECT id from kund where förnamn = ?), (Select max(köpnr) from beställning)+10 ,?)");
                stm.setString(1, loggedInKund.getFörnamn());
                stm.setInt(2, produktid);
                stm.execute();

            } else {
                stm = con.prepareCall("call AddToCart((SELECT id from kund where förnamn = ?), (Select max(köpnr) from beställning),?)");
                stm.setString(1, loggedInKund.getFörnamn());
                stm.setInt(2, produktid);
                stm.execute();

            }
            System.out.println("Beställning lagd!");
            purchase = 1;

        imp.updateData();

        System.out.println("0. Tillbaka");
        System.out.println("1. Köp igen");

        String choice = in.next();

        int choicee = Integer.parseInt(choice);

        if (choicee == 0) {
            showProductInfo(produktid);
        }else if (choicee == 1){
            purchaseProduct(produktid);
        }



    }

    public int fullCounterInt(int produktid) throws SQLException {

        int i = 0;
        int y = 0;

        rs = stmt.executeQuery("select lager from produkt");

        while (rs.next()) {
            if (i == produktid - 1) {
                i++;
                y = rs.getInt("lager");
            } else {
                i++;
                rs.getInt("lager");
            }
        }

        return y;
    }

    public void fullCounterString() throws SQLException {

        rs = stmt.executeQuery("select id, förnamn,efternamn,adress, mail,telefonnummer,ortid,lösenord from kund");



        while (rs.next()) {
            Kund temp = new Kund();
            temp.setId(rs.getInt("id"));
            temp.setFörnamn(rs.getString("förnamn"));
            temp.setEfternamn(rs.getString("efternamn"));
            temp.setAddress(rs.getString("adress"));
            temp.setMail(rs.getString("mail"));
            temp.setTelefonnummer(rs.getInt("telefonnummer"));
            temp.setOrtID(rs.getInt("ortid"));
            temp.setLösenord(rs.getString("lösenord"));

            kunder.add(temp);
        }
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
    public void spaaaace(){
        System.out.println("\n------------------------------------------------\n");
    }


}
