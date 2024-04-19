package pt.uminho.braguia.services;

import android.content.Context;
import android.os.Handler;

import pt.uminho.braguia.api.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService implements IBraGuiaService {

    private Context context;
    private final EncryptedSharedPreferencesService sharedPreferencesService;

    public AuthService(Context context) {
        this.context = context;
        this.sharedPreferencesService = ServiceContainer.getInstance(context).getService(EncryptedSharedPreferencesService.class);
    }

    public boolean isAuthenticated() {
        // csrftoken=fliUCn8vDBpTm362cs0aipdugDxhUrc7BcliGStIrYoOfexoNX07U6cSeTGRi2Jk
        // sessionid=lni15neiel09yup6eco0jep96szvkiwm

        String csrfToken = sharedPreferencesService.getSharedPreferences().getString("csrftoken", null);
        String sessionId = sharedPreferencesService.getSharedPreferences().getString("sessionid", null);

        if (csrfToken == null || sessionId == null)
            return false;

        return true;

    }
}
