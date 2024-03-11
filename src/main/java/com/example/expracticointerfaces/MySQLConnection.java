package com.example.expracticointerfaces;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class MySQLConnection {

    private static Logger log=Logger.getLogger(MySQLConnection.class.getName());

    @Getter
    private static Connection conexion;

    static{
        InputStream is=MySQLConnection.class.getClassLoader().getResourceAsStream("mysql.properties");
        Properties p=new Properties();
        try {
            p.load(is);
            conexion= DriverManager.getConnection("jdbc:mysql://"+p.getProperty("url")+":"+p.getProperty("port")+"/"+p.getProperty("nombreBD"),p.getProperty("user"),p.getProperty("password"));
            System.out.println(""+conexion);
            log.info("Conexion Succesful");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.severe("Failed Conection");
            throw new RuntimeException(e);

        }
    }

}
