package database;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.List;

import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;

/**
 * Created by Daniel on 16.08.2015.
 */

//ToDo: create inner classes for each method accessing the database  ---   (GetAllPolls) (GetAllPollsFilter)
public class DatabaseEndpoint {

    private static PollBeanApi myApiService = null;



            class InsertTask extends AsyncTask<PollBean, Void, Void> {
                @Override
                protected Void doInBackground(PollBean... params) {

                    instantiateConnection();

                        try {
                            myApiService.insertPollBean(params[0]).execute();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    return null;
                }
            }


            class GetTop100PollsTask extends AsyncTask<Void, Void, List<PollBean>> {
                @Override
                protected List<PollBean> doInBackground(Void... params) {

                    instantiateConnection();
                    List<PollBean> beans = null;

                    try {
                       beans = myApiService.getTop100PollBeans().execute().getItems();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    return beans;
                }

                @Override
                protected void onPostExecute(List<PollBean> pollBeans) {
                    super.onPostExecute(pollBeans);
                }
            }


            class GetMyPollsTask extends AsyncTask<String, Void, List<PollBean>> {
                @Override
                protected List<PollBean> doInBackground(String... params) {

                    instantiateConnection();
                    List<PollBean> beans = null;

                    try {
                        beans = myApiService.getMyPollBeans(params[0]).execute().getItems();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    return beans;
                }
            }


            class GetPollTask extends AsyncTask<Long, Void, PollBean> {
                @Override
                protected PollBean doInBackground(Long... params) {

                    PollBean pollBean = null;

                    try {
                        pollBean = myApiService.getPollBean(params[0]).execute();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    return pollBean;
                }
            }




    /*
    Accessing Tasks from outside of this class
     */

    public PollBean getPollTask(Long id){

        PollBean pollBean = null;

        try {
            pollBean = new GetPollTask().execute(id).get();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return pollBean;
    }


    public List<PollBean> getMyPollsTask(String uuid){

        List<PollBean> beans = null;

        try {
            beans = new GetMyPollsTask().execute(uuid).get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return beans;
    }


    public List<PollBean> getTop100PollsTask(){

        List<PollBean> beans = null;

        try {
            beans = new GetTop100PollsTask().execute().get();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return beans;
    }


    public void insertTask(PollBean pollBean){
        new InsertTask().execute(pollBean);
    }


    public void instantiateConnection(){

        if (myApiService == null) {  // Only do this once
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
        }

    }

}