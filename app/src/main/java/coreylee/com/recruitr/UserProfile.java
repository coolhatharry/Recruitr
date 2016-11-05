package coreylee.com.recruitr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The user profile settings
 */
public class UserProfile extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,location,industry,headline,positions," +
            "specialties,picture-url)";
    private ImageView profile_picture;
    private TextView user_name;
    private TextView user_email;
    private TextView user_headline;
    private TextView user_location;
    private TextView user_industry;

    private TextView company_name;
    private TextView job_title;
    private TextView job_years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        user_headline = (TextView) findViewById(R.id.headline);
        user_location = (TextView) findViewById(R.id.location_name);
        user_industry = (TextView) findViewById(R.id.industry);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        company_name = (TextView) findViewById(R.id.company_name);
        job_title = (TextView) findViewById(R.id.job_title);
        job_years = (TextView) findViewById(R.id.job_years);

        // TODO make its own helper class
        linkededinApiHelper();
    }

    // TODO make it's own class
    public void linkededinApiHelper() {
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

    public void showResult(JSONObject response) {

        try {
            Log.i("RESPONSE", response.toString());
            user_email.setText(response.getString("emailAddress"));
            user_name.setText(response.getString("formattedName"));
            user_headline.setText(response.getString("headline"));

            JSONObject location = response.getJSONObject("location");
            user_location.setText(location.getString("name"));
            user_industry.setText(response.getString("industry"));

            Picasso.with(this).load(response.getString("pictureUrl")).into(profile_picture);

            JSONObject positions = response.getJSONObject("positions");
            JSONArray values = positions.getJSONArray("values");

            if(values != null && values.length() > 0 ) {
                JSONObject currentJob = values.getJSONObject(0);

                // TODO check if isCurrent is True
                company_name.setText(currentJob.getJSONObject("company").getString("name"));
                job_title.setText(currentJob.getString("title"));
                job_years.setText(currentJob.getJSONObject("startDate").getString("year"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
