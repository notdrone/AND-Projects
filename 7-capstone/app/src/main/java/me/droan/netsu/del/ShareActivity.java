package me.droan.netsu.del;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import me.droan.netsu.R;

public class ShareActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ShareActivity";
    Cursor c;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        String[] from = new String[]{"COL_name"};
        int[] to = new int[]{android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                String subject = getString(R.string.try_the_netsu_app);
                String addresses[] = {c.getString(c.getColumnIndex(UserDataTable.FIELD_COL_EMAILID))};
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity((Intent.createChooser(emailIntent, getString(R.string.send_email))));
                } else {
                    Toast.makeText(ShareActivity.this, R.string.no_email_client, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveEmailsToDB() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    //to get the contact names
                    String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.e("Name :", name);
                    String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if (email != null) {
                        UserData data = new UserData();
                        data.id = id;
                        data.emailId = email;
                        data.name = name;
                        cr.insert(UserDataTable.CONTENT_URI, UserDataTable.getContentValues(data, false));
                    }
                }
                cur1.close();
            }
        }
    }

    private void checkIfEmailsAlreadyExist() {


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UserDataTable.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        c = data;
        if (c.getCount() > 0) {
            adapter.swapCursor(c);
        } else {
            saveEmailsToDB();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}



