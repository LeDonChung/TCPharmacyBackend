package vn.edu.iuh.fit.pharmacy.POJOs;

public enum AddressType {
    Home("Home"), Other("Other");
    private final String name;

    AddressType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
