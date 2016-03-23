
package xatezin;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luan Medeiros
 */
public class Cliente {
    private static final String servidor = "127.0.0.1";
    
    //Método que loga o usuário e retorna um o usuário com o ID gerado no servidor.
    public static Usuario criaUsuario(Usuario user) throws Exception
    {
            Usuario userReturn = null;
            Socket cliente = new Socket(servidor, 2005);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());            
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());            
            saida.writeObject(user);
            userReturn = (Usuario) entrada.readObject();
            saida.close();
            entrada.close();
            cliente.close();

            return userReturn;
    }
    
    //Resgata todos os usuários logados no servidor
    public static ArrayList<Usuario> buscaUsuariosLogados()
    {
            ArrayList<Usuario> usuariosReturn = null;
            ObjectInputStream entrada; 
            Socket cliente = null;

        try {
            cliente = new Socket(servidor, 3005);  
            entrada = new ObjectInputStream(cliente.getInputStream());            
            usuariosReturn = (ArrayList<Usuario>) entrada.readObject();
            entrada.close();
            cliente.close();  
            return usuariosReturn;    
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
       return null;                 
    }
    
    //Método que resgata no servidor as mensagens privadas do usuário
    public static ArrayList<Mensagem> buscaMensagensPrivadas(Usuario user)
    {
            ArrayList<Mensagem> mensagensReturn = null;
            ObjectInputStream entrada; 
            Socket cliente = null;
            ObjectOutputStream saida = null;

        try {
            cliente = new Socket(servidor, 4005);  
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(user);
            entrada = new ObjectInputStream(cliente.getInputStream());            
            mensagensReturn = (ArrayList<Mensagem>) entrada.readObject();
            saida.close();
            entrada.close();
            cliente.close();  
            return mensagensReturn;    
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return null;
    }
    
    //Método que resgata no servidor as mensagens do grupo
    public static ArrayList<Mensagem> buscaMensagensGrupo(Usuario user)
    {
            ArrayList<Mensagem> mensagensReturn = null;
            ObjectInputStream entrada; 
            Socket cliente = null;
            ObjectOutputStream saida = null;

        try {
            cliente = new Socket(servidor, 7010);  
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(user);
            entrada = new ObjectInputStream(cliente.getInputStream());            
            mensagensReturn = (ArrayList<Mensagem>) entrada.readObject();
            saida.close();
            entrada.close();
            cliente.close();  
            return mensagensReturn;    
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return null;
    }
    
     public static void enviaMensagemPrivada(Mensagem msg)
    {
            Socket cliente = null;
            ObjectOutputStream saida = null;

        try {
            cliente = new Socket(servidor, 6005);  
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(msg);            
            saida.close();            
            cliente.close();                   
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }                         
    }   
     
     //Método que envia mensagem para o grupo
     public static void enviaMensagemGrupo(Mensagem msg)
    {            
            Socket cliente = null;
            ObjectOutputStream saida = null;

        try {
            cliente = new Socket(servidor, 7005);  
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(msg);            
            saida.close();            
            cliente.close();                   
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }                                       
    }       

     //Método que realiza o logout do usuário após ele sair do chat
     public static void logout(Usuario user)
    {            
            Socket cliente;            
        try {
            cliente = new Socket(servidor, 5005);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());                       
            saida.writeObject(user);
            saida.close();
            cliente.close();              
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
                   
    }

    

     
}
