package zsoltpazmandy.tutorme;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.sql.SQLException;

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
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try {
            sendRegistrationToServer(refreshedToken);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String refreshedToken) throws SQLException {
//
//        Connection dbConnection = null;
//        String dbHost = "mysql3.gear.host:3306/";
//        String dbName = "tutorme";
//        String dbDriver = "com.mysql.jdbc.Driver";
//        String username = "tutorme";
//        String password = "pizza_pie";
//        try {
//
//            dbConnection = DriverManager.getConnection("jdbc:mysql://mysql3.gear.host/tutorme","tutorme", "pizza_pie");
//
//            Statement st = dbConnection.createStatement();
//            ResultSet res = st.executeQuery("INSERT INTO tokens (Token) VALUES (" + refreshedToken + ") " +
//                    "ON DUPLICATE KEY UPDATE Token =" + refreshedToken + "");
//
//        } catch (SQLException s) {
//            s.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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


