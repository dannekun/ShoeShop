import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Bojic
 * Date: 2021-02-21
 * Time: 15:17
 * Project: Inlämningsuppgift 2
 * Copyright: MIT
 */
public class importSQLData {

    Connection con = null;
    ResultSet rs;
    Statement stmt;

    List<Kund> kunder = new ArrayList<>();
    List<Produkt> produkter = new ArrayList<>();
    List<Beställning> beställningar = new ArrayList<>();
    List<Skickar> skickar = new ArrayList<>();
    List<Ort> orten = new ArrayList<>();
    List<Betyg> betygen = new ArrayList<>();
    List<Märke> märken = new ArrayList<>();
    List<Kategori> kategorier = new ArrayList<>();
    List<Tillhör> tillhörs = new ArrayList<>();
    List<slutILager> slutILagers = new ArrayList<>();

    public List<Skickar> getSkickar() {
        return skickar;
    }

    public void setSkickar(List<Skickar> skickar) {
        this.skickar = skickar;
    }

    public List<Beställning> getBeställningar() {
        return beställningar;
    }

    public void setBeställningar(List<Beställning> beställningar) {
        this.beställningar = beställningar;
    }

    public List<Kund> getKunder() {
        return kunder;
    }

    public void setKunder(List<Kund> kunder) {
        this.kunder = kunder;
    }

    public List<Produkt> getProdukter() {
        return produkter;
    }

    public void setProdukter(List<Produkt> produkter) {
        this.produkter = produkter;
    }

    public void updateData() throws SQLException {

        connectToAndQueryDatabase("daniel", "daniel");

        updateKunder();
        updateProdukter();
        updateBeställning();
        updateSkickar();
        updateOrt();
        updateBetyg();
        updateMärke();
        updateKategori();
        updateTillhör();
        updateSlutILager();

    }

    public void updateKunder() throws SQLException {

        kunder.clear();

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

    public void updateBeställning() throws SQLException {

        beställningar.clear();

        rs = stmt.executeQuery("select id, köpnr,summa,datum, kundid from beställning");



        while (rs.next()) {
            Beställning temp = new Beställning();
            temp.setId(rs.getInt("id"));
            temp.setKöpnr(rs.getInt("köpnr"));
            temp.setSumma(rs.getInt("summa"));
            temp.setDatum(rs.getString("datum"));
            temp.setKundid(rs.getInt("kundid"));

            beställningar.add(temp);
        }

    }


    public void updateProdukter() throws SQLException {

        produkter.clear();

        rs = stmt.executeQuery("select id, namn,pris,storlek, färg,märkeid,lager from produkt");



        while (rs.next()) {
            Produkt temp = new Produkt();
            temp.setId(rs.getInt("id"));
            temp.setNamn(rs.getString("namn"));
            temp.setPris(rs.getInt("pris"));
            temp.setStorlek(rs.getInt("storlek"));
            temp.setFärg(rs.getString("färg"));
            temp.setMärkeid(rs.getInt("märkeid"));
            temp.setLager(rs.getInt("lager"));

            produkter.add(temp);
        }

    }

    public void updateSkickar() throws SQLException {

        skickar.clear();

        rs = stmt.executeQuery("select id, köpnr,produktid from skickar");



        while (rs.next()) {
            Skickar temp = new Skickar();
            temp.setId(rs.getInt("id"));
            temp.setKöpnr(rs.getInt("köpnr"));
            temp.setProduktid(rs.getInt("produktid"));


            skickar.add(temp);
        }

    }

    public void updateOrt() throws SQLException {

        orten.clear();

        rs = stmt.executeQuery("select id, namn from ort");



        while (rs.next()) {
            Ort temp = new Ort();
            temp.setId(rs.getInt("id"));
            temp.setNamn(rs.getString("namn"));


            orten.add(temp);
        }

    }

    public void updateBetyg() throws SQLException {
        betygen.clear();

        rs = stmt.executeQuery("select id, betygnr, omdöme from betyg");

        while (rs.next()) {
            Betyg temp = new Betyg();
            temp.setId(rs.getInt("id"));
            temp.setBetygnr(rs.getInt("betygnr"));
            temp.setOmdöme(rs.getString("omdöme"));

            betygen.add(temp);
        }

    }

    public void updateMärke() throws SQLException {
        märken.clear();

        rs = stmt.executeQuery("select id, namn from märke");

        while (rs.next()) {
            Märke temp = new Märke();
            temp.setId(rs.getInt("id"));
            temp.setNamn(rs.getString("namn"));

            märken.add(temp);
        }

    }

    public void updateKategori() throws SQLException {
        kategorier.clear();

        rs = stmt.executeQuery("select id, namn from kategori");

        while (rs.next()) {
            Kategori temp = new Kategori();
            temp.setId(rs.getInt("id"));
            temp.setNamn(rs.getString("namn"));

            kategorier.add(temp);
        }
    }

    public void updateTillhör() throws SQLException {
        tillhörs.clear();

        rs = stmt.executeQuery("select id, kategoriid,produktid from tillhör");

        while (rs.next()) {
            Tillhör temp = new Tillhör();
            temp.setId(rs.getInt("id"));
            temp.setKategoriID(rs.getInt("kategoriid"));
            temp.setProduktID(rs.getInt("produktid"));

            tillhörs.add(temp);
        }


    }

    public void updateSlutILager() throws SQLException {
        slutILagers.clear();

        rs = stmt.executeQuery("select id, datum,produktid from slutilager");

        while (rs.next()) {
            slutILager temp = new slutILager();
            temp.setId(rs.getInt("id"));
            temp.setDatum(rs.getString("kategoriid"));
            temp.setProduktid(rs.getInt("produktid"));

            slutILagers.add(temp);
        }



    }




    public void connectToAndQueryDatabase(String username, String
            password) throws SQLException {


        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/database_webshop", username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stmt = con.createStatement();


    }
}
