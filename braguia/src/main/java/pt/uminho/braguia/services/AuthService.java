package pt.uminho.braguia.services;

import android.content.Context;

public class AuthService implements IBraGuiaService {

    private Context context;

    public AuthService(Context context) {
        this.context = context;
    }

    private EncryptedSharedPreferencesService sharedPreferencesService;

    public boolean isAuthenticated() {
        String token = sharedPreferencesService.getSharedPreferences().getString("token", null);

        if(token == null)
            return false;

        return true;
    }

    @Override
    public void onServiceConstructed() {
        sharedPreferencesService = ServiceContainer.getInstance(context).getService(EncryptedSharedPreferencesService.class);
        System.out.println(sharedPreferencesService);
    }
}
