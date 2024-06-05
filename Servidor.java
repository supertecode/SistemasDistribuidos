package trocajsons;
import java.net.*; 
import java.io.*;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Servidor extends Thread
{ 
 protected static boolean serverContinue = true;
 protected Socket clientSocket;

 public static void main(String[] args) throws IOException 
   { 
    ServerSocket serverSocket = null; 

    try { 
         serverSocket = new ServerSocket(21234);
         //serverSocket = new ServerSocket(10008);
         System.out.println ("Connection Socket Created");
         try { 
              while (serverContinue)
                 {
                  serverSocket.setSoTimeout(10000000);
                  System.out.println ("Waiting for Connection");
                  try {
                       new Servidor (serverSocket.accept()); 
                      }
                  catch (SocketTimeoutException ste)
                      {
                       //System.out.println ("Timeout Occurred");
                      }
                 }
             } 
         catch (IOException e) 
             { 
              System.err.println("Accept failed."); 
              System.exit(1); 
             } 
        } 
    catch (IOException e) 
        { 
         System.err.println("Could not listen on port: 10008."); 
         System.exit(1); 
        } 
    finally
        {
         try {
              System.out.println ("Closing Server Connection Socket");
              serverSocket.close(); 
             }
         catch (IOException e)
             { 
              System.err.println("Could not close port: 10008."); 
              System.exit(1); 
             } 
        }
   }

 private Servidor (Socket clientSoc)
   {
    clientSocket = clientSoc;
    start();
   }

 public void run()
   {
    System.out.println ("New Communication Thread Started");

    try { 
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                                      true); 
         BufferedReader in = new BufferedReader( 
                 new InputStreamReader( clientSocket.getInputStream())); 

         String inputLine; 
         //DEFINICAO DO ID DO CANDIDATO
         int idCandidate = 1;
         int idRecruiter = 1;
         while ((inputLine = in.readLine()) != null) 
             { 
              System.out.println ("Server: " + inputLine); 
              Accounts contas = new Accounts();
              RecruiterAccounts contasEmpresas = new RecruiterAccounts();
              Gson gson = new Gson();
              OpGenerica op = (OpGenerica) gson.fromJson(inputLine, OpGenerica.class);
           
           //PARTE DO CANDIDATO   
              if(op.getOperation().equals("LOGIN_CANDIDATE")) {
            	  contas.carregarUsuarios();
            	  System.out.println("Operacao: " + op.getOperation());
            	  String emailLogin = op.getData().getEmail();
            	  Usuario usuarioEncontrado = contas.procurarUsuario(emailLogin);              	  
            	  if(usuarioEncontrado != null) {
            		  System.out.println("email existente");
            		  if(usuarioEncontrado.getSenha().equals(op.getData().getPassword())) {
            			  System.out.println("Login feito com sucesso");
            			  OpGenerica loginSucesso = new OpGenerica(); 
            			  loginSucesso.setOperation("LOGIN_CANDIDATE");
            			  loginSucesso.setStatus("SUCCESS");
            			  loginSucesso.setData();
            			  //GERANDO O TOKEN PARA O LOGIN EFETUADO
            			  Token token = new Token();
            			  String tokenLogin = token.criarToken(usuarioEncontrado.getId(), usuarioEncontrado.getRole(),"DISTRIBUIDOS");
            			  loginSucesso.getData().setToken(tokenLogin);
            			  //
            			  Gson saida = new Gson();
            			  String saidaLogin = saida.toJson(loginSucesso);
            			  out.println(saidaLogin);
            		  }
            		  else {
            			  System.out.println("Credenciais Invalidas");
            			  OpGenerica credencialInvalida = new OpGenerica();
            			  credencialInvalida.setOperation("LOGIN_CANDIDATE");
            			  credencialInvalida.setStatus("INVALID_LOGIN");
            			  credencialInvalida.setData();
            			  Gson sai = new Gson();
            			  String saida = sai.toJson(credencialInvalida);
            			  out.println(saida); 
            			       			  
            		  }
            	  }
            	  else {
        			  System.out.println("Credenciais Invalidas");
        			  OpGenerica credencialInvalida = new OpGenerica();
        			  credencialInvalida.setOperation("LOGIN_CANDIDATE");
        			  credencialInvalida.setStatus("INVALID_LOGIN");
        			  credencialInvalida.setData();
        			  Gson sai = new Gson();
        			  String saida = sai.toJson(credencialInvalida);
        			  out.println(saida); 
            	  }
              }
              
              else if(op.getOperation().equals("LOGOUT_CANDIDATE") || op.getOperation().equals("LOGOUT_RECRUITER")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica logout = new OpGenerica();
            	  if(op.getOperation().equals("LOGOUT_CANDIDATE")) {
            		  logout.setOperation("LOGOUT_CANDIDATE");
            	  }
            	  else {
            		  logout.setOperation("LOGOUT_RECRUITER");
            	  }
            	  Gson gsonLogout = new Gson();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");                 
                      logout.setStatus("SUCCESS");
                      logout.setData();
                      String stringGson = gsonLogout.toJson(logout);
                      out.println(stringGson);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      logout.setStatus("INVALID_TOKEN");
                      logout.setData();
                      String stringGson = gsonLogout.toJson(logout);
                      out.println(stringGson);
                      
                  }
              }
              
              else if(op.getOperation().equals("SIGNUP_CANDIDATE")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  
            	  if(op.getData().getEmail().equals("") || op.getData().getEmail() == null ||
            		 op.getData().getName().equals("") || op.getData().getName() == null ||
            		 op.getData().getPassword().equals("") || op.getData().getPassword() == "") {
            		  
            		  OpGenerica erro = new OpGenerica();
        			  erro.setOperation("SIGNUP_CANDIDATE");
        			  erro.setStatus("INVALID_FIELD");
        			  erro.setData();
        			  Gson erro1 = new GsonBuilder()/*.setPrettyPrinting()*/.create();
        			  String erro2 = erro1.toJson(erro);
        			  out.println(erro2);
            	  }
            	  else {
            		  contas.carregarUsuarios();
            		  Usuario usuarioEncontrado = contas.procurarUsuario(op.getData().getEmail());
            		  if (usuarioEncontrado != null) {
            			  System.out.println("Email já usado: " + usuarioEncontrado);
            			  OpGenerica erro = new OpGenerica();
            			  erro.setOperation("SIGNUP_CANDIDATE");
            			  erro.setStatus("USER_EXISTS");
            			  erro.setData();
            			  Gson erro1 = new GsonBuilder()/*.setPrettyPrinting()*/.create();; 
            			  String erro2 = erro1.toJson(erro);
            			  out.println(erro2);
            		  }
            		  else {
            			  System.out.println("Criando novo cadastro...");
            			  Usuario novoUsuario = new Usuario(op.getData().getEmail(), op.getData().getName(), op.getData().getPassword());
            			  Usuario usuarioId = contas.procurarUsuarioPorId(String.valueOf(idCandidate));
            			  while(usuarioId != null) {
            				  idCandidate = idCandidate + 1;
            				  usuarioId = contas.procurarUsuarioPorId(String.valueOf(idCandidate));
            			  }
            			  //
            			  novoUsuario.setId(String.valueOf(idCandidate));
            			  contas.cadastrarUsuario(novoUsuario);
            			  OpGenerica sucessoCadastro = new OpGenerica();
            			  sucessoCadastro.setOperation("SIGNUP_CANDIDATE");
            			  sucessoCadastro.setStatus("SUCCESS");
            			  sucessoCadastro.setData();
            			  Gson sucesso = new GsonBuilder()/*.setPrettyPrinting()*/.create();; 
            			  String statusSucesso = sucesso.toJson(sucessoCadastro);
            			  System.out.println(statusSucesso);
            			  //isso aqui devera ser apagado
            			  //idCandidate = idCandidate + 1;
            			  out.println(statusSucesso);
            		  
            	     }
                  }
              }
              
              else if(op.getOperation().equals("LOOKUP_ACCOUNT_CANDIDATE")) {
            	  System.out.println("Operacao: " + op.getOperation());           	
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica show = new OpGenerica();
            	  Gson gsonShow = new Gson();
            	  contas.carregarUsuarios();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Usuario usuario = contas.procurarUsuarioPorId(id);
                      show.setOperation("LOOKUP_ACCOUNT_CANDIDATE");
                      show.setStatus("SUCCESS");
                      show.setData();
                      show.getData().setEmail(usuario.getEmail());
                      show.getData().setName(usuario.getNome());
                      show.getData().setPassword(usuario.getSenha());
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      show.setOperation("LOOKUP_ACCOUNT_CANDIDATE");
                      show.setStatus("INVALID_TOKEN");
                      show.setData();
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                      
                  }
            	  
              }
              
              else if(op.getOperation().equals("UPDATE_ACCOUNT_CANDIDATE")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica update = new OpGenerica();
            	  Gson gsonUpdate = new Gson();
            	  contas.carregarUsuarios();
            	  try {
                         String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                   /*   Usuario usuarioEncontrado = contas.procurarUsuario(op.getData().getEmail());
                      if (usuarioEncontrado != null) {
                    	  System.out.println("Email já em uso");
                    	  update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
                    	  update.setStatus("INVALID_EMAIL");
                    	  update.setData();
                    	  String updateRequest = gsonUpdate.toJson(update);
                    	  out.println(updateRequest);
                      }
                      else { */
                    	  Usuario usuario = contas.procurarUsuarioPorId(id);
                    	  usuario.setEmail(op.getData().getEmail());
                    	  usuario.setNome(op.getData().getName());
                    	  usuario.setSenha(op.getData().getPassword());
                    	  contas.atualizarUsuario(usuario);
                    	  update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
                    	  update.setStatus("SUCCESS");
                    	  update.setData();
                    	  String stringGson = gsonUpdate.toJson(update);
                    	  out.println(stringGson);
                      //}
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
                      update.setStatus("INVALID_TOKEN");
                      update.setData();
                      String stringGson = gsonUpdate.toJson(update);
                      out.println(stringGson);
                      
                  }
              }
              
              else if(op.getOperation().equals("DELETE_ACCOUNT_CANDIDATE")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica delete = new OpGenerica();
            	  Gson gsonDelete = new Gson();
            	  contas.carregarUsuarios();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Usuario usuario = contas.procurarUsuarioPorId(id);
                      String email = usuario.getEmail();
                      contas.deletarUsuario(email);
                      delete.setOperation("DELETE_ACCOUNT_CANDIDATE");
                      delete.setStatus("SUCCESS");
                      delete.setData();
                      String stringGson = gsonDelete.toJson(delete);
                      out.println(stringGson);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      delete.setOperation("DELETE_ACCOUNT_CANDIDATE");
                      delete.setStatus("INVALID_TOKEN");
                      delete.setData();
                      String stringGson = gsonDelete.toJson(delete);
                      out.println(stringGson);
                      
                  }
              }
              
              //PARTE DA EMPRESA
              //LOGOUT_RECRUITER JA FOI FEITA
              
              else if(op.getOperation().equals("LOGIN_RECRUITER")) {
              	  contasEmpresas.carregarEmpresas();
            	  System.out.println("Operacao: " + op.getOperation());
            	  String emailLogin = op.getData().getEmail();
            	  Empresa empresaEncontrado = contasEmpresas.procurarEmpresa(emailLogin);              	  
            	  if(empresaEncontrado != null) {
            		  System.out.println("email existente");
            		  if(empresaEncontrado.getSenha().equals(op.getData().getPassword())) {
            			  System.out.println("Login feito com sucesso");
            			  OpGenerica loginSucesso = new OpGenerica(); 
            			  loginSucesso.setOperation("LOGIN_RECRUITER");
            			  loginSucesso.setStatus("SUCCESS");
            			  loginSucesso.setData();
            			  //GERANDO O TOKEN PARA O LOGIN EFETUADO
            			  Token token = new Token();
            			  String tokenLogin = token.criarToken(empresaEncontrado.getId(), empresaEncontrado.getRole(),"DISTRIBUIDOS");
            			  loginSucesso.getData().setToken(tokenLogin);
            			  //
            			  Gson saida = new Gson();
            			  String saidaLogin = saida.toJson(loginSucesso);
            			  out.println(saidaLogin);
            		  }
            		  else {
            			  System.out.println("Credenciais Invalidas");
            			  OpGenerica credencialInvalida = new OpGenerica();
            			  credencialInvalida.setOperation("LOGIN_RECRUITER");
            			  credencialInvalida.setStatus("INVALID_LOGIN");
            			  credencialInvalida.setData();
            			  Gson sai = new Gson();
            			  String saida = sai.toJson(credencialInvalida);
            			  out.println(saida); 
            			       			  
            		  }
            	  }
            	  else {
        			  System.out.println("Credenciais Invalidas");
        			  OpGenerica credencialInvalida = new OpGenerica();
        			  credencialInvalida.setOperation("LOGIN_RECRUITER");
        			  credencialInvalida.setStatus("INVALID_LOGIN");
        			  credencialInvalida.setData();
        			  Gson sai = new Gson();
        			  String saida = sai.toJson(credencialInvalida);
        			  out.println(saida); 
            	  }
              }
              
              
              else if(op.getOperation().equals("SIGNUP_RECRUITER")) {
            	  //É PRECISO COLOCAR VERIFICACAO DE CREDENCIAIS
            	  System.out.println("Operacao: " + op.getOperation());
            	  contasEmpresas.carregarEmpresas();
        		  Empresa empresaEncontrada = contasEmpresas.procurarEmpresa(op.getData().getEmail());
        		  if (empresaEncontrada != null) {
        			  System.out.println("Email já usado: " + empresaEncontrada);
        			  OpGenerica erro = new OpGenerica();
        			  erro.setOperation("SIGNUP_RECRUITER");
        			  erro.setStatus("USER_EXISTS");
        			  erro.setData();
        			  Gson erro1 = new GsonBuilder()/*.setPrettyPrinting()*/.create();; 
        			  String erro2 = erro1.toJson(erro);
        			  out.println(erro2);
        		  }
        		  else {
        			  Empresa empresa = new Empresa(op.getData().getEmail(), op.getData().getName(),
        					  op.getData().getPassword(),op.getData().getIndustry(),op.getData().getDescription());
        			  Empresa empresaId = contasEmpresas.procurarEmpresaPorId(String.valueOf(idRecruiter));
        			  while(empresaId != null) {
        				  idRecruiter = idRecruiter+ 1;
        				  empresaId = contasEmpresas.procurarEmpresaPorId(String.valueOf(idRecruiter));
        			  }
        			  
        			  empresa.setId(String.valueOf(idRecruiter));
        			  contasEmpresas.cadastrarEmpresa(empresa);
        			  OpGenerica saida = new OpGenerica(); 
        			  saida.setOperation("SIGNUP_RECRUITER");
        			  saida.setStatus("SUCCESS");
        			  saida.setData();
        			  Gson sucesso = new Gson();
        			  String saidaSucesso = sucesso.toJson(saida);
        			  out.println(saidaSucesso);
        		  }
            	  
              }
              
              else if(op.getOperation().equals("LOOKUP_ACCOUNT_RECRUITER")) {
            	  System.out.println("Operacao: " + op.getOperation());           	
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica show = new OpGenerica();
            	  Gson gsonShow = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);
                      show.setOperation("LOOKUP_ACCOUNT_RECRUITER");
                      show.setStatus("SUCCESS");
                      show.setData();
                      show.getData().setEmail(empresa.getEmail());
                      show.getData().setName(empresa.getNome());
                      show.getData().setPassword(empresa.getSenha());
                      show.getData().setDescription(empresa.getDescription());
                      show.getData().setIndustry(empresa.getIndustry());
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      show.setOperation("LOOKUP_ACCOUNT_RECRUITER");
                      show.setStatus("INVALID_TOKEN");
                      show.setData();
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                      
                  }
            	  
              }
              
              else if(op.getOperation().equals("UPDATE_ACCOUNT_RECRUITER")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica update = new OpGenerica();
            	  Gson gsonUpdate = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                         String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                   /*   Usuario usuarioEncontrado = contas.procurarUsuario(op.getData().getEmail());
                      if (usuarioEncontrado != null) {
                    	  System.out.println("Email já em uso");
                    	  update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
                    	  update.setStatus("INVALID_EMAIL");
                    	  update.setData();
                    	  String updateRequest = gsonUpdate.toJson(update);
                    	  out.println(updateRequest);
                      }
                      else { */
                    	  Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);
                    	  empresa.setEmail(op.getData().getEmail());
                    	  empresa.setNome(op.getData().getName());
                    	  empresa.setSenha(op.getData().getPassword());
                    	  empresa.setIndustry(op.getData().getIndustry());
                    	  empresa.setDescription(op.getData().getDescription());
                    	  contasEmpresas.atualizarEmpresa(empresa);
                    	  update.setOperation("UPDATE_ACCOUNT_RECRUITER");
                    	  update.setStatus("SUCCESS");
                    	  update.setData();
                    	  String stringGson = gsonUpdate.toJson(update);
                    	  out.println(stringGson);
                      //}
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      update.setOperation("UPDATE_ACCOUNT_RECRUITER");
                      update.setStatus("INVALID_TOKEN");
                      update.setData();
                      String stringGson = gsonUpdate.toJson(update);
                      out.println(stringGson);
                      
                  }
              }
              
              else if(op.getOperation().equals("DELETE_ACCOUNT_RECRUITER")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica delete = new OpGenerica();
            	  Gson gsonDelete = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);
                      String email = empresa.getEmail();
                      contasEmpresas.deletarEmpresa(email);
                      delete.setOperation("DELETE_ACCOUNT_RECRUITER");
                      delete.setStatus("SUCCESS");
                      delete.setData();
                      String stringGson = gsonDelete.toJson(delete);
                      out.println(stringGson);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      delete.setOperation("DELETE_ACCOUNT_RECRUITER");
                      delete.setStatus("INVALID_TOKEN");
                      delete.setData();
                      String stringGson = gsonDelete.toJson(delete);
                      out.println(stringGson);
                      
                  }
              }
              
              
              else {
            	  System.out.println("Operacação não existe");
            	  OpGenerica notFound = new OpGenerica();
            	  notFound.setOperation("NAO_EXISTE");
            	  notFound.setData();
            	  Gson gson1 = new Gson();
            	  String notFoundString = gson1.toJson(notFound);
            	  out.println(notFoundString);
              }
              
              if (inputLine.equals("?")) 
                  inputLine = new String ("\"Bye.\" ends Client, " +
                      "\"End Server.\" ends Server");

             // out.println(inputLine); 

              if (inputLine.equals("Bye.")) 
                  break; 

              if (inputLine.equals("End Server.")) 
                  serverContinue = false; 
              

             } 

         out.close(); 
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e) 
        { 
         System.err.println("Problem with Communication Server");
         System.exit(1); 
        } 
    }
}