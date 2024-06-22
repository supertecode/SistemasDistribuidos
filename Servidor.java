package trocajsons;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

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
         ArrayList<String> listaHabilidades = new ArrayList<>(); 
         listaHabilidades.add("NodeJs");listaHabilidades.add("JavaScript");
         listaHabilidades.add("Java");listaHabilidades.add("C");
         listaHabilidades.add("HTML");listaHabilidades.add("CSS");
         listaHabilidades.add("React");listaHabilidades.add("ReactNative");
         listaHabilidades.add("TypeScript");listaHabilidades.add("Ruby");
         while ((inputLine = in.readLine()) != null) 
             { 
              System.out.println ("Server: " + inputLine); 
              Accounts contas = new Accounts();
              RecruiterAccounts contasEmpresas = new RecruiterAccounts();
              Gson gson = new Gson();
              OpGenerica op = new OpGenerica();
              OpGenericaAlt opAlt = new OpGenericaAlt();
              opAlt.setOperation("NADA");
              try {
            	  op = (OpGenerica) gson.fromJson(inputLine, OpGenerica.class);
                  
              } catch (JsonSyntaxException e) {
                  System.out.println("Erro de sintaxe ao desserializar o JSON: " + e.getMessage());
                  e.printStackTrace();
                  // Tratar a exceção adequadamente
                  op.setOperation("QUALQUER COISA");
                  opAlt = gson.fromJson(inputLine, OpGenericaAlt.class);
                 
              }
           
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
              //INCLUSAO DE SKILL E EXPERIENCE POR PARTE DO USUARIO
              else if(op.getOperation().equals("INCLUDE_SKILL")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica skill = new OpGenerica();
            	  Gson gsonSkill = new Gson();
            	  contas.carregarUsuarios();
            	  try {
            		  String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
            		  Usuario usuario = contas.procurarUsuarioPorId(id);
            		  boolean temNaLista = false;
            		  for(String habilidade : listaHabilidades) {
            			  if(op.getData().getSkill().equals(habilidade)) {
            				  temNaLista = true;
            			  }
            		  }
            		  if(temNaLista) {
            			  boolean jaRegistrado = usuario.possuiExperiencia(op.getData().getSkill());
            			  if(jaRegistrado) {
            				  skill.setOperation("INCLUDE_SKILL");
                			  skill.setStatus("SKILL_EXISTS");
                			  skill.setData();
                			  String saida = gsonSkill.toJson(skill);
                    		  out.println(saida);
            			  }
            			  else {
            				  usuario.adicionarExperiencia(op.getData().getSkill(), op.getData().getExperience());
                    		  contas.atualizarUsuario(usuario);
                    		  skill.setOperation("INCLUDE_SKILL");
                    		  skill.setStatus("SUCCESS");
                    		  skill.setData();
                    		  String saida = gsonSkill.toJson(skill);
                    		  out.println(saida);
            			  }
            		  }
            		  else {
            			  skill.setOperation("INCLUDE_SKILL");
            			  skill.setStatus("SKILL_NOT_EXIST");
            			  skill.setData();
            			  String saida = gsonSkill.toJson(skill);
                		  out.println(saida);
            			  
            		  }
            		  
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha ao verificar token");
                      skill.setOperation("INCLUDE_SKILL");
                      skill.setStatus("INVALID_TOKEN");
                      skill.setData();
                      String saida = gsonSkill.toJson(skill);
                      out.println(saida);                                 
              }
              
             }
              else if(op.getOperation().equals("LOOKUP_SKILLSET")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica skill = new OpGenerica();
            	  Gson gsonSkill = new Gson();
            	  contas.carregarUsuarios();
            	  try {
            		  String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
            		  Usuario usuario = contas.procurarUsuarioPorId(id);
            		  skill.setOperation("LOOKUP_SKILLSET");
            		  ArrayList<Experiencia> lista = usuario.getExperiencias();
            		  skill.setStatus("SUCCESS");
            		  skill.setData();
            		  skill.getData().setSkillset_size(String.valueOf(lista.size()));
            		  skill.getData().setSkillset(lista);
            		  String saida = gsonSkill.toJson(skill);
            		  out.println(saida);
            		  
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha ao verificar token");
                      skill.setOperation("LOOKUP_SKILLSET");
                      skill.setStatus("INVALID_TOKEN");
                      skill.setData();
                      String saida = gsonSkill.toJson(skill);
                      out.println(saida);                                 
                  } 
              }
              
              else if(op.getOperation().equals("LOOKUP_SKILL")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica skill = new OpGenerica();
            	  Gson gsonSkill = new Gson();
            	  contas.carregarUsuarios();
            	  boolean achou = false;
            	  try {
            		  String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
            		  Usuario usuario = contas.procurarUsuarioPorId(id);
            		  skill.setOperation("LOOKUP_SKILL");
            		  ArrayList<Experiencia> lista = usuario.getExperiencias();
            		  for(Experiencia skillRequerida : lista) {
            			  if(skillRequerida.getExperiencia().equals(op.getData().getSkill())) {
            				  skill.setData();
            				  skill.getData().setSkill(skillRequerida.getExperiencia());
            				  skill.getData().setExperience(skillRequerida.getTempo());            				  
            				  skill.setStatus("SUCCESS");
            				  achou = true;
            			  }
            		  }
            		  if(achou) {
            			  String saida = gsonSkill.toJson(skill);
            			  out.println(saida);
            			  
            		  }
            		  else {
            			  skill.setStatus("SKILL_NOT_FOUND");
            			  skill.setData();
            			  String saida = gsonSkill.toJson(skill);
            			  out.println(saida);
            		  }
            		  
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha ao verificar token");
                      skill.setOperation("LOOKUP_SKILL");
                      skill.setStatus("INVALID_TOKEN");
                      skill.setData();
                      String saida = gsonSkill.toJson(skill);
                      out.println(saida);                                 
                  } 
              }
              
              else if(op.getOperation().equals("DELETE_SKILL")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica skill = new OpGenerica();
            	  Gson gsonSkill = new Gson();
            	  contas.carregarUsuarios();
              	  try {
            		  String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
            		  Usuario usuario = contas.procurarUsuarioPorId(id);
            		  boolean achou = false;
            		  for(Experiencia experiencia : usuario.getExperiencias()) {
            			  if(experiencia.getExperiencia().equals(op.getData().getSkill())) {
            				  achou = true;
            			  }
            		  }
            		  skill.setOperation("DELETE_SKILL");
            		  skill.setData();                			  
            		  if(achou) {
            			  usuario.removerExperiencia(op.getData().getSkill());
            			  contas.atualizarUsuario(usuario);     
            			  skill.setStatus("SUCCESS");
            		  }
            		  else {
            			  skill.setStatus("SKILL_NOT_FOUND");
            		  }
            		  String saida = gsonSkill.toJson(skill);
            		  out.println(saida);
            		  
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha ao verificar token");
                      skill.setOperation("DELETE_SKILL");
                      skill.setStatus("INVALID_TOKEN");
                      skill.setData();
                      String saida = gsonSkill.toJson(skill);
                      out.println(saida);                                 
                  } 
              }
              
              else if(op.getOperation().equals("UPDATE_SKILL")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica skill = new OpGenerica();
            	  Gson gsonSkill = new Gson();
            	  contas.carregarUsuarios();
            	  try {
            		  String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
            		  Usuario usuario = contas.procurarUsuarioPorId(id);
            		  boolean temNaLista = false;
            		  boolean achou = false;
            		  for(Experiencia skillCandidato : usuario.getExperiencias()) {
            			  if(skillCandidato.getExperiencia().equals(op.getData().getSkill())) {
            				achou = true;  
            			  }
            		  }
            		  for(String habilidade : listaHabilidades) {
            			  if(op.getData().getNewSkill().equals(habilidade)) {
            				  temNaLista = true;
            			  }
            		  }
            		  if(achou) {
            			  if(temNaLista) {
            				  usuario.atualizarExperiencia(op.getData().getSkill(),op.getData().getNewSkill(),op.getData().getExperience());
            				  contas.atualizarUsuario(usuario);
            				  skill.setOperation("UPDATE_SKILL");
            				  skill.setStatus("SUCCESS");
            				  skill.setData();      
            			  }
            			  else {
            				  skill.setOperation("UPDATE_SKILL");
            				  skill.setStatus("SKILL_NOT_EXIST");
            				  skill.setData();
            			  }           			  
            			  String saida = gsonSkill.toJson(skill);
            			  out.println(saida);
            		  }
            		  else {
            			  skill.setOperation("UPDATE_SKILL");
            			  skill.setStatus("SKILL_NOT_FOUND");
            			  skill.setData();
            			  String saida = gsonSkill.toJson(skill);
            			  out.println(saida);
            		  }
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha ao verificar token");
                      skill.setOperation("UPDATE_SKILL");
                      skill.setStatus("INVALID_TOKEN");
                      skill.setData();
                      String saida = gsonSkill.toJson(skill);
                      out.println(saida);                                 
                  } 
              }
              
              //INCLUSAO DE VAGAS PELA EMPRESA
              
              else if(op.getOperation().equals("INCLUDE_JOB")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica job = new OpGenerica();
            	  Gson jobs = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);
                      boolean temNaLista = false;
            		  for(String habilidade : listaHabilidades) {
            			  if(op.getData().getSkill().equals(habilidade)) {
            				  temNaLista = true;
            			  }
            		  }
            		  if(temNaLista) {
            			  empresa.adicionarVaga(op.getData().getSkill(), op.getData().getExperience());
            			  contasEmpresas.atualizarEmpresa(empresa);
            			  job.setOperation("INCLUDE_JOB");
            			  job.setStatus("SUCCESS");
            			  job.setData();
            			  String saida = jobs.toJson(job);
            			  out.println(saida);           			  
            		  }
            		  else {
            			  job.setOperation("INCLUDE_JOB");
                          job.setStatus("SKILL_NOT_EXIST");
                          job.setData();
                          String stringGson = jobs.toJson(job);
                          out.println(stringGson);
            		  }
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      job.setOperation("INCLUDE_JOB");
                      job.setStatus("INVALID_TOKEN");
                      job.setData();
                      String stringGson = jobs.toJson(job);
                      out.println(stringGson);                     
                  }
            	  
            	  
              }
              
              else if(op.getOperation().equals("LOOKUP_JOB")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica job = new OpGenerica();
            	  Gson jobs = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);  
                      ArrayList<Vagas> vagas = empresa.getJobset();
                      boolean achou = false;
                      job.setOperation("LOOKUP_JOB");
                      for(Vagas vaga: vagas) {
                    	  if(vaga.getId().equals(op.getData().getId())) {
                    		  job.setData();
                    		  job.getData().setSkill(vaga.getSkill());
                    		  job.getData().setExperience(vaga.getExperience());
                    		  job.getData().setId(vaga.getId());
                    		  achou = true;
                    	  }
                      }
                      if(achou) {
                    	  job.setStatus("SUCCESS");
                      }
                      else {
                    	  job.setStatus("JOB_NOT_FOUND");
                    	  job.setData();
                      }
                 
                      String saida = jobs.toJson(job);
                      out.println(saida);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      job.setOperation("LOOKUP_JOB");
                      job.setStatus("INVALID_TOKEN");
                      job.setData();
                      String stringGson = jobs.toJson(job);
                      out.println(stringGson);                     
                  }
              }
              
              else if(op.getOperation().equals("LOOKUP_JOBSET")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica job = new OpGenerica();
            	  Gson jobs = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);  
                      ArrayList<Vagas> vagas = empresa.getJobset();
                      job.setOperation("LOOKUP_JOBSET");
                      job.setStatus("SUCCESS");
                      job.setData();
                      job.getData().setJobset_size(String.valueOf(vagas.size()));
                      job.getData().setJobset(vagas);
                      String saida = jobs.toJson(job);
                      out.println(saida);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      job.setOperation("LOOKUP_JOBSET");
                      job.setStatus("INVALID_TOKEN");
                      job.setData();
                      String stringGson = jobs.toJson(job);
                      out.println(stringGson);                     
                  }
            	  
              }
              
              else if(op.getOperation().equals("DELETE_JOB")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica job = new OpGenerica();
            	  Gson jobs = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);  
                      boolean achou = false;
                      for(Vagas vaga : empresa.getJobset()) {
                    	  if(vaga.getId().equals(op.getData().getId())) {
                    		  achou = true;
                    	  }
                      }
                      if(achou) {
                    	  empresa.removerVaga(op.getData().getId());
                    	  contasEmpresas.atualizarEmpresa(empresa);
                    	  job.setOperation("DELETE_JOB");
                    	  job.setStatus("SUCCESS");
                    	  job.setData();
                    	  
                      }
                      else {
                    	  job.setOperation("DELETE_JOB");
                    	  job.setStatus("JOB_NOT_FOUND");
                    	  job.setData();
                      }
                      String saida = jobs.toJson(job);
                      out.println(saida);
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      job.setOperation("DELETE_JOB");
                      job.setStatus("INVALID_TOKEN");
                      job.setData();
                      String stringGson = jobs.toJson(job);
                      out.println(stringGson);                     
                  }
              }
              
              else if(op.getOperation().equals("UPDATE_JOB")) {
            	  System.out.println("Operacao: " + op.getOperation());
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica job = new OpGenerica();
            	  Gson jobs = new Gson();
            	  contasEmpresas.carregarEmpresas();
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");
                      Empresa empresa = contasEmpresas.procurarEmpresaPorId(id);  
                      boolean temNaLista = false;
                      boolean achou = false;
                      for(Vagas vaga : empresa.getJobset()) {
                    	  if(vaga.getId().equals(op.getData().getId())) {
                    		  achou = true;
                    	  }
                      }
             		  for(String habilidade : listaHabilidades) {
             			  if(op.getData().getSkill().equals(habilidade)) {
             				  temNaLista = true;
             			  }
             		  }
             		  if(achou) {
             			  if(temNaLista) {
             				  empresa.atualizarVaga(op.getData().getId(), op.getData().getSkill(), op.getData().getExperience());;
             				  contasEmpresas.atualizarEmpresa(empresa);
             				  job.setOperation("UPDATE_JOB");
             				  job.setStatus("SUCCESS");
             				  job.setData();             			  
             			  }
             			  else {
             				  job.setOperation("UPDATE_JOB");
             				  job.setStatus("SKILL_NOT_EXIST");
             				  job.setData();
             			  }          			  
             			  String stringGson = jobs.toJson(job);
             			  out.println(stringGson);
             		  }
             		  else {
             			 job.setOperation("UPDATE_JOB");
             			 job.setOperation("JOB_NOT_FOUND");
             			 job.setData();
             			String stringGson = jobs.toJson(job);
           			    out.println(stringGson);
             			 
             		  }
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      job.setOperation("UPDATE_JOB");
                      job.setStatus("INVALID_TOKEN");
                      job.setData();
                      String stringGson = jobs.toJson(job);
                      out.println(stringGson);                     
                  }
              }
              
              else if(opAlt.getOperation().equals("SEARCH_JOB")) {
            	  System.out.println("Operacao: " + opAlt.getOperation());           	
            	  Token tokenCliente = new Token(); 
            	  String token = opAlt.getToken();
            	  OpGenerica show = new OpGenerica();
            	  Gson gsonShow = new Gson();
         		  List<Empresa> empresas = contasEmpresas.retornarEmpresas();
     	          if (empresas != null) {
     	            empresas.forEach(System.out::println); // Para testar se as empresas foram carregadas
     	           } 
     	          else {
     	            System.out.println("Nenhuma empresa foi carregada.");
     	          }
     		      List<List<Vagas>> todasVagas = new ArrayList<>();
     		      int n = empresas.size();
     		      for(int i = 0; i<n ; i++) {
     			    todasVagas.add(empresas.get(i).getJobset());
     		      }
     		      List<Vagas> todasVagasFinal = new ArrayList<>();
     		
     		      for (List<Vagas> listaum : todasVagas) {
                    todasVagasFinal.addAll(listaum);
                  }
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");                     
                      show.setOperation("SEARCH_JOB");
                      show.setStatus("SUCCESS");
                      show.setData();    
                      List<Vagas> vagasFiltradas = new ArrayList<>();
                      List<String> campos = opAlt.getNonNullFields(opAlt.getData());
                      int size = campos.size();
                      if(size == 1) {
                    	  String campo = campos.get(0);
                    	  if(campo.equals("skill")) {
                    		  List<String> skills = opAlt.getData().getSkill();
                    		  for(Vagas vaga: todasVagasFinal) {
                    			  for(String skill: skills) {
                    				  if(vaga.getSkill().equals(skill)) {
                    					  vagasFiltradas.add(vaga);
                    				  }
                    			  }
                    		  }
                    	  }
                    	  else {
                    		  String experienceString = opAlt.getData().getExperience();
                    		  int experince = Integer.parseInt(experienceString);
                    		  for(Vagas vaga: todasVagasFinal) {
                    			  if(Integer.parseInt(vaga.getExperience()) <= experince){
                    				  vagasFiltradas.add(vaga);
                    			  }
                    		  }
                    	  }
                      }
                      else { //temos lista de skill, experience e filtro
                    	  List<String> skills = opAlt.getData().getSkill();
                    	  String experienceString = opAlt.getData().getExperience();
                		  int experince = Integer.parseInt(experienceString);
                		  String filter = opAlt.getData().getFilter();
                		  if(filter.equals("AND")) {
                			  for(Vagas vaga: todasVagasFinal) {
                    			  for(String skill: skills) {
                    				  if(vaga.getSkill().equals(skill) && (Integer.parseInt(vaga.getExperience()) <= experince)) {
                    					  vagasFiltradas.add(vaga);
                    				  }
                    			  }
                    		  }
                		  }
                		  else {
                			  for(Vagas vaga: todasVagasFinal) {
                    			  for(String skill: skills) {
                    				  if(vaga.getSkill().equals(skill) || (Integer.parseInt(vaga.getExperience()) <= experince)) {
                    					  vagasFiltradas.add(vaga);
                    				  }
                    			  }
                    		  }
                		  }
                      }
                      show.setData();
                      show.getData().setJobset_size(String.valueOf(vagasFiltradas.size()));
                      show.getData().setJobset((ArrayList<Vagas>) vagasFiltradas);
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                      
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      show.setOperation("SEARCH_JOB");
                      show.setStatus("INVALID_TOKEN");
                      show.setData();
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                      
                  }
              }
              
              else if(op.getOperation().equals("SEARCH_JOB")) {
            	  System.out.println("Operacao: " + op.getOperation());           	
            	  Token tokenCliente = new Token(); 
            	  String token = op.getToken();
            	  OpGenerica show = new OpGenerica();
            	  Gson gsonShow = new Gson();
         		  List<Empresa> empresas = contasEmpresas.retornarEmpresas();
     	          if (empresas != null) {
     	            empresas.forEach(System.out::println); // Para testar se as empresas foram carregadas
     	           } 
     	          else {
     	            System.out.println("Nenhuma empresa foi carregada.");
     	          }
     		      List<List<Vagas>> todasVagas = new ArrayList<>();
     		      int n = empresas.size();
     		      for(int i = 0; i<n ; i++) {
     			    todasVagas.add(empresas.get(i).getJobset());
     		      }
     		      List<Vagas> todasVagasFinal = new ArrayList<>();
     		
     		      for (List<Vagas> listaum : todasVagas) {
                    todasVagasFinal.addAll(listaum);
                  }
            	  try {
                      String id = tokenCliente.verificarToken(token , "DISTRIBUIDOS");                     
                      show.setOperation("SEARCH_JOB");
                      show.setStatus("SUCCESS");
                      show.setData();    
                      List<Vagas> vagasFiltradas = new ArrayList<>();
                      List<String> campos = op.getNonNullFields(op.getData());
                      int size = campos.size();
                      if(size == 1) {
                    	  String experienceString = op.getData().getExperience();
                		  int experince = Integer.parseInt(experienceString);
                		  for(Vagas vaga: todasVagasFinal) {
                			  if(Integer.parseInt(vaga.getExperience()) <= experince){
                				  vagasFiltradas.add(vaga);
                			  }
                		  }
                      }
                      else { //temos lista de skill, experience e filtro
                    	  
                      }
                      show.setData();
                      show.getData().setJobset_size(String.valueOf(vagasFiltradas.size()));
                      show.getData().setJobset((ArrayList<Vagas>) vagasFiltradas);
                      String stringGson = gsonShow.toJson(show);
                      out.println(stringGson);
                      
                  } catch (JWTVerificationException exception) {
                      System.out.println("Falha na verificação do token: ");
                      show.setOperation("SEARCH_JOB");
                      show.setStatus("INVALID_TOKEN");
                      show.setData();
                      String stringGson = gsonShow.toJson(show);
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
              
              String bufferSaida = out.toString();
              System.out.println("ENVIADO: " + bufferSaida);

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