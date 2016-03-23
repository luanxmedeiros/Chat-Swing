/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xatezin;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Luan Medeiros
 */
public class Mensagem implements Serializable, Cloneable {
    private String remetente;
    private String mensagem;
    private String destinatario;
    private String horaMsg = "";
    private String dataMsg = "";
    private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");             
    private SimpleDateFormat sdfhora = new SimpleDateFormat("HH:mm");  

    public Mensagem(String remetente, String mensagem, String destinatario) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
        long tempo = System.currentTimeMillis();
        Date tempoDate = new Date(tempo);                
        dataMsg = sdfData.format(tempoDate);
        horaMsg = sdfhora.format(tempoDate);
    }

    public String getMensagem() {
        return mensagem;
    }
    
    @Override  
    protected Mensagem clone() throws CloneNotSupportedException {  
        Mensagem clonada = new Mensagem(this.remetente, this.mensagem, this.destinatario);  
        return clonada;  
    }      

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }        

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getHoraMsg() {
        return horaMsg;
    }

    public void setHoraMsg(String horaMsg) {
        this.horaMsg = horaMsg;
    }

    public String getDataMsg() {
        return dataMsg;
    }

    public void setDataMsg(String dataMsg) {
        this.dataMsg = dataMsg;
    }
    
    
}
