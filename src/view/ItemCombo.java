package view;

public record ItemCombo(int id, String texto) {
    @Override
    public String toString() { return texto;}
}