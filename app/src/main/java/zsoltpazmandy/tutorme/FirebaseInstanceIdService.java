package zsoltpazmandy.tutorme;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zsolt on 28/07/16.
 */
public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }


