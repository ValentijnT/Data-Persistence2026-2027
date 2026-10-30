package daopsql;

import dao.OVChipkaartDAO;
import dao.ProductDAO;
import domain.OVChipkaart;
import domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {

    private Connection conn;
    private OVChipkaartDAO ovdao;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setOVChipkaartDAO(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }

    @Override
    public boolean save(Product product) {
        String sql = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        String link = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try(
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstLink = conn.prepareStatement(link)
        ){
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.executeUpdate();

            //save link table
            for (OVChipkaart ov : product.getOvchipkaarten()) {
                pstLink.setInt(1, ov.getKaartNummer());
                pstLink.setInt(2, product.getProduct_nummer());
                pstLink.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        String linkDelete = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String linkInsert = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try(
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstDelete = conn.prepareStatement(linkDelete);
                PreparedStatement pstInsert = conn.prepareStatement(linkInsert)
        ) {
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getProduct_nummer());
            pst.executeUpdate();

            //Delete link
            pstDelete.setInt(1, product.getProduct_nummer());
            pstDelete.executeUpdate();

            for (OVChipkaart ov : product.getOvchipkaarten()) {
                pstInsert.setInt(1, ov.getKaartNummer());
                pstInsert.setInt(2, product.getProduct_nummer());
                pstInsert.executeUpdate();
            }

            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        String linkDelete = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String sql = "DELETE FROM product WHERE product_nummer = ?";

        try(
                PreparedStatement pstDelete = conn.prepareStatement(linkDelete);
                PreparedStatement pst = conn.prepareStatement(sql);
        ){
            //delete links
            pstDelete.setInt(1, product.getProduct_nummer());
            pstDelete.executeUpdate();

            //delete product
            pst.setInt(1, product.getProduct_nummer());
            pst.executeUpdate();

            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ov) {
        List<Product> producten = new ArrayList<>();
        String sql =
                "SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs " +
                        "FROM product p " +
                        "JOIN ov_chipkaart_product op ON p.product_nummer = op.product_nummer " +
                        "WHERE op.kaart_nummer = ?";

        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, ov.getKaartNummer());

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs")
                );

                product.addOVChipkaart(ov);
//                ov.addProduct(product);

                producten.add(product);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return producten;
    }

    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs")
                );
                producten.add(p);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return producten;
    }
}
