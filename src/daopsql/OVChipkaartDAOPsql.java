package daopsql;

import dao.OVChipkaartDAO;
import dao.ReizigerDAO;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;
    private ProductDAOPsql pdao;

    public OVChipkaartDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public void setReizigerDAO(ReizigerDAO rdao) {this.rdao = rdao;}

    public void setProductDAO(ProductDAOPsql pdao) {this.pdao = pdao;}

    @Override
    public boolean save(OVChipkaart ov) {
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        String linkInsert = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try (
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstLinkInsert = conn.prepareStatement(linkInsert)
        ) {
            pst.setInt(1, ov.getKaartNummer());
            pst.setDate(2, java.sql.Date.valueOf(ov.getGeldigTot()));
            pst.setInt(3, ov.getKlasse());
            pst.setDouble(4, ov.getSaldo());
            pst.setInt(5, ov.getReiziger().getId());
            boolean saved = pst.executeUpdate() > 0;

            if (saved) {
                for (Product p : ov.getProducten()) {
                    pstLinkInsert.setInt(1, ov.getKaartNummer());
                    pstLinkInsert.setInt(2, p.getProduct_nummer());
                    pstLinkInsert.executeUpdate();
                }
            }

            return saved;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        String sql = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        String linkDelete = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        String linkInsert = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try(
                PreparedStatement pst = conn.prepareStatement(sql);
                PreparedStatement pstLinkDelete = conn.prepareStatement(linkDelete);
                PreparedStatement pstLinkInsert = conn.prepareStatement(linkInsert)
        ){
            pst.setDate(1, java.sql.Date.valueOf(ovChipkaart.getGeldigTot()));
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getId());
            pst.setInt(5, ovChipkaart.getKaartNummer());
            boolean updated = pst.executeUpdate() > 0;

            if (updated) {
                pstLinkDelete.setInt(1, ovChipkaart.getKaartNummer());
                pstLinkDelete.executeUpdate();

                for (Product p : ovChipkaart.getProducten()) {
                    pstLinkInsert.setInt(1, ovChipkaart.getKaartNummer());
                    pstLinkInsert.setInt(2, p.getProduct_nummer());
                    pstLinkInsert.executeUpdate();
                }
            }

            return updated;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        String linkDelete = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try(
                PreparedStatement pstLinkDelete = conn.prepareStatement(linkDelete);
                PreparedStatement pst = conn.prepareStatement(sql)
        ){
            pstLinkDelete.setInt(1, ovChipkaart.getKaartNummer());
            pstLinkDelete.executeUpdate();

            pst .setInt(1, ovChipkaart.getKaartNummer());

            return pst.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger r) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();

        String sql = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE reiziger_id = ?";
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, r.getId());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    OVChipkaart ovChipkaart = new OVChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            r
                    );
                    if(pdao != null) {
                        ovChipkaart.setProducten(pdao.findByOVChipkaart(ovChipkaart));
                    }
                    ovChipkaarten.add(ovChipkaart);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        String sql = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart";

        try(PreparedStatement pst = conn.prepareStatement(sql)){
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int reiziger_id = rs.getInt("reiziger_id");
                    Reiziger reiziger = rdao.findById(reiziger_id);
                    OVChipkaart ov = new OVChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            reiziger
                    );

                    if (pdao != null) {
                        ov.setProducten(pdao.findByOVChipkaart(ov));
                    }

                    ovChipkaarten.add(ov);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ovChipkaarten;
    }
}
