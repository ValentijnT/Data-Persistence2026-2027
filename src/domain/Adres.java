package domain;

public class Adres {
    private int adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Reiziger reiziger;

    public Adres() {}

    public Adres(int adres_id, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger){
        this.adres_id = adres_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public int getAdres_id() { return adres_id; }
    public String getPostcode() { return postcode; }
    public String getHuisnummer() { return huisnummer; }
    public String getStraat() { return straat; }
    public String getWoonplaats() { return woonplaats; }
    public Reiziger getReiziger() { return reiziger; }

    public void setAdres_id(int adres_id) { this.adres_id = adres_id; }
    public void setPostcode(String postcode) { this.postcode = postcode; }
    public void setHuisnummer(String huisnummer) { this.huisnummer = huisnummer; }
    public void setStraat(String straat) { this.straat = straat; }
    public void setWoonplaats(String woonplaats) { this.woonplaats = woonplaats; }
    public void setReiziger(Reiziger reiziger) { this.reiziger = reiziger; }

    public String toString(){
        String reizigerInfo = (reiziger != null) ? " Reiziger id: " +  reiziger.getId() : "Reiziger: null";
        return "{#" + adres_id + " " + straat + " " + huisnummer + ", " + postcode + ", " + woonplaats + ", " + reizigerInfo + "}";
    }


}
