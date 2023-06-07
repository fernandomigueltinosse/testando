/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.pdfDAO;
import java.sql.*;
import conexaoDB.Conexao;
import java.awt.Desktop;
import java.awt.Image;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.buttonRenderer;



public class frmPedidoDeCredito extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String path = "";
    int id_pedido = -1;
    SimpleDateFormat data =new SimpleDateFormat("yyyy-MM-dd");
    
    public frmPedidoDeCredito() {
        initComponents();
        setLocationRelativeTo(null);
        conn = Conexao.conector();
        preencher_tabela(tblPedido);
         BtnSalvar.setEnabled(false);
        activar_botao(false, false, false);
    }

  
///////////////////////////////////////////////////////////////////////////////
    private void activar_botao(boolean salvar,boolean actualizar,boolean apagar){
        BtnSalvar.setEnabled(salvar);
        btnActualizar.setEnabled(actualizar);
        //btnApagar.setEnabled(apagar);
        btnseleccionar.setText("Anexar Pedido");
    }
///////////////////////////////////////////////////////////////////////////////    
    public void salvar(){
      
       String sql = "INSERT INTO pedido (id_cliente,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total,anexo) VALUES  (?,?,?,?,?,?,?);";
           
        try {
        if(path.trim().length() !=0){
        File caminho = new File(path);
        pst = conn.prepareStatement(sql);
        byte[] pdf = new byte[(int) caminho.length()];
        InputStream input = new FileInputStream(caminho);
        input.read(pdf);
        byte[] archivopdf = pdf;
            
        pst.setString(1, txtCodigoDoCliente.getText());
        pst.setString(2, txtMontanteAprovado.getText());
        pst.setString(3, txtTaxaDeJuro.getText());
        pst.setString(4, txtPrestacao.getText());
        pst.setString(5, data.format(txtPrazoPagamento.getDate()));
        pst.setString(6, txtValorTotal.getText());
        pst.setBytes(7, archivopdf);
        pst.executeUpdate(); 
        JOptionPane.showMessageDialog(null, "Salvo com sucesso");
        limpar();
        preencher_tabela(tblPedido);
        }
        else{ 
        String sql2 = "INSERT INTO pedido (id_cliente,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total) VALUES  (?,?,?,?,?,?)";
        pst = conn.prepareStatement(sql2);
            pst.setString(1, txtCodigoDoCliente.getText());
            pst.setString(2, txtMontanteAprovado.getText());
            pst.setString(3, txtTaxaDeJuro.getText());
            pst.setString(4, txtPrestacao.getText());
            pst.setString(5, data.format(txtPrazoPagamento.getDate()));
            pst.setString(6, txtValorTotal.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
            limpar();
            preencher_tabela(tblPedido);
        }
       
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            
        }       
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void update(){
        String sql = "update pedido set id_cliente=?,montante_aprovado=?,juros=?,prestacao=?,data_de_pagamento=?,valor_total=?,anexo=? where id_pedido=?;";
            
             
    try {
        if(path.trim().length() != 0){
            File caminho = new File(path);
            pst = conn.prepareStatement(sql);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            
            pst.setString(1, txtCodigoDoCliente.getText());
            pst.setString(2, txtMontanteAprovado.getText());
            pst.setString(3, txtTaxaDeJuro.getText());
            pst.setString(4, txtPrestacao.getText());
            pst.setString(5, data.format(txtPrazoPagamento.getDate()));
            pst.setString(6, txtValorTotal.getText());
            pst.setBytes(7, archivopdf);
            pst.setString(8, txtIdPedido.getText());
            pst.executeUpdate();
                  JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
                      preencher_tabela(tblPedido);
                  }else{
            
            String sql2 = "update pedido set id_cliente=?,montante_aprovado=?,juros=?,prestacao=?,data_de_pagamento=?,valor_total=? where id_pedido=?;";
           pst = conn.prepareStatement(sql2);
            pst.setString(1, txtCodigoDoCliente.getText());
            pst.setString(2, txtMontanteAprovado.getText());
            pst.setString(3, txtTaxaDeJuro.getText());
            pst.setString(4, txtPrestacao.getText());
            pst.setString(5, data.format(txtPrazoPagamento.getDate()));
            pst.setString(6, txtValorTotal.getText());
            pst.setString(7, txtIdPedido.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
            preencher_tabela(tblPedido);
                  }
                  
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            
        }
              
    }


    ////////////////////////////////////////////////////////////////////////////////////     
  private void apagar(){
    
     int confirmar = JOptionPane.showConfirmDialog(null, "tem certeza que deseja apagar?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirmar == JOptionPane.YES_OPTION){
            
            String sql = "DELETE FROM pedido JOIN contratos ON contratos.id_pedido=pedido.id_pedido join declaracao_do_cliente on declaracao_do_cliente.id_pedido=pedido.id_pedido join dentrega on dentrega.id_pedido=pedido.id_pedido join garantias on garantias.id_pedido=pedido.id_pedido WHERE pedido.id_pedido=?";
         try {
             pst = conn.prepareStatement(sql);
             pst.setString(1, txtIdPedido.getText());
             pst.executeUpdate();
             JOptionPane.showMessageDialog(null, "pedido de credito apagado com sucesso");
             preencher_tabela(tblPedido);
             limpar();
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
           }
        }
}
  
/////////////////////////////////////////////////////////////////////////////////////////////////////////
     public void seleccionar_pdf() {
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter fi = new FileNameExtensionFilter("pdf", "pdf");
        file.setFileFilter(fi);
        int se = file.showOpenDialog(this);
        if (se == 0) {
            this.btnseleccionar.setText("" + file.getSelectedFile().getName());
            path = file.getSelectedFile().getAbsolutePath();

        } else {}
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setar_campos(){
    int setar  = tblPedido.getSelectedRow();
   
     String sql = "SELECT id_pedido,pedido.id_cliente,nome,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total FROM pedido JOIN clientes  on pedido.id_cliente=clientes.id_cliente where id_pedido=?";
     
       try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, tblPedido.getModel().getValueAt(setar, 0).toString());
            rs = pst.executeQuery();
            rs.first();
            txtIdPedido.setText(rs.getString("id_pedido"));
            txtCodigoDoCliente.setText(rs.getString("id_cliente"));
            txtMontanteAprovado.setText(String.valueOf(rs.getDouble("montante_aprovado")));
            txtTaxaDeJuro.setText(rs.getString("juros"));
            txtPrestacao.setText(rs.getString("prestacao"));
            String prazo= rs.getString("data_de_pagamento");
            ((JTextField)txtPrazoPagamento.getDateEditor().getUiComponent()).setText(prazo);
            txtValorTotal.setText(String.valueOf(rs.getDouble("valor_total")));
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "codigo nao encontrado");
        }   
}   
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     

private void preencher_tabela(JTable table){
    table.setDefaultRenderer(Object.class, new buttonRenderer());
    DefaultTableModel modelo = (DefaultTableModel) tblPedido.getModel();
    
    modelo.setNumRows(0);
    tblPedido.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
     
    String sql = "SELECT id_pedido,nome,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total,anexo FROM pedido JOIN clientes  on pedido.id_cliente=clientes.id_cliente where id_pedido order by id_pedido desc limit 50";
    try {
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
      
        while(rs.next()){

        Object list[] = new Object[9];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);
            list[3]= rs.getDouble(3);
            list[4]= rs.getInt(4);
            list[5]= rs.getInt(5);
            list[6]= rs.getString(6);
            list[7]= rs.getDouble(7);
          
             if (rs.getBytes(8) != null) {
                    list[8] = new JButton(icono);
                     
             } else {
                    list[8] = new JButton("Vazio");
                }
     
            modelo.addRow(new Object[]{    
            list[1],
            list[2],
            list[3],
            list[4],
            list[5],
            list[6],
            list[7],
            list[8],
            });
           
        }
        tblPedido.setModel(modelo);
        tblPedido.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public Image get_Image(String path) {
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
            Image mainIcon = imageIcon.getImage();
              
            return mainIcon;
        } catch (Exception e) {
        }
        return null;
    }  
/////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    private void limpar(){
        activar_botao(false, false, false);
        btnseleccionar.setText("Anexar documento");
        txtCodigoDoCliente.setText(null);
        txtIdPedido.setText(null);
        txtPrestacao.setText(null);
        txtMontanteAprovado.setText(null);
        txtTaxaDeJuro.setText(null);
        txtValorTotal.setText(null);
        ((JTextField)txtPrazoPagamento.getDateEditor().getUiComponent()).setText("");
    }

  private void calcular_total(){
     if(!txtTaxaDeJuro.getText().isEmpty()){
      double montante, juros, percentual,total;
      montante = Double.parseDouble(txtMontanteAprovado.getText());
      juros = Double.parseDouble(txtTaxaDeJuro.getText());
      percentual = (montante * juros) / 100;
      total = percentual + montante;
      txtValorTotal.setText(String.valueOf(total));
     } 
      
      
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
   public static void captura(int coluna){
        txtCodigoDoCliente.setText(String.valueOf(coluna));
        txtCodigoDoCliente.requestFocus();
    
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void filtrar_pedidos(JTable table){
      table.setDefaultRenderer(Object.class, new buttonRenderer());
    
    DefaultTableModel modelo = (DefaultTableModel) tblPedido.getModel();
   
    modelo.setNumRows(0);
    tblPedido.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
    
    String sql = "SELECT id_pedido,nome,montante_aprovado,juros,prestacao,data_de_pagamento,valor_total, anexo FROM pedido JOIN clientes  on pedido.id_cliente=clientes.id_cliente where id_pedido like ? order by id_pedido desc limit 50";
   //
    //modelo.setColumnIdentifiers(new Object[]{"id pedido","nome","montante aprovado","juros","prestacao","prazo de pagamento","valor total","anexo"});
    try {
        pst = conn.prepareStatement(sql);
        pst.setString(1, txtProcurar.getText() + "%");
        rs = pst.executeQuery();
         while(rs.next()){
             
             Object list[] = new Object[9];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);
            list[3]= rs.getDouble(3);
            list[4]= rs.getInt(4);
            list[5]= rs.getInt(5);
            list[6]= rs.getString(6);
            list[7]= rs.getDouble(7);
            list[8]= rs.getBytes(8);
            
            
             if (list[8] != null) {
                    list[8] = new JButton(icono);
                    
                } else {
                    list[8] = new JButton("Vazio");
                }
              
     
            modelo.addRow(new Object[]{    
            list[1],
            list[2],
            list[3],
            list[4],
            list[5],
            list[6],
            list[7],
            list[8],
            });
         
         }
        
       // tblPedido.setModel(DbUtils.resultSetToTableModel(rs));
        tblPedido.setModel(modelo);
        tblPedido.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtMontanteAprovado = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtPrestacao = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTaxaDeJuro = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtValorTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtPrazoPagamento = new com.toedter.calendar.JDateChooser();
        jPanel11 = new javax.swing.JPanel();
        BtnSalvar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPedido = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtCodigoDoCliente = new javax.swing.JTextField();
        pesquisar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtIdPedido = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtProcurar = new javax.swing.JTextField();
        btnseleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pedido de crédito");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Proposta / Destino"));

        jLabel7.setText("Montante aprovado");

        jLabel22.setText("Prestação");

        jLabel24.setText("Taxa de juros  %");

        txtTaxaDeJuro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTaxaDeJuroKeyReleased(evt);
            }
        });

        jLabel8.setText("Valor Total ");

        txtValorTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtValorTotalMouseClicked(evt);
            }
        });

        jLabel15.setText("Prazo de pagamento");

        txtPrazoPagamento.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtMontanteAprovado, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(txtTaxaDeJuro, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(txtPrestacao, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(txtPrazoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtMontanteAprovado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTaxaDeJuro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel22)
                    .addComponent(txtPrestacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(txtPrazoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtMontanteAprovado, txtPrestacao});

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Operações"));

        BtnSalvar.setText("Salvar");
        BtnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSalvarjButton1ActionPerformed(evt);
            }
        });

        btnLimpar.setText("Cancelar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparjButton4ActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jButton1.setText("Novo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(61, 61, 61)
                .addComponent(BtnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnSalvar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpar)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BtnSalvar, btnActualizar, btnLimpar});

        tblPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id pedido", "Nome do cliente", "montante aprovado", "Juros", "Prestação", "Prazo de pagamento", "Valor total", "Anexo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPedidoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPedido);

        jLabel12.setText("Ref. Do Cliente");

        txtCodigoDoCliente.setEditable(false);
        txtCodigoDoCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtCodigoDoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoDoClienteKeyTyped(evt);
            }
        });

        pesquisar.setText("Pesquisar");
        pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisarActionPerformed(evt);
            }
        });

        jLabel14.setText("ID do pedido");

        txtIdPedido.setEditable(false);

        jLabel1.setText("Filtrar pela Referencia");

        txtProcurar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProcurarKeyReleased(evt);
            }
        });

        btnseleccionar.setBackground(new java.awt.Color(255, 51, 51));
        btnseleccionar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnseleccionar.setForeground(new java.awt.Color(255, 255, 255));
        btnseleccionar.setText("Anexar Documento");
        btnseleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnseleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCodigoDoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pesquisar)
                        .addGap(109, 109, 109)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnseleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(190, 190, 190))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(txtCodigoDoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pesquisar)
                    .addComponent(txtIdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnseleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimparjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparjButton4ActionPerformed
    limpar();
    }//GEN-LAST:event_btnLimparjButton4ActionPerformed

    private void BtnSalvarjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSalvarjButton1ActionPerformed
       salvar();
    }//GEN-LAST:event_BtnSalvarjButton1ActionPerformed

    private void txtCodigoDoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoDoClienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoDoClienteKeyTyped

    private void tblPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPedidoMouseClicked
        activar_botao(false, true, true);
         int column = tblPedido.getColumnModel().getColumnIndexAtX(evt.getX());
        int row = evt.getY() / tblPedido.getRowHeight();
      
        if (row < tblPedido.getRowCount() && row >= 0 && column < tblPedido.getColumnCount() && column >= 0) {
            id_pedido = (int) tblPedido.getValueAt(row, 0);
            Object value = tblPedido.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                JButton boton = (JButton) value;

                if (boton.getText().equals("Vazio")) {
                    JOptionPane.showMessageDialog(null, "Nenhum arquivo encontrado");
                } else {
                    pdfDAO pd = new pdfDAO();
                    pd.executar_pdf(id_pedido);
                    try {
                        Desktop.getDesktop().open(new File("new.pdf"));
                    } catch (Exception ex) {
                    }
                }

            } else {
               
                setar_campos();
                
            }
        }
    }//GEN-LAST:event_tblPedidoMouseClicked

    private void pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisarActionPerformed
             FrmListaDeClientes consulta = new FrmListaDeClientes();
             consulta.setVisible(true);
    }//GEN-LAST:event_pesquisarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        update();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtValorTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtValorTotalMouseClicked
        calcular_total();
    }//GEN-LAST:event_txtValorTotalMouseClicked

    private void txtTaxaDeJuroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxaDeJuroKeyReleased
        calcular_total();
    }//GEN-LAST:event_txtTaxaDeJuroKeyReleased

    private void btnseleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnseleccionarActionPerformed
       seleccionar_pdf();
    }//GEN-LAST:event_btnseleccionarActionPerformed

    private void txtProcurarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProcurarKeyReleased
        filtrar_pedidos(tblPedido);
    }//GEN-LAST:event_txtProcurarKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        activar_botao(true, false, false);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(frmPedidoDeCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPedidoDeCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPedidoDeCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPedidoDeCredito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPedidoDeCredito().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSalvar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnseleccionar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton pesquisar;
    private javax.swing.JTable tblPedido;
    private static javax.swing.JTextField txtCodigoDoCliente;
    private javax.swing.JTextField txtIdPedido;
    private javax.swing.JTextField txtMontanteAprovado;
    private com.toedter.calendar.JDateChooser txtPrazoPagamento;
    private javax.swing.JTextField txtPrestacao;
    private javax.swing.JTextField txtProcurar;
    private javax.swing.JTextField txtTaxaDeJuro;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
