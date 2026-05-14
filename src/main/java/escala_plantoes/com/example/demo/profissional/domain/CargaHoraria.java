package escala_plantoes.com.example.demo.profissional.domain;

public enum CargaHoraria {
    HORAS_20(20),
    HORAS_30(30),
    HORAS_40(40);

    private final int horas;

    CargaHoraria(int horas) {
        this.horas = horas;
    }

    public int getHoras() {
        return horas;
    }
}
