/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import static View.frmReport.txtIdcredito;
import conexaoDB.Conexao;
import java.awt.BorderLayout;
import java.awt.Color;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;
//importa recursos da biblioteca rs2xml
import net.proteanit.sql.DbUtils;





public class frmGestaoDeVendas extends javax.swing.JFrame {
        String id_usuario;
       Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    SimpleDateFormat data =new SimpleDateFormat("yyyy-MM-d");
    
    public frmGestaoDeVendas() {
        initComponents();
        
        conn = Conexao.conector();
        preencher_tabela();
        activar_botao(false, false, false);
        
        
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    private void activar_botao(boolean salvar,boolean actualizar,boolean apagar){
        btnSalvar.setEnabled(salvar);
        //btnActualizar.setEnabled(actualizar);
        btnApagar.setEnabled(apagar);
        
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
     public static void captura4(int coluna, String coluna1,String coluna2,String coluna3){
        txtIdcredito.setText(String.valueOf(coluna));
        txtNomeDoCliente.setText(String.valueOf(coluna1));
        txtMontante.setText(String.valueOf(coluna2));
        ((JTextField)jDatePagamento.getDateEditor().getUiComponent()).setText(coluna3);
        txtIdcredito.requestFocus();
        
        
     }
       
   ////////////////////////////////////////////////////////////////////////////////////////////
     private void dias(){
         LocalDate da = LocalDate.now();
         try {
             String sql = "SELECT id_pedido,nome,DATE_FORMAT(data_de_pagamento,'%Y-%m-%d') as prazo,DATE_FORMAT(data_do_pedido,'%Y-%m-%d') AS data_do_pedido  FROM clientes JOIN pedido on pedido.id_cliente=clientes.id_cliente where id_pedido=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtIdcredito.getText());
            rs = pst.executeQuery();
            LocalDate primeiro = da;
            while(rs.next()){
                LocalDate segundo = LocalDate.parse(rs.getString(3));
           
            long diff  = ChronoUnit.DAYS.between(primeiro, segundo);
            txtDias.setText(String.valueOf(diff));
            if(primeiro.compareTo(segundo)<0){
                lblStatus.setText("Dentro do prazo");
                lblStatus.setForeground(Color.blue);
            }else{
                txtDias.setText("0");
                lblStatus.setText("Prazo expirado");
                lblStatus.setForeground(Color.red);
            }
     }
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null,e);
         }
     }
     ///////////////////////////////////////////////////////////////////////////
     
  //////////////////////////////////////////////////////////////////////////////////////////////////
     private void salvar(){
         String sql = "insert into venda (valor,multa,quebra_do_contrato,id_pedido,valor_do_bem,prazo_de_pagamento,prestacao_paga,username,gestor,avalista,conta,forma_de_pagamento,montante) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
         
         
           try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, txtValorAPagar.getText());
            pst.setString(2, txtMulta.getText().replaceAll(",", "."));
            pst.setString(3, txtQuebra.getText().replaceAll(",", "."));
            pst.setString(4, txtIdcredito.getText());
            pst.setString(5, txtVendaDoBem.getText().replaceAll(",", "."));
            pst.setString(6, data.format(jDatePagamento.getDate()));
            pst.setString(7, txtPrestacaoAPagar.getText());
            pst.setString(8, lblNome.getText());
            pst.setString(9, txtGestor.getText());
            pst.setString(10, txtAvalista.getText());
            pst.setString(11, txtConta.getText());
            pst.setString(12, txtFormaPagamento.getText());
            pst.setString(13, txtMontante.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso");
            preencher_tabela();
            divida();
            limpar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         
     }
     ///////////////////////////////////////////////////////////////////////////////////////////
     private void actualizar(){
      String sql = "update venda set valor=?,multa=?,quebra_do_contrato=?,id_pedido=?,Valor_do_bem=?,prazo_de_pagamento=?,prestacao_paga=?,username=?  where id_venda=?";
          try {
            pst = conn.prepareStatement(sql);
             pst.setString(1, txtValorAPagar.getText());
             pst.setString(2, txtMulta.getText().replaceAll(",", "."));
            pst.setString(3, txtQuebra.getText().replaceAll(",", "."));
            pst.setString(4, txtIdcredito.getText());
            pst.setString(5, txtVendaDoBem.getText());
            pst.setString(6, data.format(jDatePagamento.getDate()));
            pst.setString(7, txtPrestacaoAPagar.getText());
            pst.setString(8, lblNome.getText());
             pst.setString(9, txtIdVenda.getText());
            pst.executeUpdate();
             
            JOptionPane.showMessageDialog(null, "Cliente actualizado com sucesso");
            preencher_tabela();
            divida();
           limpar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         
      
     }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void apagar(){
     int confirmar = JOptionPane.showConfirmDialog(null, "tem certeza que deseja apagar?","Atenção",JOptionPane.                YES_NO_OPTION);
        if(confirmar == JOptionPane.YES_OPTION){
            
            String sql = "delete from venda where id_venda=?";
         try {
             pst = conn.prepareStatement(sql);
             pst.setString(1, txtIdVenda.getText());
             pst.executeUpdate();
             JOptionPane.showMessageDialog(null, "informação apagada com sucesso");
             preencher_tabela();
             limpar();
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
           }
        }
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////// 
    private void limpar(){
        activar_botao(false, false, false);
        txtValorAPagar.setText("0.0");
        txtNomeDoCliente.setText(null);
        txtMulta.setText("0.0");
        txtQuebra.setText("0.0");
        txtIdVenda.setText(null);
        txtMontante.setText("0.0");
        txtIdcredito.setText(null);
        txtSomatorio.setText("");
        txtVendaDoBem.setText("0.0");
        txtPrestacaoAPagar.setText("");
        lblStatus.setText("");
        txtPrestacaoAPagar.setText("");
        txtConta.setText("");
        txtAvalista.setText("");
        txtGestor.setText("");
        txtFormaPagamento.setText("");
        ((JTextField)jDatePagamento.getDateEditor().getUiComponent()).setText("");
    
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////    
      private void preencher_tabela(){
    
    DefaultTableModel modelo = (DefaultTableModel) tblVenda.getModel();
    modelo.setNumRows(0);
    tblVenda.getColumnModel().getColumn(0);
    
    String sql = "SELECT id_venda, nome,valor_total, valor,multa,quebra_do_contrato,data_da_operacao, prazo_de_pagamento,prestacao_paga FROM `venda` JOIN pedido ON venda.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente";
    try {
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
        while(rs.next()){
            modelo.addRow(new Object[]{
                rs.getInt(1),
                rs.getString(2),
                rs.getDouble(3),
                rs.getDouble(4),
                rs.getDouble(5),
                rs.getDouble(6),
                rs.getString(7),
                rs.getString(8),
                rs.getString(9),
               
            });
        }
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
      
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private void preencher_campos(){
   
    int linha = tblVenda.getSelectedRow();    
     String sql = "SELECT id_venda,nome,valor_total,valor,multa,quebra_do_contrato,data_da_operacao,venda.id_pedido, valor_do_bem,prazo_de_pagamento,prestacao_paga,forma_de_pagamento,conta,avalista,gestor FROM `venda` JOIN pedido ON venda.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente where id_venda=? order by id_venda desc";
    
         try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, tblVenda.getModel().getValueAt(linha, 0).toString());
            rs = pst.executeQuery();
            rs.first();
            txtIdVenda.setText(rs.getString("id_venda"));
            txtIdcredito.setText(rs.getString("id_pedido"));
            txtNomeDoCliente.setText(rs.getString("nome"));            
            txtMontante.setText(String.valueOf(rs.getDouble("valor_total")));
            txtMulta.setText(String.valueOf(rs.getDouble("multa")));
            txtQuebra.setText(String.valueOf(rs.getDouble("quebra_do_contrato")));
            txtValorAPagar.setText(String.valueOf(rs.getDouble("valor")));
            txtVendaDoBem.setText(String.valueOf(rs.getDouble("valor_do_bem")));
            String prazo = rs.getString("prazo_de_pagamento");
            txtPrestacaoAPagar.setText(rs.getString("prestacao_paga"));
            txtFormaPagamento.setText(rs.getString("forma_de_pagamento"));
            txtConta.setText(rs.getString("conta"));
            txtAvalista.setText(rs.getString("avalista"));
            txtGestor.setText(rs.getString("gestor"));
            ((JTextField)jDatePagamento.getDateEditor().getUiComponent()).setText(prazo);
            divida();
           
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "codigo nao encontrado" +e);
        }
   
    
      }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void filtrar(){
    String sql = "SELECT id_venda, nome,valor_total, valor,multa,quebra_do_contrato,data_da_operacao FROM `venda` JOIN pedido ON venda.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente where id_venda like ? order by id_venda desc";
    try {
        pst = conn.prepareStatement(sql);
        pst.setString(1, txtFiltrar.getText() + "%");
        rs = pst.executeQuery();
        tblVenda.setModel(DbUtils.resultSetToTableModel(rs));
        
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
    
       private void divida(){
         String sql = "SELECT SUM(valor) FROM `venda` WHERE id_pedido=?";
         Double divida;
         Double pago= null;
         Double credito;
         Double bemVendido;
           DecimalFormat dec = new DecimalFormat("###,###,###.##");
    
         try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, txtIdcredito.getText());
            rs = pst.executeQuery();
            while(rs.next()){
                pago =rs.getDouble(1);
            }
            credito = Double.valueOf(txtMontante.getText());
            bemVendido = Double.valueOf(txtVendaDoBem.getText());
            divida = credito - pago-bemVendido;
            
            if(divida<-1){
            txtSomatorio.setText("0.0");
            }else{
            txtSomatorio.setText(dec.format(divida));
            }
            
            dias();
           
        } catch (Exception e) {
            
        }
    
    }
       
 ///////////////////////////////////////////////////////////////////////////////////////////////////
       private void actualizarData(){
           try {
                String sql2 = "update pedido set data_de_pagamento=? where id_pedido=?;";
           pst = conn.prepareStatement(sql2);
            pst.setString(1, data.format(jDatePagamento.getDate()));
            pst.setString(2, txtIdcredito.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data actualizado com sucesso");
           } catch (Exception e) {
               JOptionPane.showMessageDialog(null, e);
           }
       }
   /////////////////////////////////////////////////////////////////////////////////////////////////
      
     
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSalvar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVenda = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtFiltrar = new javax.swing.JTextField();
        btnApagar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        Historico = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        lblNome = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtIdVenda = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtIdcredito = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNomeDoCliente = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtMontante = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtSomatorio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jDatePagamento = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtDias = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtValorAPagar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMulta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtQuebra = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtVendaDoBem = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPrestacaoAPagar = new javax.swing.JTextField();
        txtConta = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtAvalista = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtFormaPagamento = new javax.swing.JTextField();
        txtGestor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Vendas");
        setResizable(false);

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        tblVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Montante", "Valor pago", "Multa", "Quebra do contrato", "Data da operação", "Prazo do pagamento", "Prestações"
            }
        ));
        tblVenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVendaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVenda);

        jLabel4.setText("Filtrar movimento pelo código");

        txtFiltrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltrarKeyReleased(evt);
            }
        });

        btnApagar.setText("apagar");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        Historico.setText("Histórico");
        Historico.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255)));
        Historico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoricoActionPerformed(evt);
            }
        });

        jButton6.setText("Novo");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtIdVenda.setEditable(false);
        txtIdVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdVendaActionPerformed(evt);
            }
        });

        jLabel3.setText("ID");

        jLabel1.setText("Ref. do crédito");

        txtIdcredito.setEditable(false);
        txtIdcredito.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdcreditoFocusGained(evt);
            }
        });
        txtIdcredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdcreditoActionPerformed(evt);
            }
        });

        jButton1.setText("procurar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Nome do Cliente");

        txtNomeDoCliente.setEditable(false);

        jLabel7.setText("Montante");

        txtMontante.setEditable(false);

        jLabel13.setText("Saldo da divida");

        txtSomatorio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtSomatorio.setForeground(new java.awt.Color(255, 0, 0));

        jLabel9.setText("Prazo de pagamento");

        jDatePagamento.setDateFormatString("yyyy-MM-dd");

        jButton2.setForeground(new java.awt.Color(255, 0, 0));
        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel15.setText("Dias remanescentes");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtIdcredito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton1))
                                    .addComponent(txtIdVenda)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNomeDoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMontante, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSomatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jDatePagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(24, 24, 24)
                        .addComponent(txtDias, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1)
                    .addComponent(txtIdcredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNomeDoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMontante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtSomatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jDatePagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(txtDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setText("A pagar");

        txtValorAPagar.setText("0.0");
        txtValorAPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorAPagarActionPerformed(evt);
            }
        });

        jLabel5.setText("Multa");

        txtMulta.setText("0.0");

        jLabel6.setText("Quebra do contrato");

        txtQuebra.setText("0.0");

        jLabel14.setText("Valor da venda do bem");

        txtVendaDoBem.setText("0.0");

        jLabel12.setText("Prestações a pagar");

        jLabel16.setText("Conta");

        jLabel17.setText("Avalista");

        jLabel11.setText("Forma de pagamento");

        jLabel18.setText("Gestor");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10)
                            .addComponent(jLabel5))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQuebra, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(txtGestor))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAvalista, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(21, 21, 21)
                                .addComponent(txtConta, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(28, 28, 28))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addGap(44, 44, 44)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(20, 20, 20)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtVendaDoBem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrestacaoAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtAvalista, txtConta, txtGestor});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtFormaPagamento, txtMulta, txtPrestacaoAPagar, txtQuebra, txtValorAPagar});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel16)
                    .addComponent(txtConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtAvalista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtQuebra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtGestor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtVendaDoBem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtPrestacaoAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setText("Emitir recibo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnApagar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpar)
                        .addGap(18, 18, 18)
                        .addComponent(Historico, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(jLabel4)
                    .addComponent(txtFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnApagar)
                    .addComponent(btnLimpar)
                    .addComponent(Historico, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        frmListaCreditoVenda consulta = new frmListaCreditoVenda();
        consulta.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtIdcreditoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdcreditoFocusGained
      divida();
        
    }//GEN-LAST:event_txtIdcreditoFocusGained

    private void txtFiltrarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltrarKeyReleased
       filtrar();
    }//GEN-LAST:event_txtFiltrarKeyReleased

    private void tblVendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVendaMouseClicked
       preencher_campos();
        activar_botao(false, true, true);
    }//GEN-LAST:event_tblVendaMouseClicked

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        salvar();
        divida();
       
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void txtIdVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdVendaActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
      limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void HistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoricoActionPerformed
        
        frmReport r = new frmReport();
        r.setVisible(true);
   
    }//GEN-LAST:event_HistoricoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        activar_botao(true, false, false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        actualizarData();
        dias();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtIdcreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdcreditoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdcreditoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       frmRecibo recibo = new frmRecibo();
       recibo.setVisible(true);
       recibo.txtIdVenda.setText(txtIdVenda.getText());
       recibo.lblDivida.setText(txtSomatorio.getText().replaceAll(",", ""));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtValorAPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorAPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorAPagarActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        apagar();
    }//GEN-LAST:event_btnApagarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmGestaoDeVendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmGestaoDeVendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmGestaoDeVendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmGestaoDeVendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmGestaoDeVendas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Historico;
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    public static com.toedter.calendar.JDateChooser jDatePagamento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblVenda;
    private javax.swing.JTextField txtAvalista;
    private javax.swing.JTextField txtConta;
    private javax.swing.JTextField txtDias;
    private javax.swing.JTextField txtFiltrar;
    private javax.swing.JTextField txtFormaPagamento;
    private javax.swing.JTextField txtGestor;
    private javax.swing.JTextField txtIdVenda;
    private static javax.swing.JTextField txtIdcredito;
    private static javax.swing.JTextField txtMontante;
    private javax.swing.JTextField txtMulta;
    private static javax.swing.JTextField txtNomeDoCliente;
    private javax.swing.JTextField txtPrestacaoAPagar;
    private javax.swing.JTextField txtQuebra;
    private javax.swing.JTextField txtSomatorio;
    private javax.swing.JTextField txtValorAPagar;
    private javax.swing.JTextField txtVendaDoBem;
    // End of variables declaration//GEN-END:variables
}
