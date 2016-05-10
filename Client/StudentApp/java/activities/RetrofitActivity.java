package se.yrgo.java15.patrik.studentapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.yrgo.java15.patrik.studentapp.R;
import se.yrgo.java15.patrik.studentapp.remote.RetrofitAPI;

/**
 *  Contains:   onCreate metod,
 *              @Bind-Events(Butterfly API) connected to our components.
 *              Call && CallBacks(Retrofit API) to handle in and output
 */
public class RetrofitActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            RetrofitActivity.class.getName();

    // ButterKnife API to collect all my componets
    @Bind(R.id.button_addStudent) Button button_addStudent;
    @Bind(R.id.textView_output) TextView textView_output;

    private RetrofitActivity retrofitActivity;

    //////////////////////////////////////////////////////////////////////////////////
    /////       <-- onCreate -->>                                                /////
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        ButterKnife.bind(this);

        retrofitActivity = RetrofitActivity.this;
    }

    //////////////////////////////////////////////////////////////////////////////////
    /////       <-- @Onclick Events !! -->>                                      /////
    //////////////////////////////////////////////////////////////////////////////////

    @OnClick(R.id.button_addStudent)
    public void onClick_button(){

        RetrofitAPI restAPI = RetrofitAPI.ClientFactory.getInstance(retrofitActivity);
        restAPI.getMedlemmar().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){

                    List<String> medlemsLista = response.body();

                    textView_output.setText(medlemsLista.toString());

                    Toast.makeText(retrofitActivity, "Updated", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(LOG_TAG, "RestActivity.onClick.Button.onResponse -> " +
                            "Failed to create List<Medlemmar>");
                    Toast.makeText(retrofitActivity, "Respone Error, Check database",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(LOG_TAG, "RestActivity.onClick.Button.onFailure -> " +
                        "Failed to make response");
                Toast.makeText(retrofitActivity, "No response, Check database",
                        Toast.LENGTH_SHORT).show();
            }


        });

    }
}
