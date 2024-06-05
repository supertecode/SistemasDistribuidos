package trocajsons;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Cliente {
    public static void main(String[] args) throws IOException {
    	
    	BufferedReader stdIn = new BufferedReader(
    			new InputStreamReader(System.in));
    	
    	System.out.println("Digite um ip:");
        
		String ip; 
		
		ip = stdIn.readLine();
        //String serverHostname = new String ("10.20.8.170");
		String serverHostname = ip;		
        //String serverHostname = new String ("127.0.0.1");

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

	String userInput;
    String token = "";
    String tipoUsuario = "";
	    System.out.println("Escolha uma opção:");
	    System.out.println("Login: 1.");
	    System.out.println("Cadastro: 2.");
	    System.out.println("Atualização de dados: 3.");
	    System.out.println("Exclusão de conta: 4.");
	    System.out.println("Sair: 5.");
	    System.out.println("Visualizar informações da conta: 6");
	while ((userInput = stdIn.readLine()) != null) 
           {
	
		if(userInput.equals("1")) {
			
			System.out.println("Login de candidato(1) ou empresa(2)?");
			String InputUser;
			InputUser = stdIn.readLine(); //colocar while para nao deixar colocar valor diferente de 1 ou 2
			while(!(InputUser.equals("1") || InputUser.equals("2"))){
				System.out.println("Insira um valor válido, 1 ou 2");
				InputUser = stdIn.readLine();
			}
			String senha; 
			String email;
			System.out.println("Digite seu email:");
			email = stdIn.readLine();
			System.out.println("Digite a sua senha:");
			senha = stdIn.readLine();
			OpGenerica login = new OpGenerica();
			login.setData();
			login.getData().setEmail(email);
			login.getData().setPassword(senha);
			
			if(InputUser.equals("1")) {
				login.setOperation("LOGIN_CANDIDATE");
				Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
				String saida = gson.toJson(login); 
				out.println(saida);
				System.out.println("Mensagem enviada: " + saida);

			}
			else {
				login.setOperation("LOGIN_RECRUITER");
				Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
				String saida = gson.toJson(login); 
				out.println(saida);
				System.out.println("Mensagem enviada: " + saida);
			}
			
			
		}	        
	
		else if(userInput.equals("2")) {
			
			System.out.println("Cadastrar candidato(1) ou empresa(2)?");
			String InputUser;
			InputUser = stdIn.readLine(); 
			while(!(InputUser.equals("1") || InputUser.equals("2"))){
				System.out.println("Insira um valor válido, 1 ou 2");
				InputUser = stdIn.readLine();
			}
			if(InputUser.equals("1")) {
				
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
				System.out.println("Mensagem enviada: " + saida);
			}
			else {
				String senha; 
				String email;
				String nome;
				String industry;
				String description;
				
				System.out.println("Cadastre um email:");
				email = stdIn.readLine();
				System.out.println("Cadastre um nome:");
				nome = stdIn.readLine();
				System.out.println("Cadastre uma senha:");
				senha = stdIn.readLine();
				System.out.println("Cadastre o tipo de indústria");
				industry = stdIn.readLine();
				System.out.println("Cadastre uma descrição");
				description = stdIn.readLine();
				
				OpGenerica signup = new OpGenerica();
				signup.setOperation("SIGNUP_RECRUITER");
				signup.setData();
				signup.getData().setEmail(email);
				signup.getData().setPassword(senha);
				signup.getData().setName(nome);
				signup.getData().setIndustry(industry);
				signup.getData().setDescription(description);
				Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
				String saida = gson.toJson(signup);
				out.println(saida);	
				System.out.println("Mensagem enviada: " + saida);
			}
			
		}
		
		else if(userInput.equals("3")) {
			
			String senha; 
			String email;
			String nome;
			String industry;
			String description;
			
			System.out.println("Atualize seu email:");
			email = stdIn.readLine();
			System.out.println("Atualize seu nome:");
			nome = stdIn.readLine();
			System.out.println("Atualize sua senha:");
			senha = stdIn.readLine();
			OpGenerica update = new OpGenerica();
			
			if(tipoUsuario.equals("CANDIDATE") || tipoUsuario.equals("")) {
				update.setOperation("UPDATE_ACCOUNT_CANDIDATE");
				update.setData();
			}
			else {
				update.setOperation("UPDATE_ACCOUNT_RECRUITER");
				System.out.println("Atualize seu tipo de indústria:");
				industry = stdIn.readLine();
				System.out.println("Atualize a descrição: ");
				description = stdIn.readLine();
				System.out.println("");
				update.setData();
				update.getData().setDescription(description);
				update.getData().setIndustry(industry);
			}
			update.getData().setEmail(email);
			update.getData().setName(nome);
			update.getData().setPassword(senha);
			update.setToken(token);
			Gson gson = new GsonBuilder()/*.setPrettyPrinting()*/.create(); 
			String saida = gson.toJson(update);
			out.println(saida);
			System.out.println("Mensagem enviada: " + saida);
		}
		
		else if(userInput.equals("4")) {
			OpGenerica excluirConta = new OpGenerica(); 
			if(tipoUsuario.equals("CANDIDATE") || tipoUsuario.equals("")) {
				excluirConta.setOperation("DELETE_ACCOUNT_CANDIDATE");
			}
			else {
				excluirConta.setOperation("DELETE_ACCOUNT_RECRUITER");
			}
			excluirConta.setToken(token); 
			excluirConta.setData();
			Gson gson = new Gson(); 
			String saida = gson.toJson(excluirConta);
			out.println(saida);
			System.out.println("Mensagem enviada: " + saida);
		}          
        
		else if (userInput.equals("5")) { 
        		System.out.println("Realizando Logout...");
        		OpGenerica logout = new OpGenerica();
        		if(tipoUsuario.equals("CANDIDATE") || tipoUsuario.equals("")) {
        			logout.setOperation("LOGOUT_CANDIDATE");       		        		
        		}
        		else {
        			logout.setOperation("LOGOUT_RECRUITER"); 
        		}
        		logout.setToken(token);
        		logout.setData();
        		Gson gson = new Gson();
        		String saida = gson.toJson(logout);
        		out.println(saida);
        		System.out.println("Mensagem enviada: " + saida);
        }
        
		else if(userInput.equals("6")) {
        	OpGenerica solicite = new OpGenerica();
        	solicite.setOperation("LOOKUP_ACCOUNT_CANDIDATE");
    		if(tipoUsuario.equals("CANDIDATE") || tipoUsuario.equals("")) {
    			solicite.setOperation("LOOKUP_ACCOUNT_CANDIDATE");      		        		
    		}
    		else {
    			solicite.setOperation("LOOKUP_ACCOUNT_RECRUITER"); 
    		}
        	solicite.setToken(token);
        	solicite.setData();
        	Gson gson = new Gson();
        	String saida = gson.toJson(solicite);
        	out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
        }
        
		else {
			System.out.println("Insira um valor valido");
			continue;
		}
		
            String respostaServidor = in.readLine();
            Gson respostaServer = new Gson(); 
            OpGenerica operacaoRecebida = (OpGenerica) respostaServer.fromJson(respostaServidor, OpGenerica.class);
            
            //TRATAMENTO DOS DADOS RECEBIDOS DO SERVIDOE
            
            //CANDIDATO
            if(operacaoRecebida.getOperation().equals("SIGNUP_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Cadastro realizado com sucesso");
            	}
            	if(operacaoRecebida.getStatus().equals("INVALID_FIELD")) {
            		System.out.println("Valores nulos os vazios passados");
            	}
            	if(operacaoRecebida.getStatus().equals("USER_EXISTS")) {
            		System.out.println("Usuario já cadastrado");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOGIN_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Login realizado com sucesso");
            		System.out.println("Salvando seu token...");
            		token = operacaoRecebida.getData().getToken();
            		System.out.println("Seu token: " + token);
            		tipoUsuario = "CANDIDATE";
            	}
            	else{
            		System.out.println("Credenciais Incorretas");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOGOUT_CANDIDATE") || operacaoRecebida.getOperation().equals("LOGOUT_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		token = "";
            		System.out.println(tipoUsuario + " deslogado.");
            		tipoUsuario = "";
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
            		System.out.println("Email ja em uso ou token inválido");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("DELETE_ACCOUNT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Conta do candidato excluida");
            		System.out.println("Faça login novamente para acessar funcionalidades");
            		token = "";
            		tipoUsuario = "";
            	}
            	else {
            		System.out.println("Erro ao exclir conta");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOOKUP_ACCOUNT_CANDIDATE")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Informações sobre sua conta: ");
            		System.out.println("Email:" + operacaoRecebida.getData().getEmail());
            		System.out.println("Nome: " + operacaoRecebida.getData().getName());
            		System.out.println("Senha: " + operacaoRecebida.getData().getPassword());
            	}
            	else {
            		System.out.println("Erro ao conseguir dados da conta");
            	}
            }
            
            //EMPRESA
            
            if(operacaoRecebida.getOperation().equals("SIGNUP_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Cadastro realizado com sucesso");
            	}
            	if(operacaoRecebida.getStatus().equals("INVALID_FIELD")) {
            		System.out.println("Valores nulos os vazios passados");
            	}
            	if(operacaoRecebida.getStatus().equals("USER_EXISTS")) {
            		System.out.println("Usuario já cadastrado");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOGIN_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Login realizado com sucesso");
            		System.out.println("Salvando seu token...");
            		token = operacaoRecebida.getData().getToken();
            		tipoUsuario = "RECRUITER";
            		System.out.println("Seu token: " + token);
            	}
            	else{
            		System.out.println("Credenciais Incorretas");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("UPDATE_ACCOUNT_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Dados atualizados com sucesso");
            	}
            	else {
            		System.out.println("Email ja em uso ou token inválido");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("DELETE_ACCOUNT_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Conta da empresa excluida");
            		System.out.println("Faça login novamente para acessar funcionalidades");
            		token = "";
            		tipoUsuario = "";
            	}
            	else {
            		System.out.println("Erro ao exclir conta");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("LOOKUP_ACCOUNT_RECRUITER")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Informações sobre sua conta: ");
            		System.out.println("Email:" + operacaoRecebida.getData().getEmail());
            		System.out.println("Nome: " + operacaoRecebida.getData().getName());
            		System.out.println("Senha: " + operacaoRecebida.getData().getPassword());
            		System.out.println("Indústria: " + operacaoRecebida.getData().getIndustry());
            		System.out.println("Descrição: " + operacaoRecebida.getData().getDescription());
            	}
            	else {
            		System.out.println("Erro ao conseguir dados da conta");
            	}
            }
            
            if(operacaoRecebida.getOperation().equals("NAO_EXISTE")) {
                    System.out.println("Operacao Invalida recebida do servidor: " + operacaoRecebida.getOperation());
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
