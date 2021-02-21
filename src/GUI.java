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
    List<Beställning> beställningar = imp.getBeställningar();

    Kund loggedInKund = new Kund();

    List<Produkt> produkterILager = new ArrayList<>();

    List<Produkt> varukorg= new ArrayList<>();

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

    public void kollaBeställningar() throws SQLException {

        refresh();

        List<Beställning> tempB = new ArrayList<>();

        for (Beställning be : beställningar){
            if (be.getKundid() == loggedInKund.getId()){
                tempB.add(be);
            }
        }

        List<Beställning> unikaID = new ArrayList<>();

        for (Beställning beID : tempB){
            if (beID.getKöpnr())


            if (!unikaID.contains(beID.getKöpnr())){
                unikaID.add(beID);
            }
        }

        for (int i = 0; i < unikaID.size(); i++) {

        }

        List<Beställning> finalTemp = new ArrayList<>();

        for (int i = 0; i < unikaID.size(); i++) {
            int p = 0;
            for (int j = 0; j < tempB.size(); j++) {
                if (unikaID.get(i).getKöpnr() == tempB.get(j).getKöpnr()){
                   p = p + tempB.get(j).getSumma();
                }

            }
            finalTemp.add(unikaID.get(i));
            finalTemp.get(i).setSumma(p);
        }

        System.out.println("Beställningar från: " + loggedInKund.getFörnamn());
        for (Beställning printOrder : finalTemp){
            spaaaace();
            System.out.println("OrderID: " + printOrder.getKöpnr());
            System.out.println("Summa: " + printOrder.getSumma());
            System.out.println("Datum: " + printOrder.getDatum());
            spaaaace();
        }

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
        System.out.println((produkterILager.size()+1)+ ". Varukorg" );

        product = Integer.parseInt(in.next());

        if (product == 0){
            mainMeny();
        }else if (product == (produkterILager.size() + 1)){
           varukorg();
        }else{
            int ship = produkterILager.get(product-1).getId();

            showProductInfo(ship);
        }

    }

    public void showProductInfo(int id) throws SQLException {
        Produkt currentProduct = new Produkt();
        int findProductToSell = 0;
        int foundIt = 0;
        for (Produkt find : produkterILager){
            if (find.getId() == id){
                foundIt =findProductToSell;
                currentProduct = find;
            }
            findProductToSell++;
        }

        System.out.println(currentProduct.getNamn());
        System.out.println("Pris: "+currentProduct.getPris()+ "kr");
        System.out.println("Storlek: "+currentProduct.getStorlek());
        System.out.println("Färg: "+currentProduct.getFärg());
        System.out.println("Lagerstatus: " + currentProduct.getLager() + "st");
        System.out.println("0. Tillbaka");
        System.out.println("1. Lägg till i varukorgen");

        String choice = in.next();

        int choicee = Integer.parseInt(choice);

        if (choicee == 0) {
            selectAllProducts();
        }else if (choicee == 1){
            varukorg.add(currentProduct);
            purchase++;
            System.out.println("Tillagd i varukorgen!");
            produkterILager.get(foundIt).setLager(produkterILager.get(foundIt).getLager()-1);
            if (produkterILager.get(foundIt).getLager() == 0){
                produkterILager.remove(foundIt);
                selectAllProducts();
            }else {
                showProductInfo(id);
            }


        }

    }

    public void varukorg() throws SQLException {
        int totalVarukorg = 0;
        if (purchase == 0){
            System.out.println("Varukorgen är tom!");
            selectAllProducts();
        }else {
            System.out.println("Produkter i varukorgen: ");
            for (Produkt produktIKorg: varukorg){
                System.out.println(produktIKorg.getNamn() + " " + produktIKorg.getPris()+ "kr");
                totalVarukorg = totalVarukorg + produktIKorg.getPris();
            }
            System.out.println("Totalt: " + totalVarukorg + "kr");
            System.out.println("0. Tillbaka");
            System.out.println("1. Köp");

            String next = in.next();
            int nextInt = Integer.parseInt(next);

            if (nextInt == 0){
                selectAllProducts();
            }else {
                purchaseProduct();
            }
        }



    }


    public int findMaxBeställningNr(){
        int tempNr = 0;

        for (Beställning best : beställningar){
            if (best.getKöpnr() > tempNr){
                tempNr = best.getKöpnr();
            }
        }
        return tempNr+1;
    }

    public void purchaseProduct() throws SQLException {
        CallableStatement stm;

        int maxNumber = findMaxBeställningNr();

        for (int i = 0; i < varukorg.size(); i++) {
            stm = con.prepareCall("call AddToCart((SELECT id from kund where förnamn = ?), ? ,?)");
            stm.setString(1, loggedInKund.getFörnamn());
            stm.setInt(2, maxNumber);
            stm.setInt(3, varukorg.get(i).getId());
            stm.execute();
        }



/*
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

 */
            System.out.println("Beställning lagd!");
            purchase = 1;

        imp.updateData();

        /*

        System.out.println("0. Tillbaka");
        System.out.println("1. Köp igen");

        String choice = in.next();

        int choicee = Integer.parseInt(choice);

        if (choicee == 0) {
            showProductInfo(produktid);
        }else if (choicee == 1){
            purchaseProduct(produktid);
        }


         */


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
