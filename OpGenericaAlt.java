package trocajsons;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

public class OpGenericaAlt {
	
	  private String operation;
	  private String status;
	  private String token;
	  private Data data;

	  // Getters and setters
	  
	  public String getStatus() {
	      return status;
	  }

	  public void setStatus(String status) {
	      this.status = status;
	  }

	  public String getOperation() {
	      return operation;
	  }

	  public void setOperation(String operation) {
	      this.operation = operation;
	  }

	  public Data getData() {
	      return data;
	  }

	  public void setData() {
	      this.data = new Data();
	  }
	  
	  

	  public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	// Inner class representing the "data" object
	  public static class Data {
	      private String token;
	      private String email;
	      private String password;
	      private String name;
	      private ArrayList<String> skill;
	      private String experience;
	      private String id;
	      private String industry;
	      private String description;
	      private String skillset_size; 
	      private String jobset_size;
	      private ArrayList<Experiencia> skillset;
	      private ArrayList<Vagas> jobset;
	      private String newSkill;
	      private String filter;
	      private Boolean isActive;
	      
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getExperience() {
			return experience;
		}
		public void setExperience(String experience) {
			this.experience = experience;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
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
		public String getSkillset_size() {
			return skillset_size;
		}
		public void setSkillset_size(String skillset_size) {
			this.skillset_size = skillset_size;
		}
		public ArrayList<Experiencia> getSkillset() {
			return skillset;
		}
		public void setSkillset(ArrayList<Experiencia> skillset) {
			this.skillset = skillset;
		}
		public String getNewSkill() {
			return newSkill;
		}
		public void setNewSkill(String newSkill) {
			this.newSkill = newSkill;
		}
		public String getJobset_size() {
			return jobset_size;
		}
		public void setJobset_size(String jobset_size) {
			this.jobset_size = jobset_size;
		}
		public ArrayList<Vagas> getJobset() {
			return jobset;
		}
		public void setJobset(ArrayList<Vagas> jobset) {
			this.jobset = jobset;
		}
		public ArrayList<String> getSkill() {
			return skill;
		}
		public void setSkill(ArrayList<String> skill) {
			this.skill = skill;
		}
		public String getFilter() {
			return filter;
		}
		public void setFilter(String filter) {
			this.filter = filter;
		}
		public Boolean getIsActive() {
			return isActive;
		}
		public void setIsActive(Boolean isActive) {
			this.isActive = isActive;
		}	
		
		

	  }
	  
	  public static List<String> getNonNullFields(Data data) {
	        List<String> fieldsNotNull = new ArrayList<>();

	        if (data != null) {
	            Field[] fields = data.getClass().getDeclaredFields();

	            for (Field field : fields) {
	                field.setAccessible(true); // Permitir acesso a campos privados
	                try {
	                    Object value = field.get(data);
	                    if (value != null) {
	                        fieldsNotNull.add(field.getName());
	                    }
	                } catch (IllegalAccessException e) {
	                    System.out.println("Não foi possível acessar o campo: " + field.getName());
	                    e.printStackTrace();
	                }
	            }
	        } else {
	            System.out.println("O objeto Data é nulo.");
	        }

	        return fieldsNotNull;
	    }
	  
}

