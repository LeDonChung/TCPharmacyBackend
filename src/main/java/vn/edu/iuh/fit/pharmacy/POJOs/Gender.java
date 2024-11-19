package vn.edu.iuh.fit.pharmacy.POJOs;

public enum Gender {
    Female("Female"), Male("Male"), Other("Other");
    private final String name;

    Gender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
