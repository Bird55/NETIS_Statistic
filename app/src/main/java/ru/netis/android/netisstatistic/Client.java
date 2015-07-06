package ru.netis.android.netisstatistic;

public class Client {
    private String name;
    private String id;
    private double saldo;
    private String contract;
    private String contractDate;
    private String IPv4;
    private String IPv6;
    boolean ownership; // if true - person else - organization

    public Client(boolean ownership, String name, String id, double saldo, String contract, String contractDate) {
        this(ownership, name, id, saldo, contract, contractDate, null, null);
    }

    public Client(boolean ownership, String name, String id, double saldo, String contract, String contractDate, String IPv4) {
        this(ownership, name, id, saldo, contract, contractDate, IPv4, null);
    }

    public Client(boolean ownership, String name, String id, double saldo, String contract, String contractDate, String IPv4, String IPv6) {
        this.ownership = ownership;
        this.name = name;
        this.id = id;
        this.saldo = saldo;
        this.contract = contract;
        this.contractDate = contractDate;
        this.IPv4 = IPv4;
        this.IPv6 = IPv6;
    }

    public boolean isPerson() {
        return ownership;
    }

    public boolean isOrganization() {
        return !ownership;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getContract() {
        return contract;
    }

    public String getContractDate() {
        return contractDate;
    }

    public String getIPv4() {
        return IPv4;
    }

    public String getIPv6() {
        return IPv6;
    }
}
