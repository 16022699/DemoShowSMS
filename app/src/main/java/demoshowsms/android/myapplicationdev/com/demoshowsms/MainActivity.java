package demoshowsms.android.myapplicationdev.com.demoshowsms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.text.format.DateFormat;


public class MainActivity extends AppCompatActivity {

   TextView tvSms;
  Button btnRetrieve;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tvSms = (TextView) findViewById(R.id.tv);
    btnRetrieve = (Button) findViewById(R.id.btnRetrieve);
    btnRetrieve.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Create all messages URI
        Uri uri = Uri.parse("content://sms");

        // The columns we want
        //  date is when the message took place
        //  address is the number of the other party
        //  body is the message content
        //  type 1 is received, type 2 sent
        String[] reqCols = new String[]{"date", "address", "body", "type"};

        // Get Content Resolver object from which to
        //  query the content provider
        ContentResolver cr = getContentResolver();
        // Fetch SMS Message from Built-in Content Provider
        Cursor cursor = cr.query(uri, reqCols, null, null, null);
        String smsBody = "";
        if (cursor.moveToFirst()) {
          do {
            long dateInMillis = cursor.getLong(0);
            String date = (String) DateFormat
		.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
            String address = cursor.getString(1);
            String body = cursor.getString(2);
            String type = cursor.getString(3);
            if (type.equalsIgnoreCase("1")) {
              type = "Inbox:";
            } else {
              type = "Sent:";
            }
            smsBody += type + " " + address + "\n at " + date
		+ "\n\"" + body + "\"\n\n";
          } while (cursor.moveToNext());
        }
        tvSms.setText(smsBody);
      }
    });
  }
}


