package br.com.infox.dal;

import java.sql.*;


/**
 *
 * @author caiomagno
 */
public class ModuloConexao {
    //Metodo responsavel por estabelecer conecao com o BD

         
    public static Connection conector() {
        Connection conexao = null;
        

        //A linha abaixo chama o driver
        final String driver = "com.mysql.cj.jdbc.Driver";
        // Armazenando informações referentes ao banco
        final String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "Cm#210690";
        // Estabelecendo a Conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Criação da Base de Dados de nome ControleOsDB
    /*public static Connection criaDB()
    {
       
    }*/
    
}
