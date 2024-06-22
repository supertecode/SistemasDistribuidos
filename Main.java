//ESSA CLASSE SERVE SOMENTE PARA TESTES RAPIDOS
//
//
//


package trocajsons;
//import trocajsons.Operations.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {

		
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
        RecruiterAccounts empresas = new RecruiterAccounts(); 
        empresas.carregarEmpresas();
        Empresa empresa = new Empresa("murilo","murilo","murilo","murilo","murilo");
        empresas.cadastrarEmpresa(empresa);
        System.out.println("Empresas cadastradas:");
        empresas.exibirEmpresas();
        Usuario usario = gerenciadorContas.procurarUsuario("murilo3wb@gmail.com ");
        empresas.deletarEmpresa("murilo");
        empresas.deletarEmpresa("murilo");
        Empresa empresaAchar = empresas.procurarEmpresa("mu");
        
        if(empresaAchar == null) {
        	System.out.println("Empresa nao existe");
        }
        else {
        	System.out.println("Empresa existe");
        	System.out.println(empresaAchar.getEmail());
        	System.out.println(empresaAchar.getNome());
        	System.out.println(empresaAchar.getSenha());
        	System.out.println(empresaAchar.getRole());
        	System.out.println(empresaAchar.getId());
        	System.out.println(empresaAchar.getIndustry());
        	System.out.println(empresaAchar.getToken());
        	System.out.println("teste");
        }
        
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
        gerenciadorContas.deletarUsuario("cand");
        System.out.println("Candidatos");
        gerenciadorContas.exibirUsuarios();
        
        System.out.println("Empresas");
        empresas.exibirEmpresas();
        empresas.deletarEmpresa("e");
        
        String numeroStr = "123";
        int numeroInt = Integer.parseInt(numeroStr);
        System.out.println("Número inteiro: " + numeroInt);
   
	    
 }
}
//aprender a lidar com o token após o login APRENDI
//salvar os cadastros em algum lugar, arquivo ou bando de dados FALTA SO ISSO