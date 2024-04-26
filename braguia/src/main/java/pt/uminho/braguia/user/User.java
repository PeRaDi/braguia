package pt.uminho.braguia.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    @NonNull
    @SerializedName("username")
    private String username;

    @SerializedName("user_type")
    @ColumnInfo(name = "user_type")
    private String type;

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    private String firstName;

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("is_superuser")
    @ColumnInfo(name = "is_superuser")
    private boolean superUser;

    @SerializedName("is_staff")
    @ColumnInfo(name = "is_staff")
    private boolean staff;

    @SerializedName("is_active")
    @ColumnInfo(name = "is_active")
    private boolean active;

    @SerializedName("date_joined")
    @ColumnInfo(name = "date_joined")
    private Date dateJoined;

    @SerializedName("last_login")
    @ColumnInfo(name = "last_login")
    private Date lastLogin;

    @SerializedName("groups")
    private List<String> groups;

    @SerializedName("user_permissions")
    private List<Integer> permissions;

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
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
