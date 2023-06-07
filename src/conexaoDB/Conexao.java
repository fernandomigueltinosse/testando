/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexaoDB;

import View.frmConfig;
import View.frmLogin;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Conexao {
 

    public static Connection conector(){
    Connection conn;
    Properties p=new Properties ();
   
        try {
            String driver = "com.mysql.jdbc.Driver";
            FileInputStream fis=new FileInputStream("src/conexaoDB/config.properties"); 
            p.load (fis);
            String port= (String) p.get ("port");
            String database= (String) p.get ("database"); 
            String host = (String) p.get ("host");
            String user = (String) p.get ("user");
            String password = (String) p.get ("password");
            String url = "jdbc:mysql://"+host+":"+port+"/"+database;
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            
            return conn;
            
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(null, "erro de conexão \n" + e.getMessage());
            frmConfig config = new frmConfig();
            config.setVisible(true);
            
          
        }
            return null;
    }
}
    
    

