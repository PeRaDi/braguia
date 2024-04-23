package pt.uminho.braguia.auth;


public interface AuthenticationService {

    boolean isAuthenticated();

    void login(String username, String password);

}
