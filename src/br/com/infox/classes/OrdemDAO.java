package br.com.infox.classes;

import java.sql.*;
import br.com.infox.dao.ModuloConexao;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author caiomagno
 */
public class OrdemDAO {

    Connection conexao = null;
    PreparedStatement pst;
    ResultSet rs;

    public OrdemDAO() {

    }

    //Metodo para obter os dados do banco de dados
    //Ele é chamado no metodo readOs() para carregar a tabela na tela de todas OS
    public ArrayList<OrdServ> listaOrdem() {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.valor, c.fonecli, o.data_os, o.tecnico from tbos as o inner join tbcliente as c on (o.idcli= c.idcli) order by data_os desc";

        ArrayList<OrdServ> ordem = new ArrayList<>();

        try {
            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli")); //1
                o.setOs(rs.getInt("o.os")); //2
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao")); //3
                o.setEquipamento(rs.getString("o.equipamento")); //4
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico")); //5
                o.setValor(rs.getFloat("o.valor")); //6
                o.setTelefone(rs.getString("c.fonecli"));
                o.setData(rs.getString("o.data_os")); //7
                o.setTecnico(rs.getString("o.tecnico")); //8

                ordem.add(o);

            }

            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro na listagem das OS: " + e);
        }
        return ordem;
    }

    //Metodo semelhante ao anterior porém esse é chamado na tela de relatório de serviços
    public ArrayList<OrdServ> relServico(String p) {
        String sql = "select c.nomecli, o.os, o.tipo, o.situacao, o.equipamento,o.defeito, o.servico, o.valor, c.fonecli, o.data_os, tecnico from tbos as o inner join tbcliente as c on (o.idcli= c.idcli) order by data_os desc";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, p);
            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ ord = new OrdServ();

                ord.setNome(rs.getString("c.nomecli"));
                ord.setOs(rs.getInt("o.os"));
                ord.setTipo(rs.getString("o.tipo"));
                ord.setSituacao(rs.getString("o.situacao"));
                ord.setEquipamento(rs.getString("o.equipamento"));
                ord.setDefeito(rs.getString("o.defeito"));
                ord.setServico(rs.getString("o.servico"));
                ord.setValor(rs.getFloat("o.valor"));
                ord.setTelefone(rs.getString("c.fonecli"));
                ord.setData(rs.getString("o.data_os"));
                ord.setTecnico(rs.getString("o.tecnico"));

                lista.add(ord);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conexao.close();
                pst.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao montar relatório: " + e);
            }
        }
        return lista;
    }

    //Metodo responsavel por pesquisar por uma OS através do nome do Cliente
    //Esse metodo lista todas OS de um nome buscado pelo usuário
    public ArrayList<OrdServ> pequisa(String p) {
        String sql = "select c.nomecli, o.os, o.situacao, o.equipamento, o.servico, o.valor, c.fonecli, o.data_os, tecnico from tbos as o inner join tbcliente as c  on (o.idcli = c.idcli) where nomecli LIKE ? order by data_os desc";

        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setServico(rs.getString("o.servico"));
                o.setValor(rs.getFloat("o.valor"));
                o.setTelefone(rs.getString("c.fonecli"));
                o.setData(rs.getString("o.data_os"));
                o.setTecnico(rs.getString("o.tecnico"));

                lista.add(o);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Em Pesquisa OS: " + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }

        return lista;
    }

    //Metodo responsavel por buscar um OS pelo nome do cliente no relatório
    public ArrayList<OrdServ> BuscaRelNome(String p) {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.tecnico, o.valor from tbos as o inner join tbcliente as c on(o.idcli = c.idcli) where c.nomecli like ?";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico"));
                o.setTecnico(rs.getString("o.tecnico"));
                o.setValor(rs.getFloat("o.valor"));

                lista.add(o);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Em Busca Relatório Serviços por Cliente :" + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }

        return lista;
    }

    // Esse metodo busca uma OS pelo numero dela
    public ArrayList<OrdServ> buscaOs(String p) {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.tecnico, o.valor from tbos as o inner join tbcliente as c on(o.idcli = c.idcli) where o.os =?";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, p);

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico"));
                o.setTecnico("o.tecnico");
                o.setValor(rs.getFloat("o.valor"));

                lista.add(o);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro em Buscar OS por número: " + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }
        return lista;
    }

    public ArrayList<OrdServ> buscaOsTipo(String p) {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.tecnico, o.valor from tbos as o inner join tbcliente as c on(o.idcli = c.idcli) where o.tipo like ?";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico"));
                o.setTecnico("o.tecnico");
                o.setValor(rs.getFloat("o.valor"));

                lista.add(o);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro em Buscar OS por Tipo: " + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }
        return lista;
    }

    public ArrayList<OrdServ> buscaOsSit(String p) {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.tecnico, o.valor from tbos as o inner join tbcliente as c on(o.idcli = c.idcli) where o.situacao like ?";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico"));
                o.setTecnico("o.tecnico");
                o.setValor(rs.getFloat("o.valor"));

                lista.add(o);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro em Buscar OS por situacão: " + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }
        return lista;
    }

    public ArrayList<OrdServ> BuscaRelTec(String p) {
        String sql = "select c.nomecli, o.os, o.data_os, o.tipo, o.situacao, o.equipamento, o.defeito, o.servico, o.tecnico, o.valor from tbos as o inner join tbcliente as c on(o.idcli = c.idcli) where o.tecnico like ?";
        ArrayList<OrdServ> lista = new ArrayList<>();

        conexao = ModuloConexao.conector();

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                OrdServ o = new OrdServ();

                o.setNome(rs.getString("c.nomecli"));
                o.setOs(rs.getInt("o.os"));
                o.setData(rs.getString("o.data_os"));
                o.setTipo(rs.getString("o.tipo"));
                o.setSituacao(rs.getString("o.situacao"));
                o.setEquipamento(rs.getString("o.equipamento"));
                o.setDefeito(rs.getString("o.defeito"));
                o.setServico(rs.getString("o.servico"));
                o.setTecnico(rs.getString("o.tecnico"));
                o.setValor(rs.getFloat("o.valor"));

                lista.add(o);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Em Busca Relatório Serviços, Técnico :" + e);
        }
        try {
            conexao.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }

        return lista;
    }

}
