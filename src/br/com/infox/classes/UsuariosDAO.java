package br.com.infox.classes;

import java.sql.*;
import br.com.infox.dao.ModuloConexao;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author caiomagno
 */
public class UsuariosDAO {

    Connection conexao;
    PreparedStatement pst;
    ResultSet rs;

    public UsuariosDAO() {
    }

    public ArrayList<Usuarios> listaUsuarios() {

        String sql = "select * from tbusuarios";

        ArrayList<Usuarios> lista = new ArrayList<>();

        try {

            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Usuarios user = new Usuarios();
                user.setIdUser(rs.getInt("id_user"));
                user.setNome(rs.getString("usuario"));
                user.setTel(rs.getString("fone"));
                user.setLogin(rs.getString("login"));
                user.setPass(rs.getString("senha"));
                user.setPerfil(rs.getString("tipo"));
                lista.add(user);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao captar usuarios: " + e);
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

    public ArrayList<Usuarios> pesquisaUsuarios(String p) {

        String sql = "select id_user, usuario, fone, login, senha, tipo from tbusuarios where usuario like ?";

        ArrayList<Usuarios> lista = new ArrayList<>();

        try {

            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");

            rs = pst.executeQuery();

            while (rs.next()) {
                Usuarios user = new Usuarios();
                user.setIdUser(rs.getInt("id_user"));
                user.setNome(rs.getString("usuario"));
                user.setTel(rs.getString("fone"));
                user.setLogin(rs.getString("login"));
                user.setPass(rs.getString("senha"));
                user.setPerfil(rs.getString("tipo"));
                lista.add(user);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao captar usuarios: " + e);
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
