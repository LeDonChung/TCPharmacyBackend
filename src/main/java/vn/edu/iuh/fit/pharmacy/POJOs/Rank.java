package vn.edu.iuh.fit.pharmacy.POJOs;

public enum Rank {
    Bronze("Bronze"), Silver("Silver"), Gold("Gold"), Diamond("Diamond");

    private final String name;

    Rank(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
