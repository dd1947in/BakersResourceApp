package com.example.uadnd.cou8901.bakersresourceapp.net;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by dd2568 on 6/17/2017.
 */

public class BakerRecipes {
    public static String GET(String url) {
        String jsonBakerRecipes = ""; // Use Cached tables if there is past data
        try {
            OkHttpClient userAgent = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request httpRequest = builder.url(url).build();

            Response httpResponse = userAgent.newCall(httpRequest).execute() ;
            if(httpResponse.isSuccessful()) {
                jsonBakerRecipes = httpResponse.body().string();
                //Timber.d(jsonBakerRecipes);
            } else {
                Timber.d("httpResponse.isSuccessful() failed");
                return jsonBakerRecipes;
            }

        } catch(IOException ioe){
            Timber.d(ioe);
        }
        return jsonBakerRecipes;
    }
}
