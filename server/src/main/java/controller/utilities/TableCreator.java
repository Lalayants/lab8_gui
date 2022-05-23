package controller.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TableCreator {
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void TableCreator() {
        Connection c = null;

        PreparedStatement stmt = null;

        String CreateSql = null;
        try {

            Class.forName("org.postgresql.Driver");
//            Properties info = new Properties();
//            info.load(new FileInputStream("Server/db.cfg"));
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");
            //          info.load(new FileInputStream("src/main/java/info_helios.cfg"));
//            c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s", "");

            connection = c;
            System.out.println("Database Connected ..");

            CreateSql = "create table LabWorks\n" +
                    "(\n" +
                    "    id                       SERIAL PRIMARY KEY,\n" +
                    "    name                     varchar(256),\n" +
                    "    x                        float8,\n" +
                    "    y                        INT,\n" +
                    "    creation_date            TIMESTAMP WITH TIME ZONE,\n" +
                    "    minimalPoint             float8,\n" +
                    "    personalQualitiesMinimum INT,\n" +
                    "    difficulty               varchar(20),\n" +
                    "    author_name              varchar(256),\n" +
                    "    birthday                 date,\n" +
                    "    weight                   float8,\n" +
                    "    eye_color                varchar(20),\n" +
                    "    login_id                    int\n" +
                    ");";
            stmt = c.prepareStatement(CreateSql);
            stmt.executeUpdate();
            System.out.println("LabWorks' table created");
//            stmt.close();
//
//            c.close();

        } catch (Exception e) {
            if (e.getMessage().contains("already exists"))
                System.out.println("LabWorks' table already exists");
            else {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

                System.exit(0);
            }
        }
        try {

            Class.forName("org.postgresql.Driver");

            // c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kpop");


            CreateSql = "Create Table Users(id SERIAL PRIMARY KEY, login varchar unique, password varchar ) ";
            System.out.println("User table created");
            stmt = c.prepareStatement(CreateSql);
            stmt.executeUpdate();

        } catch (Exception e) {
            if (e.getMessage().contains("already exists"))
                System.out.println("Table already exists");
            else {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

                System.exit(0);
            }
        }


        System.out.println("SQL tables are ready to use\n");
    }
}
