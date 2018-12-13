package com.mipt.app.database.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;

//TODO: think about buffer size and delete directory
@Service
public class SQLiteService {

    public static final String DATABASE_NAME = "encrypted_files.db";

    private final static Logger log = LoggerFactory.getLogger(SQLiteService.class);

    public void createNewDatabase(String dbPath) {

        File db = new File(dbPath);
        if(db.exists()){
            throw new RuntimeException("Database already exist");
        }

        String url = "jdbc:sqlite:" + dbPath;

        String sql = "CREATE TABLE IF NOT EXISTS files (\n"
                + "	id integer NOT NULL,\n"
                + "	path text NOT NULL,\n"
                + "	file blob NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Database was created");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void encryptFile(String dbPath, Long id, String filePath) {
        String url = "jdbc:sqlite:" + dbPath;

        String sql = "INSERT INTO files(id, path, file) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.setString(2, filePath);
            pstmt.setBytes(3, getBytesFromFile(filePath));
            pstmt.executeUpdate();
            log.info("File with path {} was encrypted", filePath);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(String dbPath, Long id) {
        String sql = "DELETE FROM files WHERE id = ?";
        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            log.info("File with id {} was decrypted", id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void decryptFile(String dbPath, Long fileId) {

        String selectSQL = "SELECT * FROM files WHERE id=?";
        String url = "jdbc:sqlite:" + dbPath;

        ResultSet rs = null;
        FileOutputStream fos = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String filePath = null;

        try {
            conn = DriverManager.getConnection(url);
            pstmt = conn.prepareStatement(selectSQL);
            pstmt.setLong(1, fileId);
            rs = pstmt.executeQuery();

            // write binary stream into file
            filePath = rs.getString("path");
            File file = new File(filePath);
            fos = new FileOutputStream(file);

            log.info("Writing BLOB to file " + file.getAbsolutePath());
            while (rs.next()) {
                InputStream input = rs.getBinaryStream("file");
                byte[] buffer = new byte[1];
                while (input.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null) {
                    conn.close();
                }
                if (fos != null) {
                    fos.close();
                }

            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private byte[] getBytesFromFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }
}
