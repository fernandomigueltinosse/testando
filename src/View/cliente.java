/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.BiDAO;
import conexaoDB.Conexao;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.buttonRenderer;

/**
 *
 * @author pc2
 */
public class cliente extends javax.swing.JFrame {

     Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String path = "";
    String path2 = "";
    int id_cliente = -1;
    String Imagepath2="";
    SimpleDateFormat data =new SimpleDateFormat("d/M/yyyy");
    
    public cliente() {
        initComponents();
        conn = Conexao.conector();
        preencher_tabela(tblClientes);
        activar_botao(false, false, false);
    }

    private void activar_botao(boolean salvar,boolean actualizar,boolean apagar){
        BtnSalvar3.setEnabled(salvar);
        btnActualizar3.setEnabled(actualizar);
        btnApagar.setEnabled(apagar);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private void limpar(){
        activar_botao(false, false, false);
        txtIdCliente.setText(null);
        txtNome.setText(null);
        ((JTextField)txtDataDeEmissao.getDateEditor().getUiComponent()).setText("");
        ((JTextField)txtDataValidade.getDateEditor().getUiComponent()).setText("");
        jComboTipoDeDocumento.setSelectedItem(null);
        txtNumeroDoDocumento.setText(null);
        btnSelecinar.setText("Anexar B.I");
        lblPhoto.setIcon(null);
        Imagepath2 = "";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registar(){
            
        try {
            pst = conn.prepareStatement("insert into clientes (nome,tipo_de_documento,numero_do_documento,"
                + "data_de_emissao,data_validade,bi,imagem) value(?,?,?,?,?,?,?)");
            pst.setString(1, txtNome.getText());
            pst.setString(2, jComboTipoDeDocumento.getSelectedItem().toString());
            pst.setString(3, txtNumeroDoDocumento.getText());
           pst.setString(4, data.format(txtDataDeEmissao.getDate()));
            pst.setString(5, data.format(txtDataValidade.getDate()));
            
            if(path.trim().length() !=0){
            File caminho = new File(path);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            pst.setBytes(6, archivopdf);
            }else{
                pst.setString(6, null);
            }   
           
            if(Imagepath2.trim().length()!=0){
                InputStream photo = new FileInputStream(new File(Imagepath2));
                pst.setBlob(7, photo);
            }else{
             pst.setString(7, null);
            }
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "cadastrado com sucesso");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void actualizar(){
        try {
            File caminho = new File(path);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            InputStream photo = new FileInputStream(new File(Imagepath2));
            
            pst = conn.prepareStatement("update clientes set nome=?, tipo_de_documento=?, numero_do_documento=?,"
                + " data_de_emissao=?,data_validade=?, bi=?,imagem=?  where id_cliente=?");
            pst.setString(1, txtNome.getText());
            pst.setString(2, jComboTipoDeDocumento.getSelectedItem().toString());
            pst.setString(3, txtNumeroDoDocumento.getText());
            pst.setString(4, data.format(txtDataDeEmissao.getDate()));
            pst.setString(5, data.format(txtDataValidade.getDate()));
            pst.setBytes(6, archivopdf);
            pst.setBlob(7, photo);
            pst.setString(8, txtIdCliente.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"erro nivel 1");
        }
    
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
     private void actualizar2(){
        try {
            File caminho = new File(path);
            byte[] pdf = new byte[(int) caminho.length()];
            InputStream input = new FileInputStream(caminho);
            input.read(pdf);
            byte[] archivopdf = pdf;
            pst = conn.prepareStatement("update clientes set nome=?, tipo_de_documento=?, numero_do_documento=?,"
                + " data_de_emissao=?,data_validade=?, bi=?  where id_cliente=?");
            pst.setString(1, txtNome.getText());
            pst.setString(2, jComboTipoDeDocumento.getSelectedItem().toString());
            pst.setString(3, txtNumeroDoDocumento.getText());
            pst.setString(4, data.format(txtDataDeEmissao.getDate()));
            pst.setString(5, data.format(txtDataValidade.getDate()));
            pst.setBytes(6, archivopdf);
            pst.setString(7, txtIdCliente.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"erro nivel 2");
        }
    
    }
     /////////////////////////////////////////////////////////////////////////////////////////////////////////
      private void actualizar3(){
        try {
            InputStream photo = new FileInputStream(new File(Imagepath2));
            pst = conn.prepareStatement("update clientes set nome=?, tipo_de_documento=?, numero_do_documento=?,"
                + " data_de_emissao=?,data_validade=?, imagem=?  where id_cliente=?");
            pst.setString(1, txtNome.getText());
            pst.setString(2, jComboTipoDeDocumento.getSelectedItem().toString());
            pst.setString(3, txtNumeroDoDocumento.getText());
            pst.setString(4, data.format(txtDataDeEmissao.getDate()));
            pst.setString(5, data.format(txtDataValidade.getDate()));
            pst.setBlob(6, photo);
            pst.setString(7, txtIdCliente.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"erro nivel 3");
        }
    
    }
 
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
      private void actualizar4(){
        try {
            
            pst = conn.prepareStatement("update clientes set nome=?, tipo_de_documento=?, numero_do_documento=?,"
                + " data_de_emissao=?,data_validade=? where id_cliente=?");
            pst.setString(1, txtNome.getText());
            pst.setString(2, jComboTipoDeDocumento.getSelectedItem().toString());
            pst.setString(3, txtNumeroDoDocumento.getText());
           pst.setString(4, data.format(txtDataDeEmissao.getDate()));
            pst.setString(5, data.format(txtDataValidade.getDate()));
            pst.setString(6, txtIdCliente.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Actualizado com sucesso");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e+"erro nivel 4");
        }
    
    }
     ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void apagar(){
        int confirmar = JOptionPane.showConfirmDialog(null, "tem certeza que deseja apagar?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirmar == JOptionPane.YES_OPTION){
            
            String sql = "DELETE clientes,pedido,contratos,declaracao_do_cliente,dentrega,garantias, venda FROM clientes JOIN pedido on pedido.id_cliente=clientes.id_cliente JOIN contratos ON contratos.id_pedido=pedido.id_pedido join declaracao_do_cliente on declaracao_do_cliente.id_pedido=pedido.id_pedido join dentrega on dentrega.id_pedido=pedido.id_pedido join garantias on garantias.id_pedido=pedido.id_pedido JOIN venda on venda.id_pedido=pedido.id_pedido WHERE clientes.id_cliente =?";
         try {
             pst = conn.prepareStatement(sql);
             pst.setString(1, txtIdCliente.getText());
             pst.executeUpdate();
             JOptionPane.showMessageDialog(null, "Cliente apagado com sucesso");
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e);
           }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
private void preencher_tabela(JTable table){
    table.setDefaultRenderer(Object.class, new buttonRenderer());
    DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
    
    modelo.setNumRows(0);
    tblClientes.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
     
    String sql = "SELECT * from clientes order by id_cliente desc limit 50";
    //modelo.setColumnIdentifiers(new Object[]{"id cliente","nome","tipo de documento","numero","data de emissão","data de validade","anexo"});
    try {
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
      
        while(rs.next()){

        Object list[] = new Object[8];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);
            list[3]= rs.getString(3);
            list[4]= rs.getString(4);
            list[5]= rs.getString(5);
            list[6]= rs.getString(6);
        
          
             if (rs.getBytes(7) != null) {
                    list[7] = new JButton(icono);
                    
             } else {
                    list[7] = new JButton("Vazio");
                }
     
            modelo.addRow(new Object[]{ list[1],list[2],list[3],list[4],list[5],list[6],list[7],});
           
        }
        tblClientes.setModel(modelo);
        tblClientes.setRowHeight(38);
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
     public void preecher_campos(){
    int setar  = tblClientes.getSelectedRow();
   
     String sql = "SELECT id_cliente,nome,tipo_de_documento,numero_do_documento,data_de_emissao,data_validade,imagem FROM clientes where id_cliente=?";
     
       try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tblClientes.getModel().getValueAt(setar, 0).toString());
            rs = pst.executeQuery();
            rs.first();
            txtIdCliente.setText(rs.getString("id_cliente"));
            txtNome.setText(rs.getString("nome"));
            jComboTipoDeDocumento.setSelectedItem(rs.getString("tipo_de_documento"));
            txtNumeroDoDocumento.setText(rs.getString("numero_do_documento"));
            String dataEmissao= rs.getString("data_de_emissao");
            String dataValidade= rs.getString("data_validade");
            ((JTextField)txtDataDeEmissao.getDateEditor().getUiComponent()).setText(dataEmissao);
            ((JTextField)txtDataValidade.getDateEditor().getUiComponent()).setText(dataValidade);
            if(rs.getBytes("imagem")!= null){
                byte[] img = rs.getBytes("imagem");
                ImageIcon image = new ImageIcon(img);
                Image im = image.getImage();
                Image myimg = im.getScaledInstance(lblPhoto.getWidth(), lblPhoto.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon newImage = new ImageIcon(myimg);
                lblPhoto.setIcon(newImage);
            }else{
                lblPhoto.setIcon(null);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "codigo nao encontrado");
        }   
}
 ////////////////////////////////////////////////////////////////////////////////////////////////////////  
   private void filtrar_cliente(JTable table){
      table.setDefaultRenderer(Object.class, new buttonRenderer());
    
    DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
   
    modelo.setNumRows(0);
    tblClientes.getColumnModel().getColumn(0);
     ImageIcon icono = null;
     if (get_Image("/icons/pdf.png") != null) {
            icono = new ImageIcon(get_Image("/icons/pdf.png"));
        }
    
    String sql = "SELECT id_cliente,nome,tipo_de_documento,numero_do_documento,data_de_emissao,data_validade,bi from clientes where nome like ? order by id_cliente desc limit 50";
    try {
        pst = conn.prepareStatement(sql);
        pst.setString(1, txtFiltrar.getText() + "%");
        rs = pst.executeQuery();
         while(rs.next()){
             
            Object list[] = new Object[8];
            list[1]= rs.getInt(1);
            list[2]= rs.getString(2);
            list[3]= rs.getString(3);
            list[4]= rs.getInt(4);
            list[5]= rs.getInt(5);
            list[6]= rs.getString(6);

             if (list[7] != null) {
                    list[7] = new JButton(icono);
                    
                } else {
                    list[7] = new JButton("Vazio");
                }
                
                list[7] = new JButton(icono);
            modelo.addRow(new Object[]{    
            list[1],
            list[2],
            list[3],
            list[4],
            list[5],
            list[6],
            list[7],
            
            });
         
         }

        tblClientes.setModel(modelo);
        tblClientes.setRowHeight(38);
    } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////// 
    

  
///////////////////////////////////////////////////////////////////////////    
    public static void captura(int coluna){
        txtIdCliente.setText(String.valueOf(coluna));
        txtIdCliente.requestFocus();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////
   public void seleccionar_BI() {
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter fi = new FileNameExtensionFilter("pdf", "pdf");
        file.setFileFilter(fi);
        int se = file.showOpenDialog(this);
        if (se == 0) {
            this.btnSelecinar.setText("" + file.getSelectedFile().getName());
            path = file.getSelectedFile().getAbsolutePath();

        } else {}
    
   }
   private void imprimirCliente(){
       
   }
////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtIdCliente = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboTipoDeDocumento = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtNumeroDoDocumento = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnSelecinar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblPhoto = new javax.swing.JLabel();
        txtDataValidade = new com.toedter.calendar.JDateChooser();
        txtDataDeEmissao = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtFiltrar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        BtnSalvar3 = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnActualizar3 = new javax.swing.JButton();
        btnApagar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dados do cliente");
        setResizable(false);

        txtIdCliente.setEditable(false);
        txtIdCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtIdCliente.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtIdClienteInputMethodTextChanged(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Codigo");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nome");

        txtNome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel7.setText("Tipo de documento");

        jComboTipoDeDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "B.I", "Passaporte", "Outros" }));
        jComboTipoDeDocumento.setBorder(null);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Documento nº");

        txtNumeroDoDocumento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Data de emissão");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Data de validade");

        btnSelecinar.setBackground(new java.awt.Color(255, 0, 0));
        btnSelecinar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelecinar.setForeground(new java.awt.Color(255, 255, 255));
        btnSelecinar.setText("Anexar B.I");
        btnSelecinar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecinarActionPerformed(evt);
            }
        });

        jButton2.setText("Foto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblPhoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtDataValidade.setDateFormatString("d/M/yyyy");

        txtDataDeEmissao.setDateFormatString("d/M/yyyy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnSelecinar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 702, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jComboTipoDeDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumeroDoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDataValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataDeEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(jLabel2)
                    .addContainerGap(588, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboTipoDeDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNumeroDoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel11)
                            .addComponent(txtDataValidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addComponent(btnSelecinar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDataDeEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel2)
                    .addContainerGap(166, Short.MAX_VALUE)))
        );

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Filtrar pelo nome");

        txtFiltrar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtFiltrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltrarKeyReleased(evt);
            }
        });

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id cliente", "Nome", "Tipo de documento", "Nº documento", "Data de emissão", "Data de validade", "Documentos"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientes);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Operações"));

        BtnSalvar3.setText("Salvar");
        BtnSalvar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSalvar3jButton1ActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparjButton4ActionPerformed(evt);
            }
        });

        btnActualizar3.setText("Actualizar");
        btnActualizar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizar3ActionPerformed(evt);
            }
        });

        btnApagar.setText("Apagar");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });

        jButton1.setText("Novo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(BtnSalvar3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnActualizar3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnSalvar3)
                    .addComponent(btnActualizar3)
                    .addComponent(btnApagar)
                    .addComponent(btnLimpar)
                    .addComponent(jButton1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel25)
                        .addGap(12, 12, 12)
                        .addComponent(txtFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdClienteInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtIdClienteInputMethodTextChanged

    }//GEN-LAST:event_txtIdClienteInputMethodTextChanged

    private void btnSelecinarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecinarActionPerformed
        seleccionar_BI();
    }//GEN-LAST:event_btnSelecinarActionPerformed

    private void txtFiltrarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltrarKeyReleased
        filtrar_cliente(tblClientes);
    }//GEN-LAST:event_txtFiltrarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        activar_botao(false, true, true);
        int column = tblClientes.getColumnModel().getColumnIndexAtX(evt.getX());
        int row = evt.getY() / tblClientes.getRowHeight();

        if (row < tblClientes.getRowCount() && row >= 0 && column < tblClientes.getColumnCount() && column >= 0) {
            id_cliente = (int) tblClientes.getValueAt(row, 0);
            Object value = tblClientes.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                JButton boton = (JButton) value;

                if (boton.getText().equals("Vazio")) {
                    JOptionPane.showMessageDialog(null, "Nenhum arquivo encontrado");
                } else {
                    BiDAO pd = new BiDAO();
                    pd.executar_pdf(id_cliente);
                    try {
                        Desktop.getDesktop().open(new File("new.pdf"));
                    } catch (Exception ex) {
                    }
                }

            } else {

                preecher_campos();

            }
        }
    }//GEN-LAST:event_tblClientesMouseClicked

    private void BtnSalvar3jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSalvar3jButton1ActionPerformed
        registar();
        limpar();
        preencher_tabela(tblClientes);
    }//GEN-LAST:event_BtnSalvar3jButton1ActionPerformed

    private void btnLimparjButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparjButton4ActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparjButton4ActionPerformed

    private void btnActualizar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizar3ActionPerformed
        if(path.trim().length()!=0 && Imagepath2.trim().length()!=0){
            actualizar();
        }else if(path.trim().length()!=0){
            actualizar2();
        }else if(Imagepath2.trim().length()!=0){
            actualizar3();
        }else{
            actualizar4();
        }
        limpar();
        preencher_tabela(tblClientes);
    }//GEN-LAST:event_btnActualizar3ActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        apagar();
        limpar();
        preencher_tabela(tblClientes);
    }//GEN-LAST:event_btnApagarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        activar_botao(true, false, false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         try {
             JFileChooser chooser = new JFileChooser();
             chooser.showOpenDialog(null);
             File f  = chooser.getSelectedFile();
             String Imagepath = f.getAbsolutePath();
             ///this code resize de image 
             BufferedImage bi = ImageIO.read(new File(Imagepath));
             Image img = bi.getScaledInstance(152, 130, Image.SCALE_SMOOTH);
             ImageIcon icon = new ImageIcon(img);
            lblPhoto.setIcon(icon);
            Imagepath2 = Imagepath;
         } catch (IOException ex) {
             Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
         }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSalvar3;
    private javax.swing.JButton btnActualizar3;
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSelecinar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboTipoDeDocumento;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPhoto;
    private javax.swing.JTable tblClientes;
    private com.toedter.calendar.JDateChooser txtDataDeEmissao;
    private com.toedter.calendar.JDateChooser txtDataValidade;
    private javax.swing.JTextField txtFiltrar;
    private static javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNumeroDoDocumento;
    // End of variables declaration//GEN-END:variables
}
