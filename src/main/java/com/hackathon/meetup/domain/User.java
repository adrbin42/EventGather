package com.hackathon.meetup.domain;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    int userid;

    @NotNull
    @Column
    String firstname;

    @NotNull
    @Column
    String lastname;

    @NotNull
    @Column(unique=true)
    String username;

    @NotNull
    @Column
    String password;

    @Column
    String email;

    @NotNull
    @Column
    String phone;

    @NotNull
    @Column
    boolean isAdmin;

    public User() {
    }

    public User(String firstname, String lastname, String username, String password, String email, String phone, boolean isAdmin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
         this.password =   password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }



//    public String bCryptPassword(String password){
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String hashedPassword = passwordEncoder.encode(password);
//        return hashedPassword;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUserid() == user.getUserid();
    }

    @Override
    public int hashCode() {
        return getUserid();
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

}
