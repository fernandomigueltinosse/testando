/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.pedido;



public class PedidoDAO {
    
    Connection conn = null;
    PreparedStatement pst = null;
    String path = "";
    pedido p = new pedido();

//inserir dadas no banco de dados

 public void salvar(){
      
            String sql = "INSERT INTO pedido (id_cliente,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total,anexo) VALUES(?,?,?,?,?,?,?);";
            File caminho = new File(path);
             
              try {
            pst = conn.prepareStatement(sql);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            String x ="10";
            pst.setString(1, "1");
            pst.setString(2, x);
            pst.setString(3, x);
            pst.setString(4, x);
            pst.setString(5, x);
            pst.setString(6, x);
            pst.setBytes(7, archivopdf);
            //pst.setDouble(2,p.getId_cliente());
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
             pst.executeUpdate();
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            
        }       
    }

    
    
    
}
