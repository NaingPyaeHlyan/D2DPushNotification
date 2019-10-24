package mm.com.d2dpushnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {

    private EditText etTitle, etBody;
    private Spinner spinnerSubscribe, spinnerSend;
    private Button btnSubscribe, btnSend;
    private RequestQueue requestQueue;

    final protected String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    final protected String serverKey = BuildConfig.serverKey;
    final protected String contentType = "application/json; charset=utf-8";
  //  final private String key = "AIzaSyC_U1HmbEwVAh42Qqw8GWv2pWwS-E5yiA0";
    private String newToken, subscribe_to, send_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etTitle = findViewById(R.id.et_title);
        etBody = findViewById(R.id.et_message);

        btnSubscribe = findViewById(R.id.btn_subscribe);
        btnSend = findViewById(R.id.btn_send);

        spinnerSubscribe = findViewById(R.id.spinnerSubscribe);
        spinnerSend = findViewById(R.id.spinnerSend);

        requestQueue = Volley.newRequestQueue(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
                Log.e("My Token", newToken);
            }
        });
    }

    public void subScribe(View view) {
        subscribe_to = spinnerSubscribe.getSelectedItem().toString().trim().replaceAll(" +" , "").toLowerCase();
        scribe(subscribe_to);
    }

    private void scribe(final String subscribe_to) {
        FirebaseMessaging.getInstance().subscribeToTopic(subscribe_to).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, subscribe_to + ": SubscribeToTopic is Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unSubscribe(View view){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(subscribe_to).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, subscribe_to + ": UnsubscribeFromTopic is Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNoti(View view) {
        send_to = spinnerSend.getSelectedItem().toString().trim().toLowerCase();

        JSONObject mainObject = new JSONObject();
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", etTitle.getText());
            notification.put("message", etBody.getText());

            mainObject.put("to","/topics/" + send_to);
            mainObject.put("data", notification);
            //       mainObject.put("user_token", newToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendNotification(mainObject);
    }

    public void sendNotification(JSONObject mainObject){
        JsonObjectRequest request = new JsonObjectRequest(FCM_URL, mainObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                etTitle.setText("");
                etBody.setText("");
                Toast.makeText(MainActivity.this, send_to + ": is Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Request Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", "key=" + serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        requestQueue.add(request);
    }
}

// This is my Json format
//        {
//            "send_to":"/topic/admin",
//            "data": {
//              "title": "Notification title",
//              "message": "Notification message"
//        },
//            "to":["fd-Ww2O6fB0:APA91bFa6iqhVUvL_phQSCKIjL6axi6dSCOwwYibwgUgRdJURdUMN4MFA0b-65r3bX0fXfZriPt-kVGeLO3sayqV0HBgt11TNYudJjpPHqSRQpcW-ywxvxR9hMujpcdQY8VpmUMtDAsq"]
//        }

