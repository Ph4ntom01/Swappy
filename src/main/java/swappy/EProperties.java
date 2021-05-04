package swappy;

public enum EProperties {

    DARK_MODE("frame.dark"), MINIMIZE("frame.minimize"), DNS_WINDOWS("dns.windows"), DNS_ALTERNATIVE("dns.alternative"), NET_ADAPTER("net.adapter");

    private String key;

    private EProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
