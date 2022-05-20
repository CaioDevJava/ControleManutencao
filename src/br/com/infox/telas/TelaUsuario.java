package br.com.infox.telas;

import br.com.infox.classes.Usuarios;
import br.com.infox.classes.UsuariosDAO;
import java.sql.*;
import br.com.infox.dao.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author caiomagno
 */
public class TelaUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int n = 1;

    public TelaUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
        lerUser();

    }

    public void limpaCampos() {
        txtUserId.setText(null);
        txtUserNome.setText(null);
        txtUserLog.setText(null);
        txtUserPass.setText(null);
        txtUserFone.setText(null);
        btnUserCad.setEnabled(true);
    }

    private void Inserir() {
        String sql = "insert into tbusuarios (id_user, usuario, fone, login, senha, tipo) "
                + "values (?, ?, ?, ?, ?, ?)";

        try {

            //Pega os valores dos formularios para subistituir pela interrogação
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserId.getText());
            pst.setString(2, txtUserNome.getText());
            pst.setString(3, txtUserFone.getText());
            pst.setString(4, txtUserLog.getText());
            pst.setString(5, txtUserPass.getText());
            pst.setString(6, cbUserPerfil.getSelectedItem().toString());

            
            if ((txtUserId.getText().isEmpty()) || (txtUserNome.getText().isEmpty()) || (txtUserLog.getText().isEmpty())
                    || (txtUserPass.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios!");
                
            } else {

                //Insere as Sentencas acima na Base de Dados
                int confirma = pst.executeUpdate();
                if(confirma>0)
                {
                    JOptionPane.showMessageDialog(null, "Usuário Cadastrado com Sucesso!");
                }else{
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário!");
                }
            }
            //Exibe a Mensagem de usuário cadastrado com sucesso

            //Limpa os dados dos campos de textos
            txtUserId.setText(null);
            txtUserNome.setText(null);
            txtUserFone.setText(null);
            txtUserLog.setText(null);
            txtUserPass.setText(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            
        }
    }

    private void Consultar() {
        String sql = "select * from tbusuarios where id_user=?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserId.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                txtUserNome.setText(rs.getString(2));
                txtUserFone.setText(rs.getString(3));
                txtUserLog.setText(rs.getString(4));
                txtUserPass.setText(rs.getString(5));
                cbUserPerfil.setSelectedItem(rs.getString(6));

            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            JOptionPane.showMessageDialog(null, "Usuário não Cadastrado!");
        }
       
    }

    private void Apagar() {
        String sql = "delete from tbusuarios where id_user=?";
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este usuário?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUserId.getText());
                pst.executeUpdate();

                txtUserId.setText(null);
                txtUserNome.setText(null);
                txtUserFone.setText(null);
                txtUserLog.setText(null);
                txtUserPass.setText(null);

                if (confirma == 0) {
                    JOptionPane.showMessageDialog(null, "Usuário Excluído com Sucesso!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                JOptionPane.showMessageDialog(null, "Erro na exclusao do usuário!");
            }
        }
        
    }

    private void atualizar() {
        String sql = "update tbusuarios set usuario=?, fone =?, login=?, senha=?, tipo=? where id_user= ?";

        try {
            // Consertar este trecho do codigo

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtUserNome.getText());
            pst.setString(2, txtUserFone.getText());
            pst.setString(3, txtUserLog.getText());
            pst.setString(4, txtUserPass.getText());
            pst.setString(5, cbUserPerfil.getSelectedItem().toString());
            pst.setString(6, txtUserId.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            JOptionPane.showMessageDialog(null, "Erro ao atualizar os dados do usuário!");

        }
        JOptionPane.showMessageDialog(null, "Dados do usuário atualizados!");

        txtUserId.setText(null);
        txtUserNome.setText(null);
        txtUserFone.setText(null);
        txtUserLog.setText(null);
        txtUserPass.setText(null);

    }

    public void lerUser() {

        DefaultTableModel modelo = (DefaultTableModel) this.tbUser.getModel();
        modelo.setNumRows(0);

        //Objeto UsuariosDAO que ira passar para a tabela os dados obtidos do Banco de Dados
        UsuariosDAO user = new UsuariosDAO();

        //Esse for chama o metodo do Objeto UsuariosDAO que obtem os valores do banco de dados
        for (Usuarios u : user.listaUsuarios()) {
            //Adiciona mais um linha a tabela com os dados obtidos de UsuariosDAO
            modelo.addRow(new Object[]{
                u.getIdUser(),
                u.getNome(),
                u.getTel(),
                u.getLogin(),
                u.getPass(),
                u.getPerfil()
            });
        }

    }

    public void settar_campos() {

        int setar = this.tbUser.getSelectedRow();

        txtUserId.setText(tbUser.getModel().getValueAt(setar, 0).toString());
        txtUserNome.setText(tbUser.getModel().getValueAt(setar, 1).toString());
        txtUserFone.setText(tbUser.getModel().getValueAt(setar, 2).toString());
        txtUserLog.setText(tbUser.getModel().getValueAt(setar, 3).toString());
        txtUserPass.setText(tbUser.getModel().getValueAt(setar, 4).toString());

        btnUserCad.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        txtUserNome = new javax.swing.JTextField();
        txtUserFone = new javax.swing.JTextField();
        txtUserLog = new javax.swing.JTextField();
        txtUserPass = new javax.swing.JTextField();
        cbUserPerfil = new javax.swing.JComboBox<>();
        btnUserCad = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUser = new javax.swing.JTable();
        btnLimpar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(927, 671));

        jLabel1.setText("* ID:");

        jLabel2.setText("* Nome:");

        jLabel3.setText("* Login:");

        jLabel4.setText("* Senha:");

        jLabel5.setText("* Perfil:");

        jLabel6.setText("Telefone:");

        cbUserPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        btnUserCad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnUserCad.setToolTipText("Adicionar");
        btnUserCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserCadActionPerformed(evt);
            }
        });

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/Delete.png"))); // NOI18N
        btnRemove.setToolTipText("Excluir");
        btnRemove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        btnConsultar.setToolTipText("Consultar");
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnUpdate.setToolTipText("Atualizar");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        tbUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone", "Login", "Senha", "Perfil"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUserMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUser);

        btnLimpar.setText("Limpar Campos");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("Os campos que possuem o (*) são de preenchimento obrigatorio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnUserCad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConsultar)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(25, 25, 25)
                                .addComponent(txtUserLog, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUserFone, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUserNome, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUserPass, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbUserPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnLimpar)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(57, 57, 57))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtUserNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtUserFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtUserLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtUserPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cbUserPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnUserCad)
                    .addComponent(btnRemove)
                    .addComponent(btnConsultar)
                    .addComponent(btnUpdate))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        setBounds(0, 0, 927, 671);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUserCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserCadActionPerformed
        Inserir();
        lerUser();
    }//GEN-LAST:event_btnUserCadActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        Consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        Apagar();
        lerUser();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        atualizar();
        lerUser();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tbUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUserMouseClicked
        // TODO add your handling code here:
        settar_campos();
    }//GEN-LAST:event_tbUserMouseClicked

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        limpaCampos();
    }//GEN-LAST:event_btnLimparActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUserCad;
    private javax.swing.JComboBox<String> cbUserPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbUser;
    private javax.swing.JTextField txtUserFone;
    private javax.swing.JTextField txtUserId;
    private javax.swing.JTextField txtUserLog;
    private javax.swing.JTextField txtUserNome;
    private javax.swing.JTextField txtUserPass;
    // End of variables declaration//GEN-END:variables
}
