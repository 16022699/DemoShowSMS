package demoshowsms.android.myapplicationdev.com.demoshowsms;



import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.app.ActivityCompat;

public class FragmentFirst extends Fragment {

    Button btnAddText;
    TextView tvFrag1;
    EditText etFrag1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        tvFrag1 = (TextView) view.findViewById(R.id.tvFrag1);
        btnAddText = (Button) view.findViewById(R.id.btnAddTextFrag1);
        etFrag1 = (EditText) view.findViewById(R.id.etFrag1);

        btnAddText.setOnClickListener(new View.OnClickListener() {
             @Override
      public void onClick(View view) {

                  int permissionCheck = PermissionChecker.checkSelfPermission
		(getActivity(), android.Manifest.permission.READ_SMS);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_SMS}, 0);
            // stops the action from proceeding further as permission not
            //  granted yet
            return;
        }

        // Create all messages URI
        Uri uri = Uri.parse("content://sms");

         String input = etFrag1.getText().toString();


        // The columns we want
        //  date is when the message took place
        //  address is the number of the other party
        //  body is the message content
        //  type 1 is received, type 2 sent
        String[] reqCols = new String[]{"date", "address", "body", "type"};

        // The filter String
        String filter=" body LIKE ?";
        // The matches for the ?
        String[] filterArgs = {"%" + input + "%"};


        // Get Content Resolver object from which to
        //  query the content provider
        ContentResolver cr = getActivity().getContentResolver();
        // Fetch SMS Message from Built-in Content Provider
        Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

//Cursor cursor = cr.query(uri, reqCols, null, null, null);
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
        tvFrag1.setText(smsBody);
      }
    });
          return view;
  }


      @Override
  public void onRequestPermissionsResult(int requestCode,
		String permissions[], int[] grantResults) {

    switch (requestCode) {
        case 0: {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the read SMS
                //  as if the btnRetrieve is clicked
                btnAddText.performClick();

            } else {
                // permission denied... notify user
                android.widget.Toast.makeText(getActivity(), "Permission not granted",
			android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }
  }


    }
