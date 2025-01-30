package online.muydinov.securedoc.enumeration;

public enum TokenType {
    ACCESSS("access-token"),
    REFRESH("refresh-token");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
