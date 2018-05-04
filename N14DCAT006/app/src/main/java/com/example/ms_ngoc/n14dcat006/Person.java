package com.example.ms_ngoc.n14dcat006;

public class Person {
    public Person(int id, String ten, String diaChi, boolean tinhTrang) {
        this.id=id;
        this.ten = ten;
        this.diaChi = diaChi;
        this.tinhTrang = tinhTrang;

    }
    public Person(){
        this.ten="";
        this.diaChi="";
        this.tinhTrang=true;
        this.id=-1;
    }

    public String getTen() {
        return ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public boolean getTinhTrang() {
        return tinhTrang;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String toString() {
        return "Person{" +
                "ten='" + ten + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String ten;
    private String diaChi;
    private boolean tinhTrang;
    private int id;

}
