package br.com.infox.telas;

import br.com.infox.classes.ClienteDAO;
import br.com.infox.classes.Clientes;
import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author caiomagno
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        ReadTable();

    }

    private void Consulta() {

        String sql = "select idcli as ID, nomecli as Nome, endcli as Endereço, fonecli as Telefone, emailcli as email from tbcliente where nomecli like ?";

        DefaultTableModel modelo = (DefaultTableModel) this.tbClientes.getModel();
        modelo.setNumRows(0);
        
        
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
           

            
            //As linhas abaixo são responsáveis por popular a tabela
            ClienteDAO cDAO = new ClienteDAO();
            
                
            
                for(Clientes c : cDAO.listaClientes() )
                {
                    modelo.addRow(new Object[]{
                        c.getIdCli(),
                        c.getNomeCli(),
                        c.getEndCli(),
                        c.getFoneCli(),
                        c.getEndCli()

                    });
                }
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Consulta: " + e);
        }
        
    }

    public void ReadTable() {

        DefaultTableModel modelo = (DefaultTableModel) this.tbClientes.getModel();
        modelo.setNumRows(0);

        ClienteDAO cDao = new ClienteDAO();

        for (Clientes c : cDao.listaClientes()) {

            modelo.addRow(new Object[]{
                c.getIdCli(),
                c.getNomeCli(),
                c.getEndCli(),
                c.getFoneCli(),
                c.getEmailCli()

            });

        }
    }

    public void remover() {
        String sql = "delete from tbcliente where idcli=?";
        int confirma = JOptionPane.showConfirmDialog(null, "Atenção!", "Tem certeza que deseja excluir este cliente?\nOs dados não poderam ser recuperados!", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliId.getText());
                int info = pst.executeUpdate();
                if (info > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente Excluído com Sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir Cliente!");
                }

                limpaCampos();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Remover Cliente: " + ex);
            }
        }
        

    }

    // Metodo para limpar campos
    private void limpaCampos() {
        txtCliId.setText(null);
        txtCliNome.setText(null);
        txtCliEndereco.setText(null);
        txtCliTel.setText(null);
        txtCliEmail.setText(null);
        txtCliPesquisar.setText(null);
        btnAdd.setEnabled(true);

    }

    public void settar_campos() {
        int setar = tbClientes.getSelectedRow();

        txtCliId.setText(tbClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tbClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEndereco.setText(tbClientes.getModel().getValueAt(setar, 2).toString());
        txtCliTel.setText(tbClientes.getModel().getValueAt(setar, 3).toString());
        txtCliEmail.setText(tbClientes.getModel().getValueAt(setar, 4).toString());

        btnAdd.setEnabled(false);

    }

    private void Inserir() {
        String sql = "insert into tbcliente (nomecli, endcli,fonecli, emailcli) values(?, ?, ?, ?)";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEndereco.getText());
            pst.setString(3, txtCliTel.getText());
            pst.setString(4, txtCliEmail.getText());

            if (txtCliNome.getText().isEmpty() || txtCliTel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos Campos Obrigatorios");

            } else {

                int inserido = pst.executeUpdate();
                if (inserido > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente Cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ai cadastrar novo cliente!");
                }

                limpaCampos();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Inserir: " + e);
        }

        limpaCampos();
        
    }

    private void Atualizar() {
        String sql = "update tbcliente set nomecli =?, endcli=?, fonecli=?, emailcli=? where idcli=?";

        try {

            if (txtCliNome.getText().isEmpty() || txtCliTel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos Campos Obrigatorios");

            } else {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtCliNome.getText());
                pst.setString(2, txtCliEndereco.getText());
                pst.setString(3, txtCliTel.getText());
                pst.setString(4, txtCliEmail.getText());
                pst.setString(5, txtCliId.getText());

                int atualizar = pst.executeUpdate();
                if (atualizar > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente foram atualizados!");
                } else {
                    JOptionPane.showMessageDialog(null, "ERRO: Não foi possível atualizar os dados do cliente!");
                }

                limpaCampos();
                btnAdd.setEnabled(true);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Atualizar: " + e);
        }
        
    }

    public void leiaCli() {

        DefaultTableModel modelo = (DefaultTableModel) this.tbClientes.getModel();
        modelo.setNumRows(0);

        String p = txtCliPesquisar.getText();

        ClienteDAO cliente = new ClienteDAO();

        for (Clientes c : cliente.pesquisar(p)) {
            modelo.addRow(new Object[]{
                c.getIdCli(),
                c.getNomeCli(),
                c.getEndCli(),
                c.getFoneCli(),
                c.getEmailCli()
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField8 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        txtCliEndereco = new javax.swing.JTextField();
        txtCliTel = new javax.swing.JTextField();
        txtCliEmail = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        btnLimpa = new javax.swing.JButton();

        jTextField8.setText("jTextField8");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tela de Clientes");
        setPreferredSize(new java.awt.Dimension(927, 671));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/Clientes.png"))); // NOI18N

        jLabel7.setText("* Nome:");

        jLabel8.setText("  Endereço:");

        jLabel9.setText("* Telefone:");

        jLabel10.setText("  Email:");

        jLabel11.setForeground(new java.awt.Color(255, 51, 51));
        jLabel11.setText("Campos Obrigatórios possuem (*)");

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdd.setToolTipText("Adicionar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/Delete.png"))); // NOI18N
        btnRemover.setToolTipText("Apagar");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel1.setText("Procurar Cliente:");

        tbClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbClientes.setAutoCreateRowSorter(true);
        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Endereco", "Telefone", "Email"
            }
        ));
        tbClientes.setFocusable(false);
        tbClientes.getTableHeader().setReorderingAllowed(false);
        tbClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbClientes);

        jLabel2.setText("ID:");

        txtCliId.setEditable(false);

        btnLimpa.setText("Limpar Campos");
        btnLimpa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(txtCliTel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCliEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel11)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCliNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCliEndereco, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAlterar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemover))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(btnLimpa)))
                        .addGap(40, 40, 40)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpa))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnAlterar)
                            .addComponent(btnRemover))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        setBounds(0, 0, 927, 671);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        Inserir();

    }//GEN-LAST:event_btnAddActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
        // TODO add your handling code here:
        Consulta();
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        if (txtCliPesquisar.equals(null)) {
            ReadTable();

        } else {
            leiaCli();
        }
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tbClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientesMouseClicked
        // Evento para setar os campos da tabela usando o mouse
        settar_campos();
    }//GEN-LAST:event_tbClientesMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        //Atualizar os dados do Cliente
        Atualizar();
        ReadTable();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // Excluir um Cliente
        remover();
        ReadTable();


    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnLimpaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpaActionPerformed
        // TODO add your handling code here:
        limpaCampos();
        ReadTable();
    }//GEN-LAST:event_btnLimpaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnLimpa;
    private javax.swing.JButton btnRemover;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEndereco;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliTel;
    // End of variables declaration//GEN-END:variables

}
