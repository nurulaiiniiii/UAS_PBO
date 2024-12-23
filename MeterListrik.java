package model;

import java.util.Date;

/**
 * Kelas dasar (superclass) untuk pencatatan meter listrik
 * Menyimpan informasi dasar tentang pelanggan dan pembacaan meter
 */
public class MeterListrik { // Kelas Induk
    // Atribut-atribut dasar meter listrik
    protected String noPelanggan;     // Nomor identifikasi pelanggan
    protected String nama;            // Nama pelanggan
    protected double pembacaanSebelum;// Angka meter pembacaan sebelumnya
    protected double pembacaanSekarang;// Angka meter pembacaan saat ini
    protected Date tanggalBaca;       // Tanggal pembacaan meter
    
    /**
     * Constructor untuk inisialisasi data meter listrik
     * @param noPelanggan Nomor identifikasi pelanggan
     * @param nama Nama pelanggan
     */
    public MeterListrik(String noPelanggan, String nama) {
        this.noPelanggan = noPelanggan;
        this.nama = nama;
        this.pembacaanSebelum = 0;
        this.pembacaanSekarang = 0;
        this.tanggalBaca = new Date();
    }

    // Getter dan Setter
    public String getNoPelanggan() {
        return noPelanggan;
    }

    public String getNama() {
        return nama;
    }

    public double getPembacaanSebelum() {
        return pembacaanSebelum;
    }

    public double getPembacaanSekarang() {
        return pembacaanSekarang;
    }

    public Date getTanggalBaca() {
        return tanggalBaca;
    }
}
