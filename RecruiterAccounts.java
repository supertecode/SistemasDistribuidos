package trocajsons;
import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class Empresa {
	private String id;
    private String email;
    private String nome;
    private String senha;
    private String industry;
    private String description;
    private String role;
    private String token;
    private ArrayList<Vagas> jobset;

  
    public Empresa(String email, String nome, String senha, String industry, String description) {
		this.email = email;
		this.nome = nome;
		this.senha = senha;
		this.industry = industry;
		this.description = description;
		this.role = "RECRUITER";
		this.token = "";
		this.jobset = new ArrayList<Vagas>();
	}

	@Override
    public String toString() {
        return "Email: " + email + ", Nome: " + nome + ", Senha: " + senha;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

    public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}
	
	public void unSetToken() {
		this.token = "";
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Vagas> getJobset() {
		return jobset;
	}

	public void setJobset(ArrayList<Vagas> jobset) {
		this.jobset = jobset;
	}
	
	//Métodos para manipulação das vagas	
	
	 private String gerarIdUnico() {
	        int id = 1;
	        boolean idUnico;
	        do {
	            idUnico = true;
	            for (Vagas vaga : jobset) {
	                if (vaga.getId().equals(String.valueOf(id))) {
	                    id++;
	                    idUnico = false;
	                    break;
	                }
	            }
	        } while (!idUnico);
	        return String.valueOf(id);
	    }
	 
    public void adicionarVaga(String descricao, String tempo) {
    	String id = gerarIdUnico();
        this.jobset.add(new Vagas(descricao, tempo, id));
        //preciso fazer a lógica para incluir o id
    }	
    
    public ArrayList<Vagas> getVagas() {
        return jobset;
    }
    
    public boolean removerVaga(String id) {
    	for (Vagas vaga : jobset) {
    		if(vaga.getId().equals(id)){
    			this.jobset.remove(vaga);
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean atualizarVaga(String id, String novaDescricao, String novoTempo) {
        for (Vagas vaga : jobset) {
            if (vaga.getId().equals(id)) {
            	vaga.setSkill(novaDescricao);
            	vaga.setExperience(novoTempo);
                return true; // Atualização bem-sucedida
            }
        }
        return false; // Experiência não encontrada
    }
    
    public boolean possuiVaga(String descricao) {
        for (Vagas vaga : jobset) {
            if (vaga.getSkill().equals(descricao)) {
                return true; // Experiência encontrada
            }
        }
        return false; // Experiência não encontrada
    }
	
}


public class RecruiterAccounts {
    private static final String ARQUIVO_EMPRESAS = "accountsRecruiter.json";
    private static List<Empresa> empresas = new ArrayList<>();

    public static void main(String[] args) {
        carregarEmpresas();
        exibirEmpresas();
    }

    public static void cadastrarEmpresa(Empresa empresa) {
        empresas.add(empresa);
        salvarEmpresas();
    }

    public static Empresa procurarEmpresa(String email) {
        for (Empresa empresa : empresas) {
            if (empresa.getEmail().equals(email)) {
                return empresa;
            }
        }
        return null;
    }
    
    public static Empresa procurarEmpresaPorId(String id) {
        for (Empresa empresa : empresas) {
            if (empresa.getId().equals(id)) {
                return empresa;
            }
        }
        return null;
    }

    public static void atualizarEmpresa(Empresa user) {
    	/*for (Usuario usuario : usuarios) {
              if (usuario.getId().equals(user.getId())) {
                usuario.setEmail(user.getEmail());
                usuario.setNome(user.getNome());
                usuario.setSenha(user.getSenha());
            }
        }
        */
        // Não é necessário fazer nada aqui, pois o usuário já está na lista
        salvarEmpresas();
    }

    public static void deletarEmpresa(String email) {
        Empresa empresa = procurarEmpresa(email);
        if (empresa != null) {
            empresas.remove(empresa);
            salvarEmpresas();
        }
    }

    public static void exibirEmpresas() {
        for (Empresa empresa : empresas) {
            System.out.println(empresa);
        }
    }

    public static void carregarEmpresas() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_EMPRESAS))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            TypeToken<List<Empresa>> typeToken = new TypeToken<List<Empresa>>() {};
            empresas = new Gson().fromJson(jsonBuilder.toString(), typeToken.getType());
        } catch (IOException e) {
            // Se o arquivo não existir ou estiver vazio, não há usuários a carregar
        }
    }

    private static void salvarEmpresas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_EMPRESAS))) {
            String json = new Gson().toJson(empresas);
            bw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Empresa> retornarEmpresas() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_EMPRESAS))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            TypeToken<List<Empresa>> typeToken = new TypeToken<List<Empresa>>() {};
            return new Gson().fromJson(jsonBuilder.toString(), typeToken.getType());
        } catch (IOException e) {
            // Se o arquivo não existir ou estiver vazio, retorna null ou uma lista vazia
            e.printStackTrace(); // Para facilitar o debug
            return null; // Ou return Collections.emptyList();
        }
    }
}
