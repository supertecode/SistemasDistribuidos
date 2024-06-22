package trocajsons;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;


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
		
		else if(userInput.equals("7")) { //cadastro de competencia
			OpGenerica skill = new OpGenerica();
			System.out.println("Skill:");
			String skillUser = stdIn.readLine(); 
			System.out.println("Experience: ");
			String experienceUser = stdIn.readLine(); 
			skill.setOperation("INCLUDE_SKILL");
			skill.setToken(token);
			skill.setData();
			skill.getData().setSkill(skillUser);
			skill.getData().setExperience(experienceUser);
			Gson gson = new Gson();
			String saida = gson.toJson(skill);
			out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
		}
		
        else if(userInput.equals("8")) { // leitura de competencias
        	System.out.println("Skillset(1) ou Skill(2)");
        	String input = stdIn.readLine();
        	OpGenerica skillset = new OpGenerica();
        	skillset.setToken(token);
        	skillset.setData();
        	Gson gson = new Gson();
        	while(!(input.equals("1") || input.equals("2"))) {
        		System.out.println("Informe valor válido");
        		input = stdIn.readLine();
        	}
        	if(input.equals("1")) {
        		skillset.setOperation("LOOKUP_SKILLSET");
        		String saida = gson.toJson(skillset);
        		out.println(saida);
        		System.out.println("Mensagem enviada: " + saida);    		
        	}
        	else {
        		skillset.setOperation("LOOKUP_SKILL");
        		System.out.println("Escreva a skill a consultar: ");
        		String skill = stdIn.readLine();
        		skillset.getData().setSkill(skill);
        		String saida = gson.toJson(skillset);
        		out.println(saida);
        		System.out.println("Mensagem enviada: " + saida);
        	}
			
		}
		
        else if(userInput.equals("9")) { // atualização de competencias
        	System.out.println("Skill antiga: ");
        	String oldSkill = stdIn.readLine(); 
        	System.out.println("Skill nova: ");
        	String newSkill = stdIn.readLine(); 
        	System.out.println("Tempo de experiencia: ");
        	String experience = stdIn.readLine(); 
        	OpGenerica skillupdate = new OpGenerica();
        	skillupdate.setOperation("UPDATE_SKILL");
        	skillupdate.setToken(token);
        	skillupdate.setData();
        	skillupdate.getData().setSkill(oldSkill);
        	skillupdate.getData().setNewSkill(newSkill);
        	skillupdate.getData().setExperience(experience);
        	Gson skill = new Gson();
        	String saida = skill.toJson(skillupdate);
        	out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
        	  	
		}
		
        else if(userInput.equals("10")) { //excluir competencia
			OpGenerica skillset = new OpGenerica();
			System.out.println("Digite qual competência você quer excluir: ");
			String skill = stdIn.readLine();
			skillset.setOperation("DELETE_SKILL");
			skillset.setToken(token);
			skillset.setData();
			skillset.getData().setSkill(skill);
			Gson gson = new Gson();
			String saida = gson.toJson(skillset);
			out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
		}
		
        else if(userInput.equals("11")) { //cadastrar vagas no servidor
        	System.out.println("Skill: ");
        	String skill = stdIn.readLine();
        	System.out.println("Experience: ");
        	String experience = stdIn.readLine();
			OpGenerica vaga = new OpGenerica();
			vaga.setOperation("INCLUDE_JOB");
			vaga.setToken(token);
			vaga.setData();
			vaga.getData().setSkill(skill);
			vaga.getData().setExperience(experience);
			Gson gson = new Gson();
			String saida = gson.toJson(vaga);
			out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
		}
		
        else if(userInput.equals("12")) { //atualizar dados de vaga
        	System.out.println("ID da vaga a ser alterada: ");
        	String id = stdIn.readLine();
        	System.out.println("Skill: ");
        	String skill = stdIn.readLine();
        	System.out.println("Experience: ");
        	String experience = stdIn.readLine();
			OpGenerica vaga = new OpGenerica();
			vaga.setOperation("UPDATE_JOB");
			vaga.setToken(token);
			vaga.setData();
			vaga.getData().setId(id);
			vaga.getData().setSkill(skill);
			vaga.getData().setExperience(experience);
			Gson gson = new Gson();
			String saida = gson.toJson(vaga);
			out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
		}
        
        else if(userInput.equals("13")) { //lê dados das próprias vagas
        	System.out.println("Todas as vagas(1) ou uma vaga(2)?");
        	String input = stdIn.readLine();
        	OpGenerica jobset = new OpGenerica();
        	jobset.setToken(token);
        	Gson gson = new Gson();
        	while(!(input.equals("1") || input.equals("2"))) {
        		System.out.println("Informe valor válido");
        		input = stdIn.readLine();
        	}
        	if(input.equals("1")){
        		jobset.setOperation("LOOKUP_JOBSET");
        		jobset.setData();
        	}
        	else {
        		System.out.println("Informe o id da vaga que deseja consultar: ");
        		String idVaga = stdIn.readLine();
        		jobset.setOperation("LOOKUP_JOB");
        		jobset.setData();
        		jobset.getData().setId(idVaga);
        	}
        	String saida = gson.toJson(jobset);
        	out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);      		
		}
		
        else if(userInput.equals("14")) { //Envia pedido de exclusão de vagas
    		System.out.println("Digite o ID da vaga que será excluída: ");
			String id = stdIn.readLine();
			OpGenerica vaga = new OpGenerica();
			vaga.setOperation("DELETE_JOB");
			vaga.setToken(token);
			vaga.setData();
			vaga.getData().setId(id);
			Gson gson = new Gson();
			String saida = gson.toJson(vaga);
			out.println(saida);
        	System.out.println("Mensagem enviada: " + saida);
		}
		
        else if(userInput.equals("15")) {
        	System.out.println("Como pretende realizar a pesquisa de vagas?");
        	System.out.println("Somente por skill(1) ; somente por Experience(2) ; por skill e experience(3)");
        	String inputUser = stdIn.readLine();
        	while(!(inputUser.equals("1")||inputUser.equals("2")||inputUser.equals("3"))){
        		System.out.println("Digite um valor válido: ");
        	    inputUser = stdIn.readLine();
        	}
        	OpGenericaAlt search = new OpGenericaAlt();
        	search.setOperation("SEARCH_JOB");
        	ArrayList<String> skills = new ArrayList<>();
        	if(inputUser.equals("1")) {
        		boolean adicionar = true;
        		while(adicionar) {
        			System.out.println("Adicione uma skill:");
        			String skill = stdIn.readLine();
        			skills.add(skill);
        			System.out.println("deseja adicionar mais alguma skill? sim(1) nao(2)");
        			String maisSkill = stdIn.readLine();
        			if(maisSkill.equals("1")) {
        				adicionar = true;
        			}
        			else  {
        				adicionar = false;
        			}		
        		}        		
        		search.setToken(token);
        		search.setData();
        		search.getData().setSkill(skills);        		
        	}
        	if(inputUser.equals("2")) {
        		System.out.println("Digite seu Experience: ");
        		String experience = stdIn.readLine();
        		search.setToken(token);
        		search.setData();
        		search.getData().setExperience(experience);
        	}
        	if(inputUser.equals("3")) {
        		boolean adicionar = true;
        		while(adicionar) {
        			System.out.println("Adicione uma skill:");
        			String skill = stdIn.readLine();
        			skills.add(skill);
        			System.out.println("deseja adicionar mais alguma skill? sim(1) nao(2)");
        			String maisSkill = stdIn.readLine();
        			if(maisSkill.equals("1")) {
        				adicionar = true;
        			}
        			else  {
        				adicionar = false;
        			}		
        		}
        		System.out.println("Filtrar por E(1) ou OU(2)?");
        		String opLogica = stdIn.readLine();
        		if(opLogica.equals("1")) {
        			opLogica = "AND";
        		}
        		else {
        			opLogica = "OR";
        		}
        		System.out.println("Digite seu Experience: ");
        		String experience = stdIn.readLine();
        		search.setToken(token);
        		search.setData();
        		search.getData().setSkill(skills);
        		search.getData().setExperience(experience);
        		search.getData().setFilter(opLogica);
        	}
        	Gson gson = new Gson();
        	String saida = gson.toJson(search);
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
            
            if(operacaoRecebida.getOperation().equals("INCLUDE_SKILL")) {
            	if(operacaoRecebida.getStatus().equals("SUCCESS")) {
            		System.out.println("Skill cadastrada com sucesso");
            	}
            	else {
            		System.out.println("token invalido ou skill ja cadastrado");
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
		    if(tipoUsuario.equals("CANDIDATE")){
		    	System.out.println("Cadastrar habilidade: 7");
		    	System.out.println("Leitura de competencias e habilidades: 8");
		    	System.out.println("Atualizar habilidade do usuario: 9");
		    	System.out.println("Excluir habilidade do usuario: 10");
		    	System.out.println("Buscar vaga: 15");
		    }
		    if(tipoUsuario.equals("RECRUITER")){
		    	System.out.println("Cadastrar vagas no servidor: 11");
		    	System.out.println("Atualizar dados de vaga no servidor: 12");
		    	System.out.println("Ver vagas disponibilizadas: 13");
		    	System.out.println("Excluir vaga cadastradas no servidor: 14");
		    }
		    
	        
	   }

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}
