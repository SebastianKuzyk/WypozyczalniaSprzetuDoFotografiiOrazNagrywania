public class User {
    public String name;
    public String surname;
    public String mail;
    public String password;
    public int id;
    public String rented;
    private String user_type;

    public User(){

    }
    public User(String name, String surname, String mail, String password, int id){
        setName(name);
        setSurname(surname);
        setMail(mail);
        setPassword(password);
        setId(id);
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRented() {
        return rented;
    }

    public void setRented(String rented) {
        this.rented = rented;
    }
}
