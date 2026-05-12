# Sistem Dashboard Laundry - MySQL Version

Aplikasi desktop untuk manajemen laundry menggunakan Java Swing dan MySQL database (XAMPP).

## Fitur

1. **Login System** - Autentikasi user dengan username dan password
2. **Data Pelanggan** - CRUD pelanggan dengan informasi lengkap
3. **Paket Layanan** - Manajemen paket layanan dengan harga dan durasi
4. **Cucian Masuk** - Input order cucian dengan kalkulasi otomatis
5. **Transaksi Pembayaran** - Proses pembayaran dengan berbagai metode
6. **Riwayat & Laporan** - Laporan lengkap dengan ringkasan statistik
7. **Manajemen User** - Tambah user baru untuk sistem

## Teknologi

- **Java 11+** - Bahasa pemrograman utama
- **Swing** - GUI framework
- **MySQL** - Database server (via XAMPP)
- **Maven** - Build tool dan dependency management
- **FlatLaf** - Modern Look and Feel

## Persyaratan Sistem

### Software yang Diperlukan:
1. **XAMPP** - Download dari https://www.apachefriends.org/
2. **Java JDK 11+** - Download dari Oracle atau OpenJDK
3. **NetBeans IDE** (opsional) - Untuk development
4. **Maven** (opsional) - Sudah include di NetBeans

### Konfigurasi XAMPP:
1. Install XAMPP
2. Buka XAMPP Control Panel
3. Start **Apache** dan **MySQL** services
4. Pastikan MySQL berjalan di port **3306**

## Setup Database

### 1. Melalui phpMyAdmin (Recommended):
1. Buka browser, akses `http://localhost/phpmyadmin`
2. Login dengan user: `root`, password: (kosong)
3. Buat database baru dengan nama: `laundry_db`
4. Import file `scripts/database-setup.sql` atau jalankan query manual

### 2. Otomatis via Aplikasi:
- Database akan dibuat otomatis saat pertama kali menjalankan aplikasi
- Aplikasi akan membuat database `laundry_db` dan semua tabel yang diperlukan

## Struktur Database MySQL

### Tables:
- `users` - Data user dan autentikasi
- `customers` - Data pelanggan
- `service_packages` - Paket layanan laundry
- `laundry_orders` - Order cucian masuk
- `payments` - Transaksi pembayaran

### Konfigurasi Database:
\`\`\`java
Host: localhost
Port: 3306
Database: laundry_db
Username: root
Password: (kosong - default XAMPP)
\`\`\`

## Cara Menjalankan

### Menggunakan NetBeans:
1. **Pastikan XAMPP MySQL sudah berjalan**
2. **Import project sebagai Maven project**
3. **Wait for dependencies to download**
4. **Run Main.java**

### Menggunakan Maven Command Line:
1. **Pastikan XAMPP MySQL sudah berjalan**
2. **Buka terminal di folder project**
3. **Compile project:**
   \`\`\`bash
   mvn clean compile
   \`\`\`
4. **Jalankan aplikasi:**
   \`\`\`bash
   mvn exec:java
   \`\`\`

### Manual Compilation:
1. **Download MySQL Connector/J dari MySQL website**
2. **Download FlatLaf JAR**
3. **Compile:**
   \`\`\`bash
   javac -cp "lib/*" -d out src/main/java/com/laundry/**/*.java
   \`\`\`
4. **Run:**
   \`\`\`bash
   java -cp "out:lib/*" com.laundry.Main
   \`\`\`

## Login Default

- **Username:** admin
- **Password:** admin123

## Troubleshooting

### Error: "Cannot connect to database"
**Solusi:**
1. Pastikan XAMPP sudah terinstall
2. Buka XAMPP Control Panel
3. Start Apache dan MySQL services
4. Pastikan MySQL berjalan di port 3306
5. Test koneksi via phpMyAdmin: `http://localhost/phpmyadmin`

### Error: "Access denied for user 'root'"
**Solusi:**
1. Buka phpMyAdmin
2. Pergi ke tab "User accounts"
3. Edit user 'root' dan pastikan password kosong
4. Atau ubah konfigurasi di `DatabaseManager.java`

### Error: "Communications link failure"
**Solusi:**
1. Restart MySQL service di XAMPP
2. Periksa firewall yang mungkin memblokir port 3306
3. Pastikan tidak ada aplikasi lain yang menggunakan port 3306

### Error: "MySQL JDBC Driver tidak ditemukan"
**Solusi:**
1. Pastikan dependency MySQL Connector/J sudah ada di pom.xml
2. Refresh Maven dependencies
3. Atau download manual MySQL Connector/J JAR

## Fitur Tambahan MySQL

### 1. Backup Database:
\`\`\`sql
mysqldump -u root -p laundry_db > backup.sql
\`\`\`

### 2. Restore Database:
\`\`\`sql
mysql -u root -p laundry_db < backup.sql
\`\`\`

### 3. Monitoring via phpMyAdmin:
- Akses `http://localhost/phpmyadmin`
- Monitor query performance
- View database statistics
- Manage users dan permissions

## Konfigurasi untuk Production

Untuk deployment production, ubah konfigurasi di `DatabaseManager.java`:

\`\`\`java
private static final String DB_HOST = "your-mysql-server";
private static final String DB_PORT = "3306";
private static final String DB_NAME = "laundry_db";
private static final String DB_USER = "your-username";
private static final String DB_PASSWORD = "your-password";
\`\`\`

## Keunggulan MySQL vs SQLite

1. **Multi-user support** - Beberapa user bisa akses bersamaan
2. **Better performance** - Untuk data yang besar
3. **Advanced features** - Stored procedures, triggers, views
4. **Backup tools** - phpMyAdmin, mysqldump
5. **Remote access** - Bisa diakses dari komputer lain
6. **Scalability** - Mudah di-scale untuk bisnis yang berkembang

## Pengembangan Lebih Lanjut

Fitur yang bisa ditambahkan:
- **Web interface** dengan PHP/Laravel
- **REST API** untuk mobile app
- **Real-time notifications**
- **Advanced reporting** dengan charts
- **Multi-branch support**
- **Integration** dengan payment gateway

## Support

Jika mengalami masalah:
1. Pastikan XAMPP MySQL berjalan
2. Check log error di console
3. Test koneksi database via phpMyAdmin
4. Periksa firewall dan antivirus

Project ini siap digunakan dengan XAMPP dan sudah include semua konfigurasi MySQL yang diperlukan.
