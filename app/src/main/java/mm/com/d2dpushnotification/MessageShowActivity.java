package mm.com.d2dpushnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MessageShowActivity extends AppCompatActivity {
    private String title, message;
    private TextView tvTitle, tvBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);

        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        message = intent.getStringExtra("MESSAGE");

        tvTitle = findViewById(R.id.tvTitle);
        tvBody = findViewById(R.id.tvBody);

        tvTitle.setText(title);
        tvBody.setText(message);

    }
}
