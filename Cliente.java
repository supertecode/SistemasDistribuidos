package trocajsons;
import trocajsons.Operations.*;
//import trocajsons.OperationsServer.*;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Cliente {
    public static void main(String[] args) throws IOException {

        String serverHostname = new String ("10.20.8.170");
       // String serverHostname = new String ("25.4.89.234");

        if (args.length > 0)
           serverHostname = args[0];
        System.out.println ("Attemping to connect to host " +
                serverHostname + " on port 10008.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 21234);
            //echoSocket = new Socket(serverHostname, 10008);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));

	String userInput;
    String token = "";
	    System.out.println("Escolha uma opção:");
	    System.out.println("Login: 1.");
	    System.out.println("Cadastro: 2.");
	    System.out.println("Atualização de dados: 3.");
	    System.out.println("Exclusão de conta: 4.");
	    System.out.println("Sair: 5.");
	    System.out.println("Visualizar informações da conta: 6");
       // System.out.println ("Type Message (\"Bye.\" to quit)");
	while ((userInput = stdIn.readLine()) != null) 
           {
	
		if(userInput.equals("1")) {
			
			String senha; 
			String email;
			System.out.println("Digite seu email:");
			email = stdIn.readLine();
			System.out.println("Digite a sua senha:");
			senha = stdIn.readLine();
			OpGenerica login = new OpGenerica();
			login.setOperation("LOGIN_CANDIDATE");
			login.setData();
			login.getData().setEmail(email);
			login.getData().setPassword(senha);
			Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
			String saida = gson.toJson(login); 
			out.println(saida);
			
		}	        
	
		else if(userInput.equals("2")) {
			
			String senha; 
			String email;
			String nome;
			
			System.out.println("Cadastre um email:");
			email = stdIn.readLine();
			System.out.println("Cadastre um nome:");
			nome = stdIn.readLine();
			System.out.println("Cadastre uma senha:");
			senha = stdIn.readLine();
			OpGenerica signup = new OpGenerica();
			signup.setOperation("SIGNUP_CANDIDATE");
			signup.setData();
			signup.getData().setEmail(email);
			signup.getData().setName(nome);
			signup.getData().setPassword(senha);
			Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
			String saida = gson.toJson(signup);
			out.println(saida);		
		}
		
		else if(userInput.equals("3")) {
			
			String senha; 
			String email;
			String nome;
			
			System.out.println("Atualize seu email:");
			email = stdIn.readLine();
			System.out.println("Atualize seu nome:");
			nome = stdIn.readLine();
			System.out.println("Atualize sua senha:");
			senha = stdIn.readLine();
			OpGenerica update = new OpGenerica();
			update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
			update.setToken(token);
			update.setData();
			update.getData().setEmail(email);
			update.getData().setName(nome);
			update.getData().setPassword(senha);
			Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
			String saida = gson.toJson(update);
			out.println(saida);
		}
		
		else if(userInput.equals("4")) {
			OpGenerica excluirConta = new OpGenerica(); 
			excluirConta.setOperation("DELETE_ACCOUNT_CANDIDATE");
			excluirConta.setToken(token); 
			excluirConta.setData();
			Gson gson = new Gson(); 
			String saida = gson.toJson(excluirConta);
			out.println(saida);
		}          
        
		else if (userInput.equals("5")) { 
        		System.out.println("Realizando Logout...");
        		OpGenerica logout = new OpGenerica();
        		logout.setOperation("LOGOUT_CANDIDATE");
        		logout.setToken(token);
        		logout.setData();
        		Gson gson = new Gson();
        		String saida = gson.toJson(logout);
        		out.println(saida);
        }
        
		else if(userInput.equals("6")) {
        	OpGenerica solicite = new OpGenerica();
        	solicite.setOperation("LOOKUP_ACCOUNT_CANDIDATE");
        	solicite.setToken(token);
        	solicite.setData();
        	Gson gson = new Gson();
        	String saida = gson.toJson(solicite);
        	out.println(saida);
        }
        
		else {
			System.out.println("Insira um valor valido");
			continue;
		}
            String respostaServidor = in.readLine();
            Gson respostaServer = new Gson(); 
            OpGenerica operacaoRecebida = (OpGenerica) respostaServer.fromJson(respostaServidor, OpGenerica.class);
            
            //TRATAMENTO DOS DADOS RECEBIDOS DO SERVIDOE
            
            if(operacaoRecebida.getOperation().equals("SIGNUP_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Cadastro realizado com sucesso");
            	}
            	if(operacaoRecebida.getStatus().equals("INVALID_FIELD")) {
            		System.out.println("Valores nulos os vazios passados");
            	}
            	if(operacaoRecebida.getStatus().equals("USER_EXISTS")) {
            		System.out.println("Valores nulos os vazios passados");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOGIN_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Login realizado com sucesso");
            		System.out.println("Salvando seu token...");
            		token = operacaoRecebida.getData().getToken();
            		System.out.println("Seu token: " + token);
            	}
            	else{
            		System.out.println("Credenciais Incorretas");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOGOUT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		token = "";
            	}
            	else {
            		System.out.println("Você não está logado");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("UPDATE_ACCOUNT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Dados atualizados com sucesso");
            	}
            	else {
            		System.out.println("Email ja em uso");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("DELETE_ACCOUNT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Conta excluida");
            		System.out.println("Faça login novamente para acessar funcionalidades");
            		token = "";
            	}
            	else {
            		System.out.println("Erro ao exclir conta");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOOKUP_ACCOUNT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Informações sobre usa conta: ");
            		System.out.println("Email:" + operacaoRecebida.getData().getEmail());
            		System.out.println("Nome: " + operacaoRecebida.getData().getName());
            		System.out.println("Senha: " + operacaoRecebida.getData().getPassword());
            	}
            	else {
            		System.out.println("Erro ao conseguir dados da conta");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("NAO_EXISTE")) {
                    System.out.println("Operacao Invalida recebida do servidor");
            }
            
	        System.out.println("Resposta do servidor: " + respostaServidor);
	        System.out.println("");
	        System.out.println("O que deseja fazer agora?");
		    System.out.println("Escolha uma opção:");
		    System.out.println("Login: 1.");
		    System.out.println("Cadastro: 2.");
		    System.out.println("Atualização de dados: 3.");
		    System.out.println("Exclusão de conta: 4.");
		    System.out.println("Sair: 5.");
		    System.out.println("Visualizar informações da conta: 6");
	        
	   }

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}
