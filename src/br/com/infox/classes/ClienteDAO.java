package br.com.infox.classes;

import br.com.infox.dao.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author caiomagno
 * @version Beta
 */
public class ClienteDAO {

    Connection con = null;
    PreparedStatement pst;
    ResultSet rs;

    public ClienteDAO() {

    }

    public ArrayList<Clientes> listaClientes() {
        String sql = "select * from tbcliente order by nomecli";

        ArrayList<Clientes> cli = new ArrayList<>();

        try {
            con = ModuloConexao.conector();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Clientes cliente = new Clientes();

                cliente.setIdCli(rs.getInt("idcli"));
                cliente.setNomeCli(rs.getString("nomecli"));
                cliente.setEndCli(rs.getString("endcli"));
                cliente.setFoneCli(rs.getString("fonecli"));
                cliente.setEmailCli(rs.getString("emailcli"));

                //Populando o ArrayList
                cli.add(cliente);
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {

        }
        return cli;
    }

    public ArrayList<Clientes> pesquisar(String p) {
        String sql = "select * from tbcliente where nomecli LIKE ?";
        ArrayList<Clientes> lista = new ArrayList<>();
        con = ModuloConexao.conector();

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, "%" + p + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                Clientes cli = new Clientes();

                cli.setIdCli(rs.getInt("idcli"));
                cli.setNomeCli(rs.getString("nomecli"));
                cli.setEndCli("endcli");
                cli.setFoneCli(rs.getString("fonecli"));
                cli.setEmailCli("emailcli");

                lista.add(cli);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Em pesquisar cliente: " + e);
        }
        try {
            con.close();
            pst.close();
            rs.close();
        } catch (SQLException r) {
            JOptionPane.showMessageDialog(null, r);
        }

        return lista;
    }

}
