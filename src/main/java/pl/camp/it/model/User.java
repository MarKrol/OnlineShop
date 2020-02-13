package pl.camp.it.model;

public class User {
    private int id;
    private String name;
    private String pass;
    private UserRole userRole;

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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.id).append("<:@:>").append(this.name).append("<:@:>").append(this.pass).append("<:@:>").
                append(this.userRole);

        return sb.toString();
    }
}
