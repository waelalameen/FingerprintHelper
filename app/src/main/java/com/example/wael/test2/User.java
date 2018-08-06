package com.example.wael.test2;

public class User {

    private int id;
    private String name;
    private String address;
    private long createdAt;
    private long updatedAt;

    public User(int id, String name, String address, long createdAt, long updatedAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", name=" + name +
                ", address=" + address +
                ", created at=" + createdAt +
                ", updated at=" + updatedAt +
                "}";
    }
}
