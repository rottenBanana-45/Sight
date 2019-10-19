package com.example.sight.JavaClasses;

public class User {
    public String name, email, studentID, type, uid;
    public boolean isAdmin;

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getStudentID() {
        return studentID;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
}
