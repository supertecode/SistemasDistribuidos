package trocajsons;

public class Experiencia {
    private String skill;
    private String experience;

    // Construtor
    public Experiencia(String experiencia, String tempo) {
        this.skill = experiencia;
        this.experience = tempo;
    }

    // Getters e Setters
    public String getExperiencia() {
        return skill;
    }

    public void setExperiencia(String experiencia) {
        this.skill = experiencia;
    }

    public String getTempo() {
        return experience;
    }

    public void setTempo(String tempo) {
        this.experience = tempo;
    }

    // Método toString para facilitar a impressão
    @Override
    public String toString() {
        return "Experiencia: " + skill + ", Tempo: " + experience;
    }
}
