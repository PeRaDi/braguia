package pt.uminho.braguia.user;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class User {

    @SerializedName("user_type")
    private String type;

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("is_superuser")
    private boolean superUser;

    @SerializedName("is_staff")
    private boolean staff;

    @SerializedName("is_active")
    private boolean active;

    @SerializedName("date_joined")
    private Date dateJoined;

    @SerializedName("last_login")
    private Date lastLogin;

    @SerializedName("groups")
    private List<String> groups;

    @SerializedName("user_permissions")
    private List<Integer> permissions;

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public boolean isStaff() {
        return staff;
    }

    public boolean isActive() {
        return active;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", superUser=" + superUser +
                ", staff=" + staff +
                ", active=" + active +
                ", dateJoined=" + dateJoined +
                ", lastLogin=" + lastLogin +
                ", groups=" + groups +
                ", permissions=" + permissions +
                '}';
    }
}
