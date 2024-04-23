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
