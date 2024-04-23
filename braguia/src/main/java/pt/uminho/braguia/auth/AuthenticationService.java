package pt.uminho.braguia.auth;


import pt.uminho.braguia.user.User;
import pt.uminho.braguia.util.ResultConsumer;

public interface AuthenticationService {

    User currentUser();

    boolean isAuthenticated();

    void login(String username, String password, ResultConsumer<User> result);

    void logout();

}
