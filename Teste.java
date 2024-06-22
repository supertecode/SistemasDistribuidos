package trocajsons;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;


public class Teste {
	
	public static void main(String[] args) {
		ArrayList<Experiencia> lista = new ArrayList<Experiencia>();
	    Experiencia experiencia = new Experiencia("e","3");
		Experiencia experiencia2 = new Experiencia("a","2");
		lista.add(experiencia);
		lista.add(experiencia2);
		for(Experiencia nome: lista) {
			System.out.println(nome.getExperiencia());
			System.out.println(nome.getTempo());
		}
		OpGenerica generica = new OpGenerica();
		generica.setData();
		generica.getData().setSkillset(lista);
		generica.setOperation("");
		generica.setToken("");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String nova = gson.toJson(generica);
		System.out.println(nova);
		
		Usuario usuario = new Usuario("alo","alo","123");
		usuario.adicionarExperiencia("c", "123");
		usuario.adicionarExperiencia("java", "1232");
		System.out.println(usuario.getExperiencias());
		System.out.println(usuario.getExperiencias());
		
		Accounts gerenciadorContas = new Accounts();
		gerenciadorContas.carregarUsuarios();
		gerenciadorContas.exibirUsuarios();
		
		Empresa empresa = new Empresa("a","a","a","a","a");
		empresa.setId("11");
		empresa.adicionarVaga("Java", "3");
		empresa.adicionarVaga("Java", "3");
		empresa.adicionarVaga("C", "3");
		empresa.adicionarVaga("Java", "3");
		empresa.adicionarVaga("Java", "3");
		ArrayList<Vagas> listinha = empresa.getJobset();
		for(Vagas vaga : listinha) {
			System.out.println("id: " + vaga.getSkill());
		}
		System.out.println();
		//System.out.println(empresa.getJobset());
		Gson gson1 = new Gson();
		OpGenerica gen = new OpGenerica();
		gen.setData();
		gen.getData().setJobset(empresa.getJobset());
		
		RecruiterAccounts contasEmpresas = new RecruiterAccounts();
		 // CODIGO QUE SERA USADO NO SERVIDOR 
		 ////
		 List<Empresa> empresas = contasEmpresas.retornarEmpresas();
	        if (empresas != null) {
	            empresas.forEach(System.out::println); // Para testar se as empresas foram carregadas
	        } else {
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
		////
		//
		System.out.println("Todos os empregos cadastrados no sistema: " + todasVagasFinal);
		System.out.println("quantidade de vagas: " + todasVagasFinal.size());
		for(int j = 0; j<todasVagasFinal.size(); j++) {
			System.out.println("Vaga " + (j + 1));
			System.out.println(todasVagasFinal.get(j).getSkill());
			System.out.println(todasVagasFinal.get(j).getExperience());
			System.out.println("id: " + todasVagasFinal.get(j).getId());
		}
		String experience = "9";
		List<String> skill = new ArrayList<>();
		skill.add("Java");
		skill.add("C");
		
		List<Vagas> vagasFiltradas = new ArrayList<>();
		//FILTROS
		//somente para o Experience
		for(Vagas vaga: todasVagasFinal) {
			if(Long.parseLong(vaga.getExperience()) <= Long.parseLong(experience)){
				vagasFiltradas.add(vaga);//vagasFiltradas.add(vaga);
			}
		}
		//
		OpGenericaAlt op = new OpGenericaAlt();
		op.setData();
		op.getData().setEmail("Ss");
		op.getData().setDescription("sdsd");
	    List<String> teste = op.getNonNullFields(op.getData());
	    for(String nome: teste) {
	    	System.out.println(nome);
	    }
		System.out.println("TESTE:");
	   
		System.out.println("Quantidade de vagas filtradas: " + vagasFiltradas.size());
	}

}
