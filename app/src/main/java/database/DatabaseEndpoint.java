package database;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;
import utils.Settings;

/**
 * Created by Daniel on 16.08.2015.
 */



public class DatabaseEndpoint {

    private static PollBeanApi myApiService = null;



    public static PollBeanApi instantiateConnection(){

        if (myApiService == null) {
                /*
                //ONLY FOR LOCAL TESTING PURPOSES
                PollBeanApi.Builder builder = new PollBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                Log.e("DB ENDPOINT", "Creating myApiService");
                myApiService = builder.build();
                */

                PollBeanApi.Builder builder = new PollBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(Settings.PROJECT_ADDRESS);
                myApiService = builder.build();
            }

        return myApiService;
    }

}