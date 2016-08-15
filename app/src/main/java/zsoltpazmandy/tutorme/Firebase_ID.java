package zsoltpazmandy.tutorme;

/**
 * Created by zsolt on 14/08/16.
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class Firebase_ID extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    private void registerToken(String token) {


        // ONLY WORKS WHILE ON MY NETWORK

//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token",token)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://192.168.1.19/tutorme/reg.php")
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
