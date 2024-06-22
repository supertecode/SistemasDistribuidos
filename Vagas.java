package trocajsons;

public class Vagas {
	private String skill;
	private String experience;
	private String id;
	

	public Vagas(String skill, String experience, String id) {
		super();
		this.skill = skill;
		this.experience = experience;
		this.id = id;
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
	
	

}
