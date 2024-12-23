package database;

import model.PencatatanMeter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/meter_listrik";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE (Membuat data baru)
    public void tambahPencatatan(PencatatanMeter meter) {
        String sql = "INSERT INTO pencatatan (no_pelanggan, nama, tarif, pembacaan_sekarang) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, meter.getNoPelanggan());
            pstmt.setString(2, meter.getNama());
            pstmt.setDouble(3, meter.getTarif());
            pstmt.setDouble(4, meter.getPembacaanSebelum());
            pstmt.setDouble(4, meter.getPembacaanSekarang());
            
            pstmt.executeUpdate();
            System.out.println("Data berhasil ditambahkan!");
            
        } catch (SQLException e) {
            System.err.println("Error saat menambah data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /* Penggunaan Collection (List) dalam DatabaseManager.java 
    * Menggunakan ArrayList untuk menyimpan data pencatatan
    */
    // READ (Membaca data)
    public List<PencatatanMeter> getAllPencatatan() {
        List<PencatatanMeter> pencatatanList = new ArrayList<>();
        String sql = "SELECT * FROM pencatatan";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                PencatatanMeter meter = new PencatatanMeter(
                    rs.getString("no_pelanggan"),
                    rs.getString("nama"),
                    rs.getDouble("tarif")
                );
                meter.setPembacaanSekarang(rs.getDouble("pembacaan_sekarang"));
                pencatatanList.add(meter);
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        return pencatatanList;
    }

    // UPDATE (Memperbarui data)
    public void updatePembacaan(String noPelanggan, double pembacaanBaru) {
        // Pertama, ambil pembacaan_sekarang yang ada untuk dijadikan pembacaan_sebelum
        String selectSql = "SELECT pembacaan_sekarang FROM pencatatan WHERE no_pelanggan = ?";
        String updateSql = "UPDATE pencatatan SET pembacaan_sebelum = ?, pembacaan_sekarang = ? WHERE no_pelanggan = ?";
        
        try (Connection conn = getConnection()) {
            // Ambil pembacaan sekarang
            double pembacaanSebelum;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, noPelanggan);
                ResultSet rs = selectStmt.executeQuery();
                
                if (!rs.next()) {
                    throw new IllegalArgumentException("No pelanggan tidak ditemukan!");
                }
                pembacaanSebelum = rs.getDouble("pembacaan_sekarang");
            }
            
            // Update dengan pembacaan baru
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setDouble(1, pembacaanSebelum);
                updateStmt.setDouble(2, pembacaanBaru);
                updateStmt.setString(3, noPelanggan);
                
                int affectedRows = updateStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Data berhasil diupdate!");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PencatatanMeter getPencatatanByNoPelanggan(String noPelanggan) {
        String sql = "SELECT * FROM pencatatan WHERE no_pelanggan = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, noPelanggan);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                PencatatanMeter meter = new PencatatanMeter(
                    rs.getString("no_pelanggan"),
                    rs.getString("nama"),
                    rs.getDouble("tarif")
                );
                meter.bacaMeter(rs.getDouble("pembacaan_sekarang")); // Ini akan set pembacaan_sebelum dan pembacaan_sekarang
                return meter;
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        return null;
    }

    // DELETE (Menghapus data)
    public void hapusPencatatan(String noPelanggan) {
        String sql = "DELETE FROM pencatatan WHERE no_pelanggan = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, noPelanggan);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Data berhasil dihapus!");
            } else {
                System.out.println("No pelanggan tidak ditemukan!");
            }
            
        } catch (SQLException e) {
            System.err.println("Error saat menghapus data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
