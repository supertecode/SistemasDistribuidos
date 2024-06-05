package trocajsons;
//import trocajsons.Operations.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import trocajsons.Operations.LoginRequest;

public class Main {

	public static void main(String[] args) {
		LoginRequest login = new LoginRequest();
		login.getData().setEmail("murzxczxzcilo3wb@assagmail.com");
		login.getData().setPassword("123");
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		String teste = gson.toJson(login);
		System.out.println(teste);
		Gson gson2 = new Gson();
		OpGenerica login2 = (OpGenerica) gson2.fromJson(teste, OpGenerica.class);
		System.out.println(login2.getOperation());
		System.out.println(login2.getData().getEmail());
		System.out.println(login2.getData().getPassword());
		
		//TESTE COM TOKEN
		
		//Token toen = new Token(); 
		//String token = toen.createToken("joao");
       // System.out.println("Token gerado: " + token);
        
        //boolean isValid = toen.verifyToken(token, "joao");
        //System.out.println("Token válido para joao? " + isValid);
        
        //isValid = toen.verifyToken(token, "maria");
        //System.out.println("Token válido para maria? " + isValid);
        //System.out.println("uu");
        
        Accounts gerenciadorContas = new Accounts();
        gerenciadorContas.carregarUsuarios();
        Usuario usario = gerenciadorContas.procurarUsuario("ih");
        if(usario == null) {
        	System.out.println("O usuario nao existe");
        }
        else {
        	System.out.println("O ususario existe");
        	System.out.println(usario.getEmail());
        	System.out.println(usario.getNome());
        	System.out.println(usario.getSenha());
        	System.out.println(usario.getRole());
        	System.out.println(usario.getId());
        	System.out.println(usario.getToken());
        	System.out.println("teste");
        }
        //gerenciadorContas.deletarUsuario("AnaClara@gmail.com ");
        gerenciadorContas.deletarUsuario("sonic");
        gerenciadorContas.deletarUsuario("shadow");
        gerenciadorContas.deletarUsuario("ba");
        gerenciadorContas.deletarUsuario("ih");
        gerenciadorContas.deletarUsuario("murilo3wb@gmail.com");
        gerenciadorContas.deletarUsuario("cachorropidao");
        gerenciadorContas.exibirUsuarios();
	}
	    
}

//aprender a lidar com o token após o login APRENDI
//salvar os cadastros em algum lugar, arquivo ou bando de dados FALTA SO ISSO