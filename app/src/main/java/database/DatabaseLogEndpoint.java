package database;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.util.DateTime;

import java.util.Date;

import model.logBeanApi.LogBeanApi;
import model.logBeanApi.model.LogBean;
import utils.Settings;


/**
 * Created by Daniel on 01.09.2015.
 */
public class DatabaseLogEndpoint extends Application {

    private static LogBeanApi myApiService = null;
    private static String uuid;


    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("InstallSettings", MODE_PRIVATE);
        uuid = sharedPreferences.getString("UUID", "no id available - DatabaseLogEndpoint");
    }



    class InsertTask extends AsyncTask<LogBean, Void, Void> {
        @Override
        protected Void doInBackground(LogBean... params) {
            instantiateConnection();

            try {
                myApiService.insertLogBean(params[0]).execute();
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


    public void insertTask(String errorPath, String errorText){
        LogBean logBean = new LogBean();
        logBean.setErrorPath(errorPath);
        logBean.setErrorText(errorText);
        logBean.setUuid(uuid);
        logBean.setInsertDate(new DateTime(new Date()));
        new InsertTask().execute(logBean);
    }



    public void instantiateConnection(){
        if (myApiService == null) {

            /*
            //ONLY FOR LOCAL TESTING PURPOSES
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
            Log.e("DB ENDPOINT", "Creating Log myApiService");
            myApiService = builder.build();
            */

            LogBeanApi.Builder builder = new LogBeanApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Settings.PROJECT_ADDRESS);
            myApiService = builder.build();

        }
    }


}
