package coreylee.com.recruitr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * The user profile settings
 */
public class UserProfile extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers," +
            "picture-url,picture-urls::(original))";
    private TextView user_name;
    private TextView user_email;
    private ImageView profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        // TODO make its own helper class
        linkededinApiHelper();
    }

    // TODO make it's own class
    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    showResult(result.getResponseDataAsJson());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // TODO
            }
        });
    }

    public void showResult(JSONObject response){

        try {
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl")).into(profile_picture);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
