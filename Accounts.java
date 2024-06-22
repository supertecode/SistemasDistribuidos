package trocajsons;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class Usuario {
	private String id;
    private String email;
    private String nome;
    private String senha;
    private String role;
    private String token; 
    private ArrayList<Experiencia> skillset;

    public Usuario(String email, String nome, String senha) {
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.role = "CANDIDATE";
        this.token = "";
        this.skillset = new ArrayList<Experiencia>();
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
	
    //COMPETENCIAS DO USUARIO
    public void adicionarExperiencia(String descricao, String tempo) {
        this.skillset.add(new Experiencia(descricao, tempo));
    }	
    
    public ArrayList<Experiencia> getExperiencias() {
        return skillset;
    }
    
    public boolean removerExperiencia(String skill) {
    	for (Experiencia experiencia : skillset) {
    		if(experiencia.getExperiencia().equals(skill)){
    			this.skillset.remove(experiencia);
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean atualizarExperiencia(String descricaoAntiga, String novaDescricao, String novoTempo) {
        for (Experiencia experiencia : skillset) {
            if (experiencia.getExperiencia().equals(descricaoAntiga)) {
                experiencia.setExperiencia(novaDescricao);
                experiencia.setTempo(novoTempo);
                return true; // Atualização bem-sucedida
            }
        }
        return false; // Experiência não encontrada
    }
    
    public boolean possuiExperiencia(String descricao) {
        for (Experiencia experiencia : skillset) {
            if (experiencia.getExperiencia().equals(descricao)) {
                return true; // Experiência encontrada
            }
        }
        return false; // Experiência não encontrada
    }
	
}

public class Accounts {
    private static final String ARQUIVO_USUARIOS = "accounts.json";
    private static List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        carregarUsuarios();

        // Exemplo de uso
       // Usuario novoUsuario = new Usuario("exemplo@email.com", "Exemplo", "senha123");
        //Usuario novoUsuario2 = new Usuario("exemplo222@email.com", "Exemplo", "senha123");
        //Usuario novoUsuario3 = new Usuario("exemplo333@email.com", "Exemplo", "senha123");
       // cadastrarUsuario(novoUsuario);
       // cadastrarUsuario(novoUsuario2);
      //  cadastrarUsuario(novoUsuario3);
        
        //System.out.println("Usuários cadastrados: ");
        //exibirUsuarios();

        //Usuario usuarioEncontrado = procurarUsuario("exemplo222@email.com");
       // if (usuarioEncontrado != null) {
        //    System.out.println("Usuário encontrado: " + usuarioEncontrado);
        //    usuarioEncontrado.setSenha("novaSenha123");
        //    atualizarUsuario(usuarioEncontrado);
        //    System.out.println("Usuário atualizado: " + usuarioEncontrado);
       // } else {
      //      System.out.println("Usuário não encontrado.");
      //  }

       // deletarUsuario("exemplo@email.com");
      
       // System.out.println("Usuários após deletar: ");
        exibirUsuarios();
    }

    public static void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        salvarUsuarios();
    }

    public static Usuario procurarUsuario(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }
    
    public static Usuario procurarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    public static void atualizarUsuario(Usuario user) {
    	/*for (Usuario usuario : usuarios) {
              if (usuario.getId().equals(user.getId())) {
                usuario.setEmail(user.getEmail());
                usuario.setNome(user.getNome());
                usuario.setSenha(user.getSenha());
            }
        }
        */
        // Não é necessário fazer nada aqui, pois o usuário já está na lista
        salvarUsuarios();
    }

    public static void deletarUsuario(String email) {
        Usuario usuario = procurarUsuario(email);
        if (usuario != null) {
            usuarios.remove(usuario);
            salvarUsuarios();
        }
    }

    public static void exibirUsuarios() {
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    public static void carregarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_USUARIOS))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            TypeToken<List<Usuario>> typeToken = new TypeToken<List<Usuario>>() {};
            usuarios = new Gson().fromJson(jsonBuilder.toString(), typeToken.getType());
        } catch (IOException e) {
            // Se o arquivo não existir ou estiver vazio, não há usuários a carregar
        }
    }

    private static void salvarUsuarios() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            String json = new Gson().toJson(usuarios);
            bw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



