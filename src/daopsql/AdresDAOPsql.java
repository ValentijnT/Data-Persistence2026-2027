package daopsql;

import dao.AdresDAO;
import domain.Adres;
import domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;
    private ReizigerDAOPsql rdao;

    public AdresDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public void setReizigerDAO(ReizigerDAOPsql rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, adres.getAdres_id());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        String sql = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger().getId());
            pst.setInt(6, adres.getAdres_id());
            return pst.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, adres.getAdres_id());
            return pst.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger r) {
        String sql = "SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id FROM adres WHERE reiziger_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, r.getId());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        r
                );

                r.setAdres(adres);
                return adres;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        List<Adres> adressen = new ArrayList<>();
        String sql = "SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id FROM adres";
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int reiziger_id = rs.getInt("reiziger_id");
                Reiziger reiziger = rdao.findById(reiziger_id);
                adressen.add(new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        reiziger
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return adressen;
    }
}
