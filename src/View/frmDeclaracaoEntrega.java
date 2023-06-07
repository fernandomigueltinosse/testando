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
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.buttonRenderer;


public class frmDeclaracaoEntrega extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String path="";
    int id_entrega = -1;
    
    public frmDeclaracaoEntrega() {
        initComponents();
         conn = Conexao.conector();
         preencher_tabela(tblEntrega);
         activar_botao(false, false, false);
    }
///////////////////////////////////////////////////////////////////////////////
    private void activar_botao(boolean salvar,boolean actualizar,boolean apagar){
        btnSalvar.setEnabled(salvar);
        btnActualizar.setEnabled(actualizar);
        //btnApagar.setEnabled(apagar);
        btnSelecionar.setText("Anexar Documento");
    }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       private void registar(){     
    String sql = "insert into dEntrega (id_pedido,declaracao_entrega) value(?,?)";
             try {
            if(path.trim().length() !=0){
            File caminho = new File(path);
            pst = conn.prepareStatement(sql);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;     
            pst =conn.prepareStatement(sql);
           
            pst.setString(1, txtPedido.getText());
            pst.setBytes(2, archivopdf);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "adicionado com sucesso");
                preencher_tabela(tblEntrega);
            limpar();
            }else{
                JOptionPane.showMessageDialog(null, "anexar garantia");
            }
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
       
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       private void actualizar(){
       String sql = "update dEntrega set id_pedido=?, declaracao_entrega=? where id_entrega=?";
             try {
            if(path.trim().length() != 0){
            File caminho = new File(path);
           
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtPedido.getText());
            pst.setBytes(2, archivopdf);
            pst.setString(3, txtIdEntrega.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "actualizado com sucesso");
            limpar();
                preencher_tabela(tblEntrega);
            }else{
                String sql2 = "update dEntrega set id_pedido=? where id_entrega=?";
                 pst = conn.prepareStatement(sql2);
                 pst.setString(1, txtPedido.getText());
                 pst.setString(2, txtIdEntrega.getText());
                 pst.executeUpdate();
                 JOptionPane.showMessageDialog(null, "actualizado com sucesso");
                 preencher_tabela(tblEntrega);
                 limpar();
                 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }

     
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     private void apagar(){
    
     int confirmar = JOptionPane.showConfirmDialog(null, "tem certeza que deseja apagar?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirmar == JOptionPane.YES_OPTION){
            
            String sql = "delete from dEntrega where id_entrega=?";
         try {
             pst = conn.prepareStatement(sql);
             pst.setString(1, txtIdEntrega.getText());
             pst.executeUpdate();
             JOptionPane.showMessageDialog(null, "apagado com sucesso");
             limpar();
             preencher_tabela(tblEntrega);
             
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
           }
        }
}
     /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void preencher_tabela(JTable table){
    table.setDefaultRenderer(Object.class, new buttonRenderer());
    DefaultTableModel modelo = (DefaultTableModel) tblEntrega.getModel();
    
    modelo.setNumRows(0);
    tblEntrega.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
     
    String sql = "SELECT id_entrega,nome,declaracao_entrega from dEntrega JOIN pedido on dEntrega.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente order by id_entrega desc";
    
    try {
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
      
        while(rs.next()){

        Object list[] = new Object[4];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);

             if (rs.getBytes(3) != null) {
                    list[3] = new JButton(icono);
                    
             } else {
                    list[3] = new JButton("Vazio");
                }
     
            modelo.addRow(new Object[]{    
            list[1],
            list[2],
            list[3],
         
            
            });
           
        }
        tblEntrega.setModel(modelo);
        tblEntrega.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
   
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
           public Image get_Image(String path) {
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
            Image mainIcon = imageIcon.getImage();
              
            return mainIcon;
        } catch (Exception e) {
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public void preencher_campos(){
    int linha  = tblEntrega.getSelectedRow();
          String sql = "select *from dEntrega where id_entrega=? limit 50";
    
         try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, tblEntrega.getModel().getValueAt(linha, 0).toString());
            rs = pst.executeQuery();
            rs.first();
            txtIdEntrega.setText(rs.getString("id_entrega"));
            txtPedido.setText(rs.getString("id_pedido"));
            
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "codigo nao encontrado"+e);
        }

   }
/////////////////////////////////////////////////////////////////////////////////
   
/////////////////////////////////////////////////////////////////////////////////

    public static void captura(int coluna){
        txtPedido.setText(String.valueOf(coluna));
        txtPedido.requestFocus();}

////////////////////////////////////////////////////////////////////////////////    
 private void limpar(){
     activar_botao(false, false, false);
     txtIdEntrega.setText(null);
     txtPedido.setText(null);
     btnSelecionar.setText("Anexar Documento");
 }   
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void seleccionar_garantia() {
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter fi = new FileNameExtensionFilter("pdf", "pdf");
        file.setFileFilter(fi);
        int se = file.showOpenDialog(this);
        if (se == 0) {
            this.btnSelecionar.setText("" + file.getSelectedFile().getName());
            path = file.getSelectedFile().getAbsolutePath();

        } else {}
    }
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 private void filtrar_garantias(JTable table){
      table.setDefaultRenderer(Object.class, new buttonRenderer());
    
    DefaultTableModel modelo = (DefaultTableModel) tblEntrega.getModel();
   
    modelo.setNumRows(0);
    tblEntrega.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
    
    String sql = "SELECT id_entrega,nome,declaracao_entrega, pedido.id_pedido from dEntrega JOIN pedido on dEntrega.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente where pedido.id_pedido like ? order by id_entrega desc limit 50";
   //
    //modelo.setColumnIdentifiers(new Object[]{"id pedido","nome","montante aprovado","juros","prestacao","prazo de pagamento","valor total","anexo"});
    try {
        pst = conn.prepareStatement(sql);
        pst.setString(1, txtProcurar.getText() + "%");
        rs = pst.executeQuery();
         while(rs.next()){
             
             Object list[] = new Object[4];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);
            
           
            list[3]= rs.getBytes(3);
            
            
             if (list[3] != null) {
                    list[3] = new JButton(icono);
                    
                } else {
                    list[3] = new JButton("Vazio");
                }
              
     
            modelo.addRow(new Object[]{    
            list[1],
            list[2],
            list[3],
           
            });
         
         }
        
       // tblPedido.setModel(DbUtils.resultSetToTableModel(rs));
        tblEntrega.setModel(modelo);
        tblEntrega.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntrega = new javax.swing.JTable();
        txtProcurar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtIdEntrega = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPedido = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        btnSelecionar = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Declaração de entrega");
        setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Declaração de entrega"));

        tblEntrega.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nome do cliente", "Anexo"
            }
        ));
        tblEntrega.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEntregaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEntrega);

        txtProcurar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtProcurar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProcurarKeyReleased(evt);
            }
        });

        jLabel7.setText("Filtrar pelo codigo");

        txtIdEntrega.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtIdEntrega.setEnabled(false);

        jLabel8.setText("ID");

        jLabel9.setText("Ref. do pedido de crédito");

        txtPedido.setEditable(false);
        txtPedido.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPedidoActionPerformed(evt);
            }
        });

        btnPesquisar.setText("Procurar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnSelecionar.setBackground(new java.awt.Color(255, 51, 51));
        btnSelecionar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelecionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSelecionar.setText("Anexar Documento");
        btnSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarActionPerformed(evt);
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Operações"));

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarjButton1ActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
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

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(74, 74, 74)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpar)
                    .addComponent(jButton1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 43, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(31, 31, 31)
                                .addComponent(txtIdEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPesquisar))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtProcurar)))
                        .addGap(62, 62, 62)
                        .addComponent(btnSelecionar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar)
                    .addComponent(jLabel8)
                    .addComponent(txtIdEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelecionar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPedidoActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
       //pesquisar();
       frmListaCreditoDeclaracaoEntrega frm = new frmListaCreditoDeclaracaoEntrega();
       frm.setVisible(true);
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void tblEntregaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEntregaMouseClicked
        activar_botao(false, true, true);
        int column = tblEntrega.getColumnModel().getColumnIndexAtX(evt.getX());
        int row = evt.getY() / tblEntrega.getRowHeight();
      
        if (row < tblEntrega.getRowCount() && row >= 0 && column < tblEntrega.getColumnCount() && column >= 0) {
            id_entrega = (int) tblEntrega.getValueAt(row, 0);
            Object value = tblEntrega.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                JButton boton = (JButton) value;

                if (boton.getText().equals("Vazio")) {
                    JOptionPane.showMessageDialog(null, "Nenhum arquivo encontrado");
                } else {
                    pdfDAO pd = new pdfDAO();
                    pd.executar_pdDeclaracaoEntrega(id_entrega);
                    try {
                        Desktop.getDesktop().open(new File("new.pdf"));
                    } catch (Exception ex) {
                    }
                }

            } else {
               
                   preencher_campos();
       
                
            }
        }
    }//GEN-LAST:event_tblEntregaMouseClicked

    private void txtProcurarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProcurarKeyReleased
        filtrar_garantias(tblEntrega);
    }//GEN-LAST:event_txtProcurarKeyReleased

    private void btnSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarActionPerformed
        seleccionar_garantia();
    }//GEN-LAST:event_btnSelecionarActionPerformed

    private void btnSalvarjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarjButton1ActionPerformed
        registar();
    }//GEN-LAST:event_btnSalvarjButton1ActionPerformed

    private void btnLimparjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparjButton4ActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparjButton4ActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

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
            java.util.logging.Logger.getLogger(frmDeclaracaoEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmDeclaracaoEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmDeclaracaoEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmDeclaracaoEntrega.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmDeclaracaoEntrega().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEntrega;
    private javax.swing.JTextField txtIdEntrega;
    private static javax.swing.JTextField txtPedido;
    private javax.swing.JTextField txtProcurar;
    // End of variables declaration//GEN-END:variables
}
