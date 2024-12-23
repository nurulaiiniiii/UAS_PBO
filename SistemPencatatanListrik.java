package main;

// Import statement untuk kelas-kelas yang diperlukan
import model.PencatatanMeter; // Kelas yang merepresentasikan pencatatan meter
import database.DatabaseManager; // Kelas untuk mengelola operasi database
import java.util.List; // Mengimpor List dari framework koleksi
import java.util.Scanner; // Mengimpor Scanner untuk input pengguna
import java.util.Random;
import java.util.InputMismatchException; // Mengimpor exception untuk kesalahan input

public class SistemPencatatanListrik {
    // Objek Scanner untuk membaca input pengguna
    private static final Scanner scanner = new Scanner(System.in); 
    // Objek DatabaseManager untuk menangani operasi database (CRUD)
    private static final DatabaseManager db = new DatabaseManager();
    private static int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
         // Cek login sebelum masuk ke menu utama
         if (!login()) {
            System.out.println("Program berhenti karena gagal login.");
            System.exit(1);
        }
        // Perulangan untuk terus menampilkan menu hingga pengguna memutuskan untuk keluar
        while(true) {
            /* Penanganan exception dalam SistemPencatatanListrik.java 
            * Menangani kesalahan input dari pengguna
            */
            try {
                tampilkanMenu();
                int pilihan = getPilihan();
                
                /* Contoh percabangan dalam SistemPencatatanListrik.java 
                * Switch case untuk memilih menu
                */
                switch(pilihan) {
                    case 1 -> inputPelangganBaru();
                    case 2 -> inputPembacaanMeter();
                    case 3 -> lihatSemuaData();
                    case 4 -> updatePembacaanMeter();
                    case 5 -> hapusDataPelanggan();
                    case 0 -> {
                        System.out.println("Terima kasih telah menggunakan sistem");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                // Penanganan exception untuk input yang tidak valid
                System.out.println("Error: Input tidak valid! Mohon masukkan angka.");
                scanner.nextLine(); // Clear buffer
            } catch (Exception e) {
                // Penanganan exception umum
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private static boolean login() {
        while (loginAttempts < MAX_ATTEMPTS) {
            System.out.println("\n=== SISTEM LOGIN ===");
            
            // Generate dan tampilkan CAPTCHA
            String captcha = generateCaptcha();
            System.out.println("CAPTCHA: " + captcha);
            
            // Input user
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Masukkan CAPTCHA: ");
            String userCaptcha = scanner.nextLine();
            
            // Verifikasi CAPTCHA
            if (!captcha.equals(userCaptcha)) {
                System.out.println("CAPTCHA salah! Silakan coba lagi.");
                loginAttempts++;
                System.out.println("Sisa percobaan: " + (MAX_ATTEMPTS - loginAttempts));
                continue;
            }
            
            // Verifikasi username dan password sederhana
            // Dalam kasus ini menggunakan kredensial hardcoded
            // Dalam praktik nyata, sebaiknya menggunakan database
            if (username.equals("admin") && password.equals("admin123")) {
                System.out.println("Login berhasil!");
                return true;
            } else {
                System.out.println("Username atau password salah!");
                loginAttempts++;
                System.out.println("Sisa percobaan: " + (MAX_ATTEMPTS - loginAttempts));
            }
        }
        
        System.out.println("Anda telah melebihi batas percobaan login!");
        return false;
    }

    private static String generateCaptcha() {
        // Generate CAPTCHA dengan kombinasi huruf dan angka
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        
        // Membuat CAPTCHA sepanjang 6 karakter
        for (int i = 0; i < 6; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return captcha.toString();
    }

    // Metode untuk menampilkan opsi menu
    private static void tampilkanMenu() {
        System.out.println("\n=== SISTEM PENCATATAN METER LISTRIK ===");
        System.out.println("1. Input Data Pelanggan Baru");
        System.out.println("2. Input Pembacaan Meter");
        System.out.println("3. Lihat Semua Data");
        System.out.println("4. Update Pembacaan Meter");
        System.out.println("5. Hapus Data Pelanggan");
        System.out.println("0. Keluar");
    }

    // Metode untuk mendapatkan pilihan penggun
    private static int getPilihan() {
        System.out.print("Pilih menu (0-5): ");
        return scanner.nextInt();
    }

    // Metode untuk input data pelanggan baru
    private static void inputPelangganBaru() {
        try {
            scanner.nextLine(); // Clear buffer
            System.out.println("\n-- Input Data Pelanggan Baru --");
            
            System.out.print("Nomor Pelanggan: ");
            String noPelanggan = scanner.nextLine();
            
            System.out.print("Nama Pelanggan: ");
            String nama = scanner.nextLine();
            
            System.out.print("Tarif per kWh: ");
            double tarif = scanner.nextDouble();
            
            // Membuat objek PencatatanMeter baru (penggunaan konstruktor)
            PencatatanMeter meter = new PencatatanMeter(noPelanggan, nama, tarif);
            
            System.out.print("Masukkan pembacaan meter awal: ");
            double bacaAwal = scanner.nextDouble();
            meter.bacaMeter(bacaAwal);
            
            db.tambahPencatatan(meter);
            
        } catch (InputMismatchException e) {
            // Penanganan exception untuk input yang tidak valid
            throw new InputMismatchException("Input tidak valid! Mohon periksa format input.");
        }
    }
    // Metode untuk melihat semua data
    private static void lihatSemuaData() {
        System.out.println("\n-- Data Semua Pencatatan --");
        List<PencatatanMeter> semuaData = db.getAllPencatatan();
        
        if (semuaData.isEmpty()) {
            System.out.println("Tidak ada data pencatatan.");
            return;
        }
        
        for (PencatatanMeter meter : semuaData) {
            System.out.println(meter.toString());
            System.out.println("Pemakaian: " + meter.hitungPemakaian() + " kWh");
            System.out.println("Biaya: Rp. " + meter.hitungBiaya());
            System.out.println("-------------------");
        }
    }
    // Metode untuk update pembacaan meter
    private static void updatePembacaanMeter() {
        try {
            scanner.nextLine(); // Clear buffer
            System.out.println("\n-- Update Pembacaan Meter --");
            
            System.out.print("Masukkan nomor pelanggan: ");
            String noPelanggan = scanner.nextLine();
            
            System.out.print("Masukkan pembacaan meter baru: ");
            double pembacaanBaru = scanner.nextDouble();
            
            if (pembacaanBaru < 0) {
                throw new IllegalArgumentException("Pembacaan meter tidak boleh negatif!");
            }
            
            db.updatePembacaan(noPelanggan, pembacaanBaru);
            
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Input tidak valid! Mohon periksa format input.");
        }
    }

    // Metode untuk menghapus data yang tidak diperlukan
    private static void hapusDataPelanggan() {
        scanner.nextLine(); // Clear buffer
        System.out.println("\n-- Hapus Data Pelanggan --");
        
        System.out.print("Masukkan nomor pelanggan yang akan dihapus: ");
        String noHapus = scanner.nextLine();
        
        db.hapusPencatatan(noHapus);
    }

    // Metode untuk input pembacaan meter baru
    private static void inputPembacaanMeter() {
        try {
            scanner.nextLine(); // Clear buffer
            System.out.println("\n-- Input Pembacaan Meter --");
            
            System.out.print("Masukkan nomor pelanggan: ");
            String noPelangganInput = scanner.nextLine();
            
            // Ambil data pelanggan dari database
            List<PencatatanMeter> allData = db.getAllPencatatan();
            PencatatanMeter meterData = allData.stream()
                .filter(m -> m.getNoPelanggan().equals(noPelangganInput))
                .findFirst()
                .orElse(null);
            
            if (meterData != null) {
                System.out.println("Pembacaan sebelumnya: " + meterData.getPembacaanSekarang());
                System.out.print("Masukkan angka meter baru: ");
                double bacaBaru = scanner.nextDouble();
                scanner.nextLine(); // clear buffer
                
                if (bacaBaru < meterData.getPembacaanSekarang()) {
                    throw new IllegalArgumentException("Pembacaan baru tidak boleh lebih kecil dari pembacaan sebelumnya!");
                }
                
                // Update pembacaan meter
                meterData.setPembacaanSekarang(bacaBaru);
                db.updatePembacaan(noPelangganInput, bacaBaru);
                
                // Tampilkan informasi pemakaian
                System.out.println("\nInformasi Pemakaian:");
                System.out.println("Pemakaian: " + meterData.hitungPemakaian() + " kWh");
                System.out.println("Biaya: Rp. " + meterData.hitungBiaya());
                
            } else {
                throw new IllegalArgumentException("Nomor pelanggan tidak ditemukan.");
            }
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Input tidak valid! Mohon masukkan angka yang benar.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}