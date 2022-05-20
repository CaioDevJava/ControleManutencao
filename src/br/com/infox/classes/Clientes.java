package br.com.infox.classes;

/**
 *
 * @author caiomagno
 */
public class Clientes {

    //Atributos da Classe
     private int idCli;
     private String nomeCli,
            endCli,
            foneCli,
            emailCli;
    

    public Clientes() {
       
    }
    
    

    //Metodos construtores
    public int getIdCli() {
        return idCli;
    }

    public void setIdCli(int idCli) {
        this.idCli = idCli;
    }

    public String getNomeCli() {
        return nomeCli;
    }

    public void setNomeCli(String nomeCli) {
        this.nomeCli = nomeCli;
    }

    public String getEndCli() {
        return endCli;
    }

    public void setEndCli(String endCli) {
        this.endCli = endCli;
    }

    public String getFoneCli() {
        return foneCli;
    }

    public void setFoneCli(String foneCli) {
        this.foneCli = foneCli;
    }

    public String getEmailCli() {
        return emailCli;
    }

    public void setEmailCli(String emailCli) {
        this.emailCli = emailCli;
    }

}
