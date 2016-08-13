package zsoltpazmandy.tutorme;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.sql.SQLException;

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
    }

}


