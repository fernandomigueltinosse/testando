/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import conexaoDB.Conexao;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author pc2
 */
public class BiDAO {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    //permite mostrar os dados do banco de dados
    public void executar_pdf(int id_cliente){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT bi FROM clientes WHERE id_cliente = ?;");
            pst.setInt(1, id_cliente);
            rs = pst.executeQuery();
            while (rs.next()) {
                b = rs.getBytes(1);
               
            }
            try (InputStream bos = new ByteArrayInputStream(b)) {
                int tamanoInput = bos.available();
                byte[] datosPDF = new byte[tamanoInput];
                bos.read(datosPDF, 0, tamanoInput);
                
                OutputStream out = new FileOutputStream("new.pdf");
                out.write(datosPDF);
                
                //abrir archivo
                out.close();
            }
          

        } catch (IOException | NumberFormatException | SQLException ex) {
           JOptionPane.showMessageDialog(null, ex);
        
    }
    
    }
    
}
