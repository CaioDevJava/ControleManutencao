package br.com.infox.telas;

import br.com.infox.classes.ClienteDAO;
import br.com.infox.classes.Clientes;
import java.sql.*;
import br.com.infox.dao.ModuloConexao;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author caiomagno
 */
public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();
        
        DefaultTableModel modelo = (DefaultTableModel) this.tbClientes.getModel();
        modelo.setNumRows(0);
        conexao = ModuloConexao.conector();

    }

    private void limparCampos() {
        txtNumOs.setText(null);
        txtDataOs.setText(null);
        txtEquipamentoOS.setText(null);
        txtDefeitoOs.setText(null);
        txtServicoOs.setText(null);
        txtTecnicoOs.setText(null);
        txtValorOS.setText("0");
        btnAddOs.setEnabled(true);
    }

    private void settar_campos() {
        int setar = tbClientes.getSelectedRow();

        txtCliId.setText(tbClientes.getModel().getValueAt(setar, 0).toString());

    }

    //Pesquisa os clientes na tela de OS
    private void PesquisaC() {
        String sql = "select idcli, nomecli, fonecli from tbcliente where nomecli like ?";
        String cli = txtCliOs.getText();
        
        DefaultTableModel modelo = (DefaultTableModel) this.tbClientes.getModel();
        modelo.setNumRows(0);
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + cli + "%");
            rs = pst.executeQuery();
            
            //As linhas abaixo são responsáveis por popular a tabela
            ClienteDAO cDAO = new ClienteDAO();
            
            for(Clientes c : cDAO.pesquisar(cli) )
            {
                modelo.addRow(new Object[]{
                    c.getIdCli(),
                    c.getNomeCli(),
                    c.getEndCli(),
                    c.getFoneCli(),
                    c.getEndCli()
                    
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Pesquisar Cliente OS: " + ex);
        }
    }

    //Cria uma nova Ordem de Serviço
    private void newOs() {
        String sql = "insert into tbos (tipo, situacao, equipamento, defeito, servico, tecnico, valor, idcli) values (?, ?, ?, ?, ?, ?, ?, ?) ";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cbSituacaoOs.getSelectedItem().toString());
            pst.setString(3, txtEquipamentoOS.getText());
            pst.setString(4, txtDefeitoOs.getText());
            pst.setString(5, txtServicoOs.getText());
            pst.setString(6, txtTecnicoOs.getText());
            pst.setString(7, txtValorOS.getText().replace(",", "."));
            pst.setString(8, txtCliId.getText());

            //Verifica se todos os campos obrigatórios foram preenchidos
            if (txtCliId.getText().isEmpty() || txtEquipamentoOS.getText().isEmpty() || txtDefeitoOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigarios!");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS emitida com sucesso!");
                    limparCampos();

                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao emitir OS");
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
       
    }

    //Pesquisa por Ordens de Serviços pelo seu número
    private void PesquisarOS() {
        //Abre uma caixa de texto onde o usuário digita o número da Ordem de Serviço que deseja consultar
        String num_os = JOptionPane.showInputDialog("Número da OS");

        //Variavel contendo o valor de pesquisa SQL no banco de dados
        String sql = "Select * from tbos where os=" + num_os;

        //Busca os dados pelo valor digitado da Ordem de Serviço e Retorna os Resultados
        //Nas caixas de Texto
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNumOs.setText(rs.getString(1));
                txtDataOs.setText(rs.getString(2));
                String rbTipo = rs.getString(9);

                //Tratamento do Radio Button buscando o texto no banco de dados e marcando a opção equivalente
                //no grupo de radio buttons da Tela de OS
                if (rbTipo.equals("O.S")) {
                    rbOs.setSelected(true);
                    tipo = "O.S";
                } else {
                    rbOrcamentoOs.setSelected(true);
                    tipo = "Orçamento";
                }

                cbSituacaoOs.setSelectedItem(rs.getString(10));
                txtEquipamentoOS.setText(rs.getString(3));
                txtDefeitoOs.setText(rs.getString(4));
                txtServicoOs.setText(rs.getString(5));
                txtTecnicoOs.setText(rs.getString(6));
                txtValorOS.setText(rs.getString(7));

                //Desabilita os Botões de adicionar uma nova OS, o campo de texto onde busca o nome do cliente e tira a visibilidade da tabela
                btnAddOs.setEnabled(false);
                txtCliOs.setEnabled(false);
                tbClientes.setVisible(false);

                // Exibe uma mensagem caso a busca pelo banco de dados não resulte valor
            } else {
                JOptionPane.showMessageDialog(null, "Sem OS cadastradas!");
            }

            //Trata a exceção de valor digitado que não é o mesmo do campo do banco de dados
        } catch (java.sql.SQLSyntaxErrorException ex) {
            JOptionPane.showMessageDialog(null, "OS Inválida se você estiver usando letras uma OS é composta apenas por números!");
            System.out.println(ex);
        } //Trata outro tipo de exceção que possa acontecer
        catch (HeadlessException | SQLException err) {
            JOptionPane.showMessageDialog(null, "Erro na consulta de OS: " + err);
        }
        
    }

    //COnferir este trecho do codigo e ver porque nao esta fazendo update
    private void atualizarOs() {
        String sql = "update tbos set tipo=?,situacao=?, equipamento=?, defeito=?, servico=?,tecnico=?,valor=? where os=?";

        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, tipo);
            pst.setString(2, cbSituacaoOs.getSelectedItem().toString());
            pst.setString(3, txtEquipamentoOS.getText());
            pst.setString(4, txtDefeitoOs.getText());
            pst.setString(5, txtServicoOs.getText());
            pst.setString(6, txtTecnicoOs.getText());
            pst.setString(7, txtValorOS.getText());
            pst.setString(8, txtNumOs.getText());

            int info = pst.executeUpdate();
            if (info > 0) {
                JOptionPane.showMessageDialog(null, "OS Alterada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar OS!");
            }

            limparCampos();

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Atualizar a OS!" + e);
        }
        
    }

    private void excluirOS() {

        String sql = "delete from tbos where os=?";

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta ordem de serviço?\n Os dados não poderam ser recuperados!", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtNumOs.getText());

                int info = pst.executeUpdate();

                if (info > 0) {
                    JOptionPane.showMessageDialog(null, "OS excluída com sucesso");
                    limparCampos();
                    txtCliId.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir OS!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Exclusão de OS: " + ex);
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDataOs = new javax.swing.JTextField();
        txtNumOs = new javax.swing.JTextField();
        rbOrcamentoOs = new javax.swing.JRadioButton();
        rbOs = new javax.swing.JRadioButton();
        btnOsCad = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cbSituacaoOs = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtCliOs = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtEquipamentoOS = new javax.swing.JTextField();
        txtDefeitoOs = new javax.swing.JTextField();
        txtServicoOs = new javax.swing.JTextField();
        txtValorOS = new javax.swing.JTextField();
        txtTecnicoOs = new javax.swing.JTextField();
        btnAddOs = new javax.swing.JButton();
        btnUpdateOs = new javax.swing.JButton();
        btnRemoveOs = new javax.swing.JButton();
        BtnFindOs = new javax.swing.JButton();

        jToggleButton1.setText("jToggleButton1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ordem de Serviço");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nº OS:");

        jLabel2.setText("Data:");

        txtDataOs.setEditable(false);

        txtNumOs.setEditable(false);

        buttonGroup1.add(rbOrcamentoOs);
        rbOrcamentoOs.setText("Orçamento");
        rbOrcamentoOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbOrcamentoOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrcamentoOsActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbOs);
        rbOs.setText("Ordem de Serviço");
        rbOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOsActionPerformed(evt);
            }
        });

        btnOsCad.setText("OS Cadastradas");
        btnOsCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsCadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNumOs, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnOsCad)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtDataOs)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbOrcamentoOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbOs)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOsCad))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDataOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbOrcamentoOs)
                    .addComponent(rbOs))
                .addContainerGap())
        );

        jLabel3.setText("Situação");

        cbSituacaoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Na Bancada", "Entregue ao Cliente", "Orçamento Reprovado", "Aguardando Aprovação", "Aguardando Peças", "Abandonado pelo Cliente", "Retornou" }));
        cbSituacaoOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtCliOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliOsActionPerformed(evt);
            }
        });
        txtCliOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliOsKeyReleased(evt);
            }
        });

        jLabel4.setText("*ID:");

        txtCliId.setEditable(false);

        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone"
            }
        ));
        tbClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbClientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel5.setText("* Equipamento:");

        jLabel6.setText("* Defeito:");

        jLabel7.setText("Técnico:");

        jLabel8.setText("Valor:");

        jLabel9.setText("Serviço:");

        txtValorOS.setText("0");

        btnAddOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/new-file_40454.png"))); // NOI18N
        btnAddOs.setToolTipText("Nova O.S");
        btnAddOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOsActionPerformed(evt);
            }
        });

        btnUpdateOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/edit_modify_alter_icon_141930.png"))); // NOI18N
        btnUpdateOs.setToolTipText("Atualizar uma O.S");
        btnUpdateOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateOsActionPerformed(evt);
            }
        });

        btnRemoveOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/recycle_recyclebin_full_delete_trash_1772.png"))); // NOI18N
        btnRemoveOs.setToolTipText("Excluir uma OS");
        btnRemoveOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRemoveOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveOsActionPerformed(evt);
            }
        });

        BtnFindOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/procurar.png"))); // NOI18N
        BtnFindOs.setToolTipText("Consultar O.S");
        BtnFindOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnFindOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnFindOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtServicoOs)
                            .addComponent(txtDefeitoOs)
                            .addComponent(txtEquipamentoOS, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(183, 183, 183)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(btnAddOs)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(BtnFindOs)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnUpdateOs)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRemoveOs)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtValorOS, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(64, 64, 64)
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtTecnicoOs))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbSituacaoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbSituacaoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36)
                .addComponent(jLabel5)
                .addGap(4, 4, 4)
                .addComponent(txtEquipamentoOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDefeitoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(3, 3, 3)
                .addComponent(txtServicoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtValorOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTecnicoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpdateOs, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRemoveOs)
                    .addComponent(BtnFindOs, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAddOs))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        setBounds(0, 0, 927, 671);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCliOsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliOsKeyReleased
        PesquisaC();
    }//GEN-LAST:event_txtCliOsKeyReleased

    private void tbClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientesMouseClicked
        settar_campos();
    }//GEN-LAST:event_tbClientesMouseClicked

    private void rbOrcamentoOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOrcamentoOsActionPerformed
        tipo = "Orçamento";
    }//GEN-LAST:event_rbOrcamentoOsActionPerformed

    private void rbOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOsActionPerformed
        tipo = "O.S";
    }//GEN-LAST:event_rbOsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        rbOrcamentoOs.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnAddOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOsActionPerformed
        newOs();
    }//GEN-LAST:event_btnAddOsActionPerformed

    private void BtnFindOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnFindOsActionPerformed
        PesquisarOS();

    }//GEN-LAST:event_BtnFindOsActionPerformed

    private void btnUpdateOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateOsActionPerformed
        atualizarOs();
    }//GEN-LAST:event_btnUpdateOsActionPerformed

    private void btnRemoveOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOsActionPerformed
        // Metodo de Excluir uma OS
        excluirOS();
    }//GEN-LAST:event_btnRemoveOsActionPerformed

    private void btnOsCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsCadActionPerformed
        // TODO add your handling code here:
        TelaTodasOS telaOs = new TelaTodasOS();
        telaOs.setVisible(true);
        //this.dispose();

    }//GEN-LAST:event_btnOsCadActionPerformed

    private void txtCliOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliOsActionPerformed
        // TODO add your handling code here:
        PesquisaC();
    }//GEN-LAST:event_txtCliOsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnFindOs;
    private javax.swing.JButton btnAddOs;
    private javax.swing.JButton btnOsCad;
    private javax.swing.JButton btnRemoveOs;
    private javax.swing.JButton btnUpdateOs;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbSituacaoOs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JRadioButton rbOrcamentoOs;
    private javax.swing.JRadioButton rbOs;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliOs;
    private javax.swing.JTextField txtDataOs;
    private javax.swing.JTextField txtDefeitoOs;
    private javax.swing.JTextField txtEquipamentoOS;
    private javax.swing.JTextField txtNumOs;
    private javax.swing.JTextField txtServicoOs;
    private javax.swing.JTextField txtTecnicoOs;
    private javax.swing.JTextField txtValorOS;
    // End of variables declaration//GEN-END:variables
}
