package escala_plantoes.com.example.demo.domain.plantao;

public enum Turno {
    MANHA(6), TARDE(6), NOITE(12);

    private final int hours;

    Turno(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }
}
