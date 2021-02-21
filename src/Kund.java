/**
 * Created by Daniel Bojic
 * Date: 2021-02-21
 * Time: 14:19
 * Project: Inlämningsuppgift 2
 * Copyright: MIT
 */
public class Kund {

    Kund(){

    }


    private int id;
    private String förnamn;
    private  String efternamn;
    private String address;
    private String mail;
    private int telefonnummer;
    private int ortID;
    private String lösenord;

    public Kund(int id, String förnamn, String efternamn, String address, String mail, int telefonnummer, int ortID, String lösenord) {
        this.id = id;
        this.förnamn = förnamn;
        this.efternamn = efternamn;
        this.address = address;
        this.mail = mail;
        this.telefonnummer = telefonnummer;
        this.ortID = ortID;
        this.lösenord = lösenord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFörnamn() {
        return förnamn;
    }

    public void setFörnamn(String förnamn) {
        this.förnamn = förnamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(int telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public int getOrtID() {
        return ortID;
    }

    public void setOrtID(int ortID) {
        this.ortID = ortID;
    }

    public String getLösenord() {
        return lösenord;
    }

    public void setLösenord(String lösenord) {
        this.lösenord = lösenord;
    }
}
