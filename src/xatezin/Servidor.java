
package xatezin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Luan Medeiros
 */
public class Servidor {

    public static int idUsuario = 1;
    public static ArrayList<Usuario> usuarios = new ArrayList<>();
    public static Thread thread1;
    public static Thread thread2;
    public static Thread thread3;
    public static Thread thread4; 
    public static Thread thread5;    
    public static Thread thread6;    
    public static Thread thread7;        


    public static void mandarMensagem(Mensagem msg) {
        for (Usuario user : usuarios) {
            if (user.getIdUsuario().equals(msg.getDestinatario())) {
                user.getMensagens().add(msg);
                break;
            }
        }

    }
    
    public static void mandarMensagemGrupo(Mensagem msg) {
        for (Usuario user : usuarios)
        {            
                user.getMensagensGrupo().add(msg);                            
        }

    }

    public static void logout(Usuario userLogout) {
        for (Usuario user : usuarios) {
            if (user.getIdUsuario().equals(userLogout.getIdUsuario())) {
                user.setOnline(false);
                break;
            }
            
        }        
    }

    public static List<Mensagem> getMensagensPrivadas(String idUsuario) {
        List<Mensagem> mensagens = null;
        for (Usuario user : usuarios) {
            if (user.getIdUsuario().equals(idUsuario)) {

                mensagens = new ArrayList<Mensagem>(user.getMensagens().size());
                for (Mensagem item : user.getMensagens()) {
                    try {
                        mensagens.add(item.clone());
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                user.getMensagens().clear();
                break;
            }                                    
        }
        return mensagens;
    }

    public static List<Mensagem> getMensagensGrupo(Usuario userParam) {
        List<Mensagem> mensagens = null;
        for (Usuario user : usuarios) {
            if (user.getIdUsuario().equals(userParam.getIdUsuario())) {

                mensagens = new ArrayList<Mensagem>(user.getMensagensGrupo().size());
                for (Mensagem item : user.getMensagensGrupo()) {
                    try {
                        mensagens.add(item.clone());
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                user.getMensagensGrupo().clear();
                break;
            }                                  
        }
        return mensagens;
    }
    
    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }
    
    

    public static void main(String[] args) throws IOException, ClassNotFoundException {


            //Socket para adição de usuários
            ServerSocket serverSocketAddUsuario = new ServerSocket(2005);            

            //Socket para consulta de usuários
            ServerSocket serverSocketUsuarios = new ServerSocket(3005);

            //Socket para consulta de mensagens privadas
            ServerSocket serverSocketMensagensPrivadas = new ServerSocket(4005);

            //Socket para logout
            ServerSocket serverSocketLogout = new ServerSocket(5005);

            //Socket para envio de mensagens privadas
            ServerSocket serverSocketEnviaMsg = new ServerSocket(6005);
            
            //Socket para envio de mensagens para o grupo
            ServerSocket serverSocketEnviaMsgGrupo = new ServerSocket(7005);
            
            //Socket para consulta de mensagens do grupo
            ServerSocket serverSocketMensgensGrupo = new ServerSocket(7010);
            

            //Thread que trata as requesições de adição de usuários
            thread1 = new Thread(new Runnable() {            
            @Override
            public void run() 
            {            
                while(true)
                {                
                    Socket clienteSocketAddUsuario = null;
                    try {
                        clienteSocketAddUsuario = serverSocketAddUsuario.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(!clienteSocketAddUsuario.isClosed())
                    {
                        ObjectInputStream entrada = null;   
                        ObjectOutputStream saida = null;   
                        Usuario user = null;
                        try {
                            entrada = new ObjectInputStream(clienteSocketAddUsuario.getInputStream());
                            saida = new ObjectOutputStream(clienteSocketAddUsuario.getOutputStream());
                            user = (Usuario) entrada.readObject();
                            user.setIdUsuario(idUsuario+"");
                            usuarios.add(user);                                                        
                            idUsuario++;
                            saida.writeObject(user);
                            saida.close();
                            entrada.close();
                            clienteSocketAddUsuario.close();
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }                 
            }});
            thread1.start();
            
            //Thread que trata as requesições de consultas de usuários
            thread2 = new Thread(new Runnable() {            
            @Override
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                    try {
                        clienteSocket = serverSocketUsuarios.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(!clienteSocket.isClosed())
                    {  
                        try
                        {
                        ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
                        out.writeObject(getUsuarios());
                        out.close();
                        clienteSocket.close();
                        }
                        catch(Exception ex)
                       {
                           System.out.println("DEU RUIM"); 
                       }
                    }
                 }               
            }});            
            thread2.start();
            
            //Thread que trata as requesições de consultas de mensagens privadas
            thread3 = new Thread(new Runnable() {            
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                    try {
                        clienteSocket = serverSocketMensagensPrivadas.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(!clienteSocket.isClosed())
                    {  
                        try
                        {
                        ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(clienteSocket.getInputStream());
                        Usuario user =  (Usuario) in.readObject();                        
                        out.writeObject(getMensagensPrivadas(user.getIdUsuario()));
                        in.close();
                        out.close();
                        clienteSocket.close();
                        }
                        catch(Exception ex)
                       {
                           System.out.println("DEU RUIM"); 
                       }
                    }
                 }               
            }});            
            thread3.start();           
            
            //Thread que trata as requesições de logout
            thread4 = new Thread(new Runnable() {            
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                     ObjectInputStream entrada = null;
                    try {
                        clienteSocket = serverSocketLogout.accept();
                        if(!clienteSocket.isClosed())
                        {
                            entrada = new ObjectInputStream(clienteSocket.getInputStream());                                
                            Usuario user = (Usuario) entrada.readObject();
                            logout(user);
                            entrada.close();
                            clienteSocket.close();
                        }                         
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                 }               
            }});            
            thread4.start();              
            
            //Thread que trata os envios de mensagens privadas
            thread5 = new Thread(new Runnable() {            
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                     ObjectInputStream mensagemEntrada = null;
                     Mensagem mensagem = null;
                    try {
                        clienteSocket = serverSocketEnviaMsg.accept();
                        if(!clienteSocket.isClosed()){            
                            mensagemEntrada = new ObjectInputStream(clienteSocket.getInputStream());
                            mensagem = (Mensagem) mensagemEntrada.readObject();
                            if (mensagem != null) {
                                mandarMensagem(mensagem);
                            }
                            mensagemEntrada.close();
                            clienteSocket.close();
                        }                      
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                 }               
            }});            
            thread5.start();   

            //Thread que trata os envios de mensagens ao grupo
            thread6 = new Thread(new Runnable() {            
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                     ObjectInputStream mensagemEntrada = null;
                     Mensagem mensagem = null;
                    try {
                        clienteSocket = serverSocketEnviaMsgGrupo.accept();
                        if(!clienteSocket.isClosed()){            
                            mensagemEntrada = new ObjectInputStream(clienteSocket.getInputStream());
                            mensagem = (Mensagem) mensagemEntrada.readObject();                            
                            if (mensagem != null) {
                                mandarMensagemGrupo(mensagem);
                            }
                            mensagemEntrada.close();
                            clienteSocket.close();
                        }                      
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                 }               
            }});            
            thread6.start(); 
            
            //Thread que trata as consultas de mensagens do grupo
            thread7 = new Thread(new Runnable() {            
            public void run() 
            {            
                while(true)
                 {          
                     Socket clienteSocket = null;
                     ObjectInputStream entrada = null;                     
                     ObjectOutputStream saida = null;
                     Usuario usuario = null;
                    try {
                        clienteSocket = serverSocketMensgensGrupo.accept();
                        if(!clienteSocket.isClosed()){            
                            entrada = new ObjectInputStream(clienteSocket.getInputStream());
                            saida = new ObjectOutputStream(clienteSocket.getOutputStream());
                            usuario = (Usuario) entrada.readObject();
                            List<Mensagem> mensagensGrupo = getMensagensGrupo(usuario);
                            saida.writeObject(mensagensGrupo);
                            saida.close();
                            entrada.close();
                            clienteSocket.close();
                        }                      
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                 }               
            }});            
            thread7.start();             
        

    }

}
