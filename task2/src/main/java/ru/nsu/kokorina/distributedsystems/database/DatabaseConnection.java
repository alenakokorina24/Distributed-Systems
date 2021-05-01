package ru.nsu.kokorina.distributedsystems.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/osm";
    private static final String DB_USER = "postgres";
    private static final String DB_PWD = "postgres";

    private static Connection connection;

    public static void init() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.
                getResource("initDatabase.sql")).getFile());
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuffer stringBuffer = new StringBuffer();
            while (reader.ready()) {
                stringBuffer.append(reader.readLine());
            }
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            Statement statement = connection.createStatement();
            String str = new String(stringBuffer);
            System.out.println(str);
            statement.execute(new String(stringBuffer));
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
