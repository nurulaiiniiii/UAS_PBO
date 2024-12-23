package model;

import interfaces.MeterOperation;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Implementasi interface dalam PencatatanMeter.java 
 * Kelas PencatatanMeter mengimplementasikan semua method dari interface MeterOperation
 */
public class PencatatanMeter extends MeterListrik implements MeterOperation {  // Kelas Anak
    private double tarif;
    private SimpleDateFormat dateFormatter;

    public PencatatanMeter(String noPelanggan, String nama, double tarif) {
        super(noPelanggan, nama);
        this.tarif = tarif;
        this.dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    @Override
    public void bacaMeter(double pembacaanBaru) { // Implementasi method
        try {
            if (pembacaanBaru < this.pembacaanSekarang) {
                throw new IllegalArgumentException("Pembacaan baru tidak boleh lebih kecil dari pembacaan sebelumnya");
            }
            this.pembacaanSebelum = this.pembacaanSekarang;
            this.pembacaanSekarang = pembacaanBaru;
            this.tanggalBaca = new Date();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public void setPembacaanSekarang(double pembacaanBaru) {
        try {
            if (pembacaanBaru < this.pembacaanSebelum) {
                throw new IllegalArgumentException("Pembacaan baru tidak boleh lebih kecil dari pembacaan sebelumnya");
            }
            this.pembacaanSekarang = pembacaanBaru;
            this.tanggalBaca = new Date(); // Update tanggal baca
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    /* Contoh perhitungan matematika dalam PencatatanMeter.java 
    * Menghitung pemakaian listrik
    */
    @Override
    public double hitungPemakaian() {
        return pembacaanSekarang - pembacaanSebelum;
    }

    @Override
    public double hitungBiaya() {
        return hitungPemakaian() * tarif;
    }

    /* Manipulasi Tanggal 
    * Memformat tanggal ke format yang diinginkan
    */
    public String getFormattedDate() {
        return dateFormatter.format(tanggalBaca);
    }

    // Getters
    public double getTarif() {
        return tarif;
    }

    /* Manipulasi String dalam PencatatanMeter.java 
    * Method toString untuk mengubah data objek menjadi String
    */
    @Override
    public String toString() {
        return String.format("No Pelanggan: %s, Nama: %s, Pembacaan: %.2f, Tanggal: %s",
                getNoPelanggan(), getNama(), getPembacaanSekarang(), getFormattedDate());
    }
}