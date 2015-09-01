package database;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import model.logBeanApi.LogBeanApi;

/**
 * Created by Daniel on 01.09.2015.
 */
public class DatabaseLogEndpoint extends Application {

    private static LogBeanApi myApiService = null;


    class InsertLogTask extends AsyncTask<Void, Void, Void> {

        private String errorPath;
        private String errorText;
        private String uuid;

        InsertLogTask(String path, String text, String uuid){
            this.errorPath = path;
            this.errorText = text;
            this.uuid = uuid;
        }


        @Override
        protected Void doInBackground(Void... params) {

            instantiateConnection();

            try {
                myApiService.insert(errorPath, errorText, uuid).execute();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }



      /*
    ###############################################################################################
                   Methods for accessing these Tasks from outside of the class
    ###############################################################################################
     */


    public void insertLogTask(String path, String text){
        SharedPreferences sharedPreferences = getSharedPreferences("InstallSettings", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID", "no id available");
        new InsertLogTask(path, text, uuid).execute();
    }




    public void instantiateConnection(){
        if (myApiService == null) {  // Only do this once
            LogBeanApi.Builder builder = new LogBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
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
        }
    }


}
