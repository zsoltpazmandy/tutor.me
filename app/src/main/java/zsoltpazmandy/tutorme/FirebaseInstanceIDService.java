package zsoltpazmandy.tutorme;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zsolt on 28/07/16.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        System.out.println("from HERE -----------------");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",refreshedToken)
                .build();

        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/zsoltpazmandy/tutor.me/master/index.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            System.out.println("just after the PHP request -------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }


