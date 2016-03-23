package xatezin;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luan Medeiros
 */
public class Usuario implements Serializable{
    private String nomeUsuario;
    private String idUsuario;
    private ArrayList<Mensagem> mensagens = new ArrayList<>();
    private ArrayList<Mensagem> mensagensGrupo = new ArrayList<>();
    private ArrayList<Mensagem> mensagensChat = new ArrayList<>();
    private boolean Online = true;
    
    //private PrintWriter escritor;

    public Usuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;        

        //this.corUsuario = corUsuario.toUpperCase();
        //this.escritor =  escritor;
    }
    
    
/*
    public String getCorUsuario() {
        return corUsuario;
    }

    public void setCorUsuario(String corUsuario) {
        this.corUsuario = corUsuario;
    }*/

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }       

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    @Override
    public String toString() {
        return "Usuario{" + "nomeUsuario=" + nomeUsuario + ", idUsuario=" + idUsuario + '}';
    }

    public ArrayList<Mensagem> getMensagensGrupo() {
        return mensagensGrupo;
    }

    public ArrayList<Mensagem> getMensagensChat() {
        return mensagensChat;
    }

    public void setMensagensChat(ArrayList<Mensagem> mensagensChat) {
        this.mensagensChat = mensagensChat;
    }

    public boolean isOnline() {
        return Online;
    }

    public void setOnline(boolean Online) {
        this.Online = Online;
    }

    
            
}
