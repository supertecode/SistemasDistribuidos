package trocajsons;


public class OpGenerica {
	
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
	      private String skill;
	      private String experience;
	      private String id;
	      private String industry;
	      private String description;
	      
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
		public String getSkill() {
			return skill;
		}
		public void setSkill(String skill) {
			this.skill = skill;
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
		
		

	  }
}
