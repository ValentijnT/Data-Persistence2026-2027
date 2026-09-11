package domain;

import java.time.LocalDate;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsel, String achternaam, LocalDate geboortedatum) {
        this.id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() { return id; }
    public String getVoorletters() { return voorletters; }
    public String getTussenvoegsel() { return tussenvoegsel; }
    public String getAchternaam() { return achternaam; }
    public LocalDate getGeboortedatum() { return geboortedatum; }
    public Adres getAdres() { return adres; }


    public void setId(int id) { this.id = id; }
    public void setVoorletters(String voorletters) { this.voorletters = voorletters; }
    public void setTussenvoegsel(String tussenvoegsel) { this.tussenvoegsel = tussenvoegsel; }
    public void setAchternaam(String achternaam) { this.achternaam = achternaam; }
    public void setGeboortedatum(LocalDate geboortedatum) { this.geboortedatum = geboortedatum; }
    public void setAdres(Adres adres) { this.adres = adres; }


    public String getNaam(){
        String tv = (tussenvoegsel != null) ? tussenvoegsel + " " : "";
        return voorletters + ". " + tv + achternaam;
    }

    public String toString() {
        String adresString = (adres != null) ? ", Adres: " + adres : "";
        return  "Reiziger {#" + getId() + ": " + getNaam() + " (" + geboortedatum + ")" + adresString + "}";
    }

}
