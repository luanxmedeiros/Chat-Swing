
package xatezin;


import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;


/**
 *
 * @author Luan Medeiros
 */
public class PainelChat extends javax.swing.JFrame {

    Usuario usuarioLogado = null;
    Usuario usuarioChatPrivado = null;
    String idUsuarioConversa = "";
    ArrayList<Usuario> usuariosOnline = new ArrayList<>();
    Thread requisicoes;
    DefaultListModel modeloLista = null;


    
    //Método usado para inicar as requesições de mensagens em grupo
    public void iniciaRequisicoesMensagensGrupo()
    {
           //Thread que solicita as mensagens em grupo.
            requisicoes = new Thread(new Runnable() {            
            @Override
            public void run() 
            {   
                while(true)
                {   ArrayList<Mensagem> mensagens = null;
                    mensagens = Cliente.buscaMensagensGrupo(usuarioLogado);
                    if(!mensagens.isEmpty())
                    {
                        for(Mensagem msg : mensagens)
                        {
                            mensagensGrupo.append(msg.getDataMsg()+" as "+msg.getHoraMsg()+"\n"+msg.getRemetente()+" tagarelando: "+msg.getMensagem()+"\n\n");
                             mensagensGrupo.setCaretPosition(  mensagensGrupo.getDocument().getLength() );
                        }                      
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PainelChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
            }});
            requisicoes.start();
    }
    
    //Método que chama todos as mensagens privadas 
     public void iniciaRequisicoesMensagensPrivado()
    {
           //Thread que solicita as mensagens em grupo.
            requisicoes = new Thread(new Runnable() {            
            @Override
            public void run() 
            {   
                while(true)
                {   ArrayList<Mensagem> mensagens = null;
                    mensagens = Cliente.buscaMensagensPrivadas(usuarioLogado);
                    if(!mensagens.isEmpty())
                    {
                        for(Mensagem msg : mensagens)
                        {
                            for(Usuario user : usuariosOnline)
                            {
                                if(msg.getRemetente().equals(user.getIdUsuario()))
                                {
                                    msg.setRemetente(user.getNomeUsuario());
                                    user.getMensagensChat().add(msg);
                                    break;
                                }
                            }                            
                        }
                        exibirConversaPrivada();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PainelChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
            }});
            requisicoes.start();
    }
    
   //Método usado para inicar as requesições de busca de usuários
    public void iniciaRequisicoesUsuarios()
    {
           //Thread que solicita solicita os usuários
            requisicoes = new Thread(new Runnable() {            
            @Override
            public void run() 
            {   
                while(true)
                    
                {   ArrayList<Usuario> usuariosLogados = null;
                    usuariosLogados = Cliente.buscaUsuariosLogados();
                    if(!usuariosLogados.isEmpty() && usuariosLogados != null)
                    {
                       
                        for(Usuario user : usuariosLogados)
                        {
                            if(buscaUsuarioExistente(user)==false && !user.getIdUsuario().equals(usuarioLogado.getIdUsuario()) && user.isOnline()==true)
                            {
                               usuariosOnline.add(user);                               
                               modeloLista.add(modeloLista.size(), user.getNomeUsuario() + " - ONLINE");                                                              
                            }
                            else if(buscaUsuarioExistente(user)==true && user.isOnline()==false && !user.getIdUsuario().equals(usuarioLogado.getIdUsuario()))
                            {
                                for(Usuario userOff : usuariosOnline) 
                                {
                                    if(userOff.getIdUsuario().equals(user.getIdUsuario())) 
                                    {
                                        userOff.setOnline(false);                                        
                                        break;
                                    }
                                }                                        
                            }
                            
                        }
                        atualizaListaUsuariosOnline();
                        listaUsuarios.setModel(modeloLista);
                        listaUsuariosGrupo.setModel(modeloLista);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PainelChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
            }});
            requisicoes.start();
    }
    
    //Método para exibir a conversa privada escolhida
    public void exibirConversaPrivada()            
    {   
        if(usuarioChatPrivado != null)
        {       mensagensPrivada.setText("");
                for(Mensagem msg : usuarioChatPrivado.getMensagensChat())
                {
                mensagensPrivada.append(msg.getDataMsg()+" as "+msg.getHoraMsg()+"\n"+msg.getRemetente()+" tagarelando: "+msg.getMensagem()+"\n\n");
                mensagensPrivada.setCaretPosition(mensagensPrivada.getDocument().getLength());                
                }
        }
        /*for(Usuario userOn : usuariosOnline)
        {
            if(userOn.getIdUsuario().equals(usuarioChatPrivado.getIdUsuario()))
            {   mensagensPrivada.setText("");
                for(Mensagem msg : userOn.getMensagensChat())
                {
                mensagensPrivada.append(msg.getDataMsg()+" as "+msg.getHoraMsg()+"\n"+msg.getRemetente()+" tagarelando: "+msg.getMensagem()+"\n\n");
                mensagensPrivada.setCaretPosition(mensagensPrivada.getDocument().getLength());                
                }
                break;
            }
        }   */                          
    }
    
    public void atualizaListaUsuariosOnline()
    {
        for(int i = 0; i < usuariosOnline.size(); i++)
        {
            if(usuariosOnline.get(i).isOnline()==false)
            {                
                modeloLista.set(i, "<html><font color=\"red\">"+usuariosOnline.get(i).getNomeUsuario() +" - OFFLINE </font></html>" );
                
            }
        }        
    }
    
    public boolean buscaUsuarioExistente(Usuario user)
    {
        for(Usuario usuario : usuariosOnline)
        {
            if(usuario.getIdUsuario().equals(user.getIdUsuario()))
            {
                return true;
            }
        }
        return false;
    }
    /** Creates new form PainelChat */
    
    
    public PainelChat() {        
       initComponents(); 
        //Pegando caminho relativo
        String diretorioString = System.getProperty("user.dir");        
        diretorioString = diretorioString.replace("\\", "/");
        diretorioString += "/src/xatezin/";              
       imagemChat.setIcon(new javax.swing.ImageIcon(diretorioString+"GifChat.gif"));
       modeloLista =  new DefaultListModel();       
       PainelChat.setVisible(false);      
    }
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        PainelChat = new javax.swing.JTabbedPane();
        Privado = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mensagensPrivada = new javax.swing.JTextArea();
        textoMensagemPrivada = new javax.swing.JTextField();
        enviarMensagemPrivado = new javax.swing.JButton();
        ListaContados = new javax.swing.JScrollPane();
        listaUsuarios = new javax.swing.JList<>();
        usuarioAtual = new javax.swing.JLabel();
        Grupo = new javax.swing.JPanel();
        enviarMensagemGrupo = new javax.swing.JButton();
        textoMensagemGrupo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        mensagensGrupo = new javax.swing.JTextArea();
        ListaContadosGrupo = new javax.swing.JScrollPane();
        listaUsuariosGrupo = new javax.swing.JList<>();
        TelaLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nick = new javax.swing.JTextField();
        botaoLogar = new javax.swing.JButton();
        imagemChat = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        PainelChat.setBackground(new java.awt.Color(255, 255, 255));
        PainelChat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setAutoscrolls(true);

        mensagensPrivada.setEditable(false);
        mensagensPrivada.setColumns(20);
        mensagensPrivada.setLineWrap(true);
        mensagensPrivada.setRows(5);
        jScrollPane2.setViewportView(mensagensPrivada);

        textoMensagemPrivada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textoMensagemPrivadaKeyReleased(evt);
            }
        });

        enviarMensagemPrivado.setText("ENVIAR");
        enviarMensagemPrivado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarMensagemPrivadoActionPerformed(evt);
            }
        });

        listaUsuarios.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        listaUsuarios.setForeground(new java.awt.Color(0, 0, 255));
        listaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaUsuariosMouseClicked(evt);
            }
        });
        ListaContados.setViewportView(listaUsuarios);

        usuarioAtual.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        usuarioAtual.setForeground(new java.awt.Color(0, 51, 204));

        javax.swing.GroupLayout PrivadoLayout = new javax.swing.GroupLayout(Privado);
        Privado.setLayout(PrivadoLayout);
        PrivadoLayout.setHorizontalGroup(
            PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrivadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PrivadoLayout.createSequentialGroup()
                        .addComponent(usuarioAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PrivadoLayout.createSequentialGroup()
                        .addGroup(PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PrivadoLayout.createSequentialGroup()
                                .addComponent(textoMensagemPrivada, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(enviarMensagemPrivado, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ListaContados, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PrivadoLayout.setVerticalGroup(
            PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrivadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usuarioAtual, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ListaContados, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(28, 28, 28)
                .addGroup(PrivadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(enviarMensagemPrivado, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(textoMensagemPrivada))
                .addContainerGap())
        );

        PainelChat.addTab("Privado", Privado);
        Privado.getAccessibleContext().setAccessibleName("Privado");

        enviarMensagemGrupo.setText("ENVIAR");
        enviarMensagemGrupo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarMensagemGrupoActionPerformed(evt);
            }
        });

        textoMensagemGrupo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textoMensagemGrupoKeyReleased(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);

        mensagensGrupo.setEditable(false);
        mensagensGrupo.setColumns(1);
        mensagensGrupo.setLineWrap(true);
        mensagensGrupo.setRows(5);
        mensagensGrupo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(mensagensGrupo);

        ListaContadosGrupo.setViewportView(listaUsuariosGrupo);

        javax.swing.GroupLayout GrupoLayout = new javax.swing.GroupLayout(Grupo);
        Grupo.setLayout(GrupoLayout);
        GrupoLayout.setHorizontalGroup(
            GrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GrupoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GrupoLayout.createSequentialGroup()
                        .addComponent(textoMensagemGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enviarMensagemGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(GrupoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ListaContadosGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                .addContainerGap())
        );
        GrupoLayout.setVerticalGroup(
            GrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GrupoLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(GrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ListaContadosGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GrupoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textoMensagemGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(enviarMensagemGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        PainelChat.addTab("Grupo", Grupo);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("DIGITE SEU NICK PRA BATERMOS UMA PROSA...");

        nick.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nick.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        botaoLogar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        botaoLogar.setText("LOGAR");
        botaoLogar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLogarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TelaLoginLayout = new javax.swing.GroupLayout(TelaLogin);
        TelaLogin.setLayout(TelaLoginLayout);
        TelaLoginLayout.setHorizontalGroup(
            TelaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TelaLoginLayout.createSequentialGroup()
                .addGroup(TelaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TelaLoginLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(TelaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botaoLogar, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nick, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(TelaLoginLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(imagemChat, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        TelaLoginLayout.setVerticalGroup(
            TelaLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TelaLoginLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nick, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botaoLogar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imagemChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLayeredPane1.setLayer(PainelChat, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(TelaLogin, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PainelChat)
                .addContainerGap())
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TelaLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(PainelChat)
                .addGap(6, 6, 6))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(TelaLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //Enter para envio de mensagens em grupo pelo botão
    private void enviarMensagemGrupoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarMensagemGrupoActionPerformed
        if(!textoMensagemGrupo.getText().equals(""))
        {          
          Cliente.enviaMensagemGrupo(new Mensagem(usuarioLogado.getNomeUsuario(), textoMensagemGrupo.getText(), "Grupo"));          
          textoMensagemGrupo.setText("");
        }        
    }//GEN-LAST:event_enviarMensagemGrupoActionPerformed

    //Enter para envio de mensagens em grupo
    private void textoMensagemGrupoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoMensagemGrupoKeyReleased
        if(evt.getKeyCode() ==10 && !textoMensagemGrupo.getText().equals(""))
        {
          //mensagensGrupo.setText(mensagensGrupo.getText()+textoMensagemGrupo.getText());
       //   mensagensGrupo.append(textoMensagemGrupo.getText()+"\n\n");
          Cliente.enviaMensagemGrupo(new Mensagem(usuarioLogado.getNomeUsuario(), textoMensagemGrupo.getText(), "Grupo"));
          //mensagensGrupo.setText(mensagensGrupo.getText()+textoMensagemGrupo.getText()+"\n");
          textoMensagemGrupo.setText("");
        }
         
    }//GEN-LAST:event_textoMensagemGrupoKeyReleased
    
    //Evento do botão logar que coloca o usuário no chat
    private void botaoLogarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLogarActionPerformed
       if(!nick.getText().equals(""))
       {
           PainelChat.setVisible(true);
           TelaLogin.setVisible(false);
           usuarioLogado = new Usuario(nick.getText());
           try {
               usuarioLogado = Cliente.criaUsuario(usuarioLogado);               
           } catch (Exception ex) {
               Logger.getLogger(PainelChat.class.getName()).log(Level.SEVERE, null, ex);
           }
           iniciaRequisicoesMensagensGrupo();
           iniciaRequisicoesUsuarios();
           iniciaRequisicoesMensagensPrivado();
       }
    }//GEN-LAST:event_botaoLogarActionPerformed

    //Evento para envio de mensagens em privado pela tecla enter
    private void textoMensagemPrivadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoMensagemPrivadaKeyReleased

        if(evt.getKeyCode() ==10 && !textoMensagemPrivada.getText().equals("") &&  usuarioChatPrivado != null)
        { 
          if(usuarioChatPrivado.isOnline()==true)
          {
            usuarioChatPrivado.getMensagensChat().add(new Mensagem(usuarioLogado.getNomeUsuario(), textoMensagemPrivada.getText(), usuarioChatPrivado.getIdUsuario()));
            Cliente.enviaMensagemPrivada(new Mensagem(usuarioLogado.getIdUsuario(), textoMensagemPrivada.getText(), usuarioChatPrivado.getIdUsuario()));
            textoMensagemPrivada.setText("");
            exibirConversaPrivada();
          }
          else
          {
              exibirConversaPrivada();
              mensagensPrivada.append("USUÁRIO OFFLINE - IMPOSSÍVEL MANDAR MENSAGEM!\n\n");
              textoMensagemPrivada.setText("");
              
          }
        }
        
    }//GEN-LAST:event_textoMensagemPrivadaKeyReleased

    //Evento para logout assim que o usuário fechar o chat
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Cliente.logout(usuarioLogado);      
    }//GEN-LAST:event_formWindowClosing
    
    //Evento para escolha de usuário para conversa privada
    private void listaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaUsuariosMouseClicked
        //System.out.println("POSIÇÃO "+listaUsuarios.getSelectedIndex());
        if(listaUsuarios.getSelectedIndex() >= 0)
        {   
            
            usuarioChatPrivado = usuariosOnline.get(listaUsuarios.getSelectedIndex());
            usuarioAtual.setText(usuarioChatPrivado.getNomeUsuario());
            if(usuarioChatPrivado != null)
            {
                exibirConversaPrivada();
            }
        }
    }//GEN-LAST:event_listaUsuariosMouseClicked

    //Envio de mensagem privada pelo botão enviar
    private void enviarMensagemPrivadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarMensagemPrivadoActionPerformed
        if(!textoMensagemPrivada.getText().equals("") &&  usuarioChatPrivado != null)
        { 
          if(usuarioChatPrivado.isOnline()==true)
          {
            usuarioChatPrivado.getMensagensChat().add(new Mensagem(usuarioLogado.getNomeUsuario(), textoMensagemPrivada.getText(), usuarioChatPrivado.getIdUsuario()));
            Cliente.enviaMensagemPrivada(new Mensagem(usuarioLogado.getIdUsuario(), textoMensagemPrivada.getText(), usuarioChatPrivado.getIdUsuario()));
            textoMensagemPrivada.setText("");
            exibirConversaPrivada();
          }
          else
          {
              exibirConversaPrivada();
              mensagensPrivada.append("USUÁRIO OFFLINE - IMPOSSÍVEL MANDAR MENSAGEM!\n\n");
              textoMensagemPrivada.setText("");
              
          }
        }        
    }//GEN-LAST:event_enviarMensagemPrivadoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PainelChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PainelChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PainelChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PainelChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PainelChat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Grupo;
    private javax.swing.JScrollPane ListaContados;
    private javax.swing.JScrollPane ListaContadosGrupo;
    private javax.swing.JTabbedPane PainelChat;
    private javax.swing.JPanel Privado;
    private javax.swing.JPanel TelaLogin;
    private javax.swing.JButton botaoLogar;
    private javax.swing.JButton enviarMensagemGrupo;
    private javax.swing.JButton enviarMensagemPrivado;
    private javax.swing.JLabel imagemChat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listaUsuarios;
    private javax.swing.JList<String> listaUsuariosGrupo;
    private javax.swing.JTextArea mensagensGrupo;
    private javax.swing.JTextArea mensagensPrivada;
    private javax.swing.JTextField nick;
    private javax.swing.JTextField textoMensagemGrupo;
    private javax.swing.JTextField textoMensagemPrivada;
    private javax.swing.JLabel usuarioAtual;
    // End of variables declaration//GEN-END:variables
}
