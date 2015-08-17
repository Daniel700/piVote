package database;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;

/**
 * Created by Daniel on 16.08.2015.
 */
public class DatabaseEndpoint extends AsyncTask <PollBean, Void, Void> {
    private static PollBeanApi myApiService = null;

    @Override
    protected Void doInBackground(PollBean... params) {

        if(myApiService == null) {  // Only do this once
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
            // end options for devappserver

            myApiService = builder.build();
        }


        try {
            myApiService.insertPollBean(params[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
