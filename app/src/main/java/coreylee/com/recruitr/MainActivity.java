package coreylee.com.recruitr;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

/**
 * TODO rename to login page, check if the user is already logged in
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PACKAGE = "coreylee.com.recruitr";
    private Button hash_key_button;
    private Button login_linkedin_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_linkedin_button = (Button) findViewById(R.id.linkedin_login_button);
        login_linkedin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_linkedin();
            }
        });

        // TODO remove
        hash_key_button = (Button) findViewById(R.id.show_hash);

        hash_key_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateHashkey();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    /**
     * Handles the login action for the LinkedIn button
     */
    private void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // TODO move
                Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    /**
     * Obtain the permission scope of the require
     *
     * @return permissions requested of Linkedin api
     */
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    /**
     * TODO remove
     */
    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                ((EditText) findViewById(R.id.package_name)).setText(info.packageName);
                ((EditText) findViewById(R.id.hash_key)).setText(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

}
