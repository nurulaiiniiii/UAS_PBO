package interfaces; 
/**
 * Interface yang mendefinisikan operasi-operasi dasar untuk pencatatan meter listrik
 * Interface ini akan diimplementasikan oleh kelas yang menangani pencatatan meter
 */
public interface MeterOperation {
    // Method untuk mencatat pembacaan meter baru
    void bacaMeter(double pembacaanBaru);
    // Method untuk menghitung jumlah pemakaian listrik
    double hitungPemakaian();
    // Method untuk menghitung biaya berdasarkan pemakaian
    double hitungBiaya();
}