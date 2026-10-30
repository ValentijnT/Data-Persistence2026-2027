package domain;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<OVChipkaart> ovchipkaarten = new ArrayList<>();

    public Product() {}

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProduct_nummer() {return product_nummer;}
    public String getNaam() {return naam;}
    public String getBeschrijving() {return beschrijving;}
    public double getPrijs() {return prijs;}
    public List<OVChipkaart> getOvchipkaarten() {return ovchipkaarten;}

    public void setProduct_nummer(int product_nummer) {this.product_nummer = product_nummer;}
    public void setNaam(String naam) {this.naam = naam;}
    public void setBeschrijving(String beschrijving) {this.beschrijving = beschrijving;}
    public void setPrijs(double prijs) {this.prijs = prijs;}
    public void setOvchipkaarten (List<OVChipkaart> ovchipkaarten) {this.ovchipkaarten = ovchipkaarten;}

    public void addOVChipkaart(OVChipkaart ov) {
        if (!ovchipkaarten.contains(ov)) {
            ovchipkaarten.add(ov);
        }
    }

    public void removeOVChipkaart(OVChipkaart ov) {
        ovchipkaarten.remove(ov);
    }

    public String toString(){
        return "Product {#" + product_nummer +
                ": naam: " + naam +
                ", beschrijving: " + beschrijving +
                ", prijs: €" + String.format("%.2f", prijs) + "}";
    }
}
