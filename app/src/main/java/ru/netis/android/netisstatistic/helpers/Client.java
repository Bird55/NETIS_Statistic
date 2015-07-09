package ru.netis.android.netisstatistic.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {
    boolean ownership; // if true - person else - organization
    private String name;
    private String id;
    private double saldo;
    private String contract;
    private String contractDate;
    private String IPv4;
    private String IPv6;

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

    public Client(Parcel source) {
        ownership = source.readByte() != 0;
        name = source.readString();
        id = source.readString();
        saldo = source.readDouble();
        contract = source.readString();
        contractDate = source.readString();
        IPv4 = source.readString();
        IPv6 = source.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ownership ? 1 : 0));
        dest.writeString(name);
        dest.writeString(id);
        dest.writeDouble(saldo);
        dest.writeString(contract);
        dest.writeString(contractDate);
        dest.writeString(IPv4);
        dest.writeString(IPv6);
    }

    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator<Client>() {

        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };
}
