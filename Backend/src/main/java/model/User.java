package model;

public class User {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;

    public enum GENDER {
        male, female, other

    }

    private GENDER gender;
    private String avatar_url;
    private String bio;
    private String birth_date;  //date
    private String registeredAt;  //date
    private String profession;


    public User() {

    }

    public User(String name, String surname, String username, GENDER gender, String email, String passwordHash, String bio, String birth_date, String profession) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.password = passwordHash;
        this.bio = bio;
        this.birth_date = birth_date;
        this.profession = profession;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender.name();
    }


    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }
}


