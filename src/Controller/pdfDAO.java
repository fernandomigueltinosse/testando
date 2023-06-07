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
public class pdfDAO {
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    
//permite mostrar os dados do banco de dados
    public void executar_pdf(int id_pedido){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT anexo FROM pedido WHERE id_pedido = ?;");
            pst.setInt(1, id_pedido);
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
    
  //permite mostrar os dados do banco de dados
    public void executar_pdfContrato(int id_contrato){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT anexo FROM contratos WHERE id_contrato = ?;");
            pst.setInt(1, id_contrato);
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
    
    
    //permite mostrar os dados do banco de dados
    public void executar_pdfGarantia(int id_garantia){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT garantia FROM garantias WHERE id_garantia = ?;");
            pst.setInt(1, id_garantia);
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
    
    
    ////////////////////////////////////////////////////////////////////////////
    
     //permite mostrar os dados do banco de dados
    public void executar_pdDeclaracaoEntrega(int id_entrega){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT declaracao_entrega FROM dentrega WHERE id_entrega = ?;");
            pst.setInt(1, id_entrega);
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
    
    
    ////////////////////////////////////////////////////////////////////////////
     //permite mostrar os dados do banco de dados
    public void executar_pdDeclaracaoCliente(int id_declaracao){
 
        conn = Conexao.conector();
        byte[] b = null;
        try {
            pst = conn.prepareStatement("SELECT declaracao FROM declaracao_do_cliente WHERE id_declaracao = ?;");
            pst.setInt(1, id_declaracao);
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
