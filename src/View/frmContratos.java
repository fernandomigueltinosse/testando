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


public class frmContratos extends javax.swing.JFrame {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String path = "";
        int id_contrato = -1;
    public frmContratos() {
        initComponents();
         conn = Conexao.conector();
         preencher_tabela(tblContrato);
         activar_botao(false, false, false);
          
        
    }
    
        private void activar_botao(boolean salvar,boolean actualizar,boolean apagar){
        btnSalvar.setEnabled(salvar);
        btnActualizar.setEnabled(actualizar);
        //btnApagar.setEnabled(apagar);
        btnSelecionar.setText("Anexar Documento");
    }
/////////////////////////////////////////////////////////////////////////////// 
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registar(){     
   
             try {
            if(path.trim().length() !=0){
            File caminho = new File(path);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;     
            pst =conn.prepareStatement("insert into contratos (id_pedido,anexo) value(?,?)");
            pst.setString(1, txtIdPedidoDeCredito.getText());
            pst.setBytes(2, archivopdf);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Contrato adicionado com sucesso");
                preencher_tabela(tblContrato);
            limpar();
            }else{
                JOptionPane.showMessageDialog(null, "Selecione o contrato");
            }
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
       
    }
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void actualizar(){
       String sql = "update contratos set id_pedido=?, anexo=? where id_contrato=?";
             try {
            if(path.trim().length() != 0){
            File caminho = new File(path);
           
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtIdPedidoDeCredito.getText());
            pst.setBytes(2, archivopdf);
            pst.setString(3, txtIdContrato.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Contrato actualizado com sucesso");
            limpar();
                preencher_tabela(tblContrato);
            }else{
                String sql2 = "update contratos set id_pedido=? where id_contrato=?";
                 pst = conn.prepareStatement(sql2);
                 pst.setString(1, txtIdPedidoDeCredito.getText());
                 pst.setString(2, txtIdContrato.getText());
                 pst.executeUpdate();
                 JOptionPane.showMessageDialog(null, "Contrato actualizado com sucesso");
                 limpar();
                 preencher_tabela(tblContrato);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     private void apagar(){
    
     int confirmar = JOptionPane.showConfirmDialog(null, "tem certeza que deseja apagar?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirmar == JOptionPane.YES_OPTION){
            
            String sql = "delete from contratos where id_contrato=?";
         try {
             pst = conn.prepareStatement(sql);
             pst.setString(1, txtIdContrato.getText());
             pst.executeUpdate();
             JOptionPane.showMessageDialog(null, "Contrato apagado com sucesso");
             limpar();
             preencher_tabela(tblContrato);
             
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
           }
        }
}
     /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void limpar(){
        activar_botao(false, false, false);
        txtIdContrato.setText(null);
        txtIdPedidoDeCredito.setText(null);
        btnSelecionar.setText("Selecionar contrato");    
    }
 
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void preencher_tabela(JTable table){
    table.setDefaultRenderer(Object.class, new buttonRenderer());
    DefaultTableModel modelo = (DefaultTableModel) tblContrato.getModel();
    
    modelo.setNumRows(0);
    tblContrato.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
     
    String sql = "SELECT id_contrato,nome,contratos.id_pedido from contratos JOIN pedido on contratos.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente order by id_contrato desc";
    
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
        tblContrato.setModel(modelo);
        tblContrato.setRowHeight(38);
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
     public void preencher_campos(){
    int linha  = tblContrato.getSelectedRow();
          String sql = "select *from contratos where id_contrato=?";
    
         try {
            pst = conn.prepareStatement(sql);
           
            pst.setString(1, tblContrato.getModel().getValueAt(linha, 0).toString());
            rs = pst.executeQuery();
            rs.first();
            txtIdContrato.setText(rs.getString("id_contrato"));
            txtIdPedidoDeCredito.setText(rs.getString("id_pedido"));
            
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "codigo nao encontrado"+e);
        }   
}
////////////////////////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void captura(int coluna){
        txtIdPedidoDeCredito.setText(String.valueOf(coluna));
        txtIdPedidoDeCredito.requestFocus();}
    ////////////////////////////////////////////////////////////////////////////////////////////////////
   
     private void filtrar_contrato(JTable table){
      table.setDefaultRenderer(Object.class, new buttonRenderer());
    
    DefaultTableModel modelo = (DefaultTableModel) tblContrato.getModel();
   
    modelo.setNumRows(0);
    tblContrato.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
      
    String sql = "SELECT id_contrato,nome,contratos.anexo, pedido.id_pedido from contratos JOIN pedido on contratos.id_pedido=pedido.id_pedido JOIN clientes on pedido.id_cliente=clientes.id_cliente where pedido.id_pedido like ? order by id_contrato desc ";
   
    try {
        pst = conn.prepareStatement(sql);
        pst.setString(1, txtFiltrarContrato.getText() + "%");
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

            modelo.addRow(new Object[]{list[1],list[2],list[3],});
         
         }

        tblContrato.setModel(modelo);
        tblContrato.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////
     public void seleccionar_contrato() {
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter fi = new FileNameExtensionFilter("pdf", "pdf");
        file.setFileFilter(fi);
        int se = file.showOpenDialog(this);
        if (se == 0) {
            this.btnSelecionar.setText("" + file.getSelectedFile().getName());
            path = file.getSelectedFile().getAbsolutePath();

        } else {}
    }
    
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtIdPedidoDeCredito = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblContrato = new javax.swing.JTable();
        txtFiltrarContrato = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtIdContrato = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        btnSelecionar = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnSalvar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Contratos");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Contrato de Cliente"));
        jPanel2.setPreferredSize(new java.awt.Dimension(932, 500));

        txtIdPedidoDeCredito.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel29.setText("Ref. do pedido");

        tblContrato.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Codigo do Contrato", "Nome do cliente", "Anexo"
            }
        ));
        tblContrato.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblContratoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblContrato);

        txtFiltrarContrato.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtFiltrarContrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltrarContratoKeyReleased(evt);
            }
        });

        jLabel30.setText("Procurar pelo codigo");

        jLabel31.setText("Ref. Contrato");

        txtIdContrato.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtIdContrato.setEnabled(false);

        btnPesquisar.setText("Pesquisar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnSelecionar.setBackground(new java.awt.Color(255, 0, 0));
        btnSelecionar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelecionar.setForeground(new java.awt.Color(255, 255, 255));
        btnSelecionar.setText("Anexar Documento");
        btnSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(26, 26, 26)
                        .addComponent(txtFiltrarContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(27, 27, 27)
                        .addComponent(txtIdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdPedidoDeCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesquisar)
                        .addGap(18, 18, 18)
                        .addComponent(btnSelecionar)))
                .addGap(0, 23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel31)
                    .addComponent(txtIdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(txtIdPedidoDeCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar)
                    .addComponent(btnSelecionar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtFiltrarContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

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

        btnNovo.setText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(btnNovo)
                .addGap(55, 55, 55)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
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
                    .addComponent(btnNovo))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblContratoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContratoMouseClicked
        activar_botao(false, true, true);
        int column = tblContrato.getColumnModel().getColumnIndexAtX(evt.getX());
        int row = evt.getY() / tblContrato.getRowHeight();
      
        if (row < tblContrato.getRowCount() && row >= 0 && column < tblContrato.getColumnCount() && column >= 0) {
            id_contrato = (int) tblContrato.getValueAt(row, 0);
            Object value = tblContrato.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                JButton boton = (JButton) value;

                if (boton.getText().equals("Vazio")) {
                    JOptionPane.showMessageDialog(null, "Nenhum arquivo encontrado");
                } else {
                    pdfDAO pd = new pdfDAO();
                    pd.executar_pdfContrato(id_contrato);
                    try {
                        Desktop.getDesktop().open(new File("new.pdf"));
                    } catch (Exception ex) {
                    }
                }

            } else {
               
                   preencher_campos();
       txtIdContrato.requestFocus();
                
            }
        }
        
        
     
    }//GEN-LAST:event_tblContratoMouseClicked

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
       frmListaCreditoContrato consulta = new frmListaCreditoContrato();
             consulta.setVisible(true);
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void txtFiltrarContratoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltrarContratoKeyReleased
       filtrar_contrato(tblContrato);
    }//GEN-LAST:event_txtFiltrarContratoKeyReleased

    private void btnSalvarjButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarjButton1ActionPerformed
        registar();
    }//GEN-LAST:event_btnSalvarjButton1ActionPerformed

    private void btnLimparjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparjButton4ActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparjButton4ActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarActionPerformed
        seleccionar_contrato();
    }//GEN-LAST:event_btnSelecionarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        activar_botao(true, false, false);
    }//GEN-LAST:event_btnNovoActionPerformed

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
            java.util.logging.Logger.getLogger(frmContratos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmContratos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmContratos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmContratos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmContratos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionar;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblContrato;
    private javax.swing.JTextField txtFiltrarContrato;
    private javax.swing.JTextField txtIdContrato;
    private static javax.swing.JTextField txtIdPedidoDeCredito;
    // End of variables declaration//GEN-END:variables
}
