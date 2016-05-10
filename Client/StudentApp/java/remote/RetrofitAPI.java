package se.yrgo.java15.patrik.studentapp.remote;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.SimpleXmlConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import se.yrgo.java15.patrik.studentapp.model.Course;

public interface RetrofitAPI {

    String url = "http://46.194.13.231:8084/";

    // USE THIS FOR Student Objects In APP !! -->
    @GET("StudentAPI?format=json&id=20") Call<List<Course>> getCoursesAsJson();

    @GET("StudentAPI?format=xml&id=20") Call<List<Course>> getCoursesAsXML();

    @GET("Medlemmar") Call<List<String>> getMedlemmar();

    class ClientFactory {

        private static RetrofitAPI service;

        public static RetrofitAPI getInstance(Context context) {
            if (service == null) {

                // Okhttp API handles HTTP connection
                OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();

//                Retrofit retrofitClient = new Retrofit.Builder()
//                        .client(httpClient.build())
//                        .addConverterFactory(SimpleXmlConverterFactory.create())
//                        .baseUrl(JsonUrl)
//                        .build();

                Retrofit retrofitClient = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofitClient.create(RetrofitAPI.class);

                return service;
            } else {
                return service;
            }
        }
    }
}
