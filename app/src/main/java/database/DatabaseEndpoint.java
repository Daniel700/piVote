package database;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.logBeanApi.LogBeanApi;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;

/**
 * Created by Daniel on 16.08.2015.
 */



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
                            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                            endpoint.insertLogTask("InsertTask - Async", e.getMessage());
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
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("GetTop100PollsTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return beans;
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
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("GetMyPollsTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return beans;
                }
            }


            class GetPollTask extends AsyncTask<Long, Void, PollBean> {
                @Override
                protected PollBean doInBackground(Long... params) {

                    instantiateConnection();
                    PollBean pollBean = null;

                    try {
                        pollBean = myApiService.getPollBean(params[0]).execute();
                    }
                    catch (Exception e) {
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("GetPollTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return pollBean;
                }
            }


            class UpdatePollTask extends AsyncTask<Pair<PollBean, String>, Void, Void> {
                @Override
                protected Void doInBackground(Pair<PollBean, String>... params) {

                    instantiateConnection();
                    PollBean pollBean = params[0].first;
                    String answer = params[0].second;

                    try {
                        myApiService.updatePollBean(answer, pollBean).execute();
                    }
                    catch (Exception e) {
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("UpdatePollTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return null;
                }
            }


            class GetRandomPollsTask extends AsyncTask<Void, Void, List<PollBean>> {
                private String language;
                private String category;

                GetRandomPollsTask(String lang, String cat){
                    this.language = lang;
                    this.category = cat;
                }

                @Override
                protected List<PollBean> doInBackground(Void... params) {

                    instantiateConnection();
                    List<PollBean> beans = null;

                    try {
                        beans = myApiService.getRandomPollBeans(category, language).execute().getItems();
                    }
                    catch (IOException e) {
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("GetRandomPollsTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return beans;
                }
            }


            class GetBatchPollTask extends AsyncTask<Void, Void, List<PollBean>> {

                private List<Long> ids;
                GetBatchPollTask(List<Long> ids){
                    this.ids = ids;
                }
                @Override
                protected List<PollBean> doInBackground(Void... params) {

                    instantiateConnection();
                    List<PollBean> polls = null;

                    try {
                        polls = myApiService.getBatchPollBeans(ids).execute().getItems();
                    }
                    catch (Exception e){
                        DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                        endpoint.insertLogTask("GetBatchPollTask - Async", e.getMessage());
                        e.printStackTrace();
                    }

                    return polls;
                }
            }




    /*
    ###############################################################################################
                   Methods for accessing these Tasks from outside of the class
    ###############################################################################################
     */


    public List<PollBean> getBatchPollTask(List<Long> ids){
        List<PollBean> pollBean = null;
        try {
            pollBean = new GetBatchPollTask(ids).execute().get();
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("GetBatchPollTask", e.getMessage());
            e.printStackTrace();
        }
        return pollBean;
    }


    public List<PollBean> getRandomPollsTask(int languagePos, int categoryPos){
        List<PollBean> beans = null;

        String language = FilterOptions.languages.get(languagePos);
        String category = FilterOptions.categories.get(categoryPos);

        try {
            beans = new GetRandomPollsTask(language, category).execute().get();
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("GetRandomPollsTask", e.getMessage());
            e.printStackTrace();
        }

        return beans;
    }


    public void updatePollTask(PollBean pollBean, String answer){

        Pair pair = Pair.create(pollBean, answer);

        try {
            new UpdatePollTask().execute(pair);
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("UpdatePollTask", e.getMessage());
            e.printStackTrace();
        }

    }


    public PollBean getPollTask(Long id){

        PollBean pollBean = null;

        try {
            pollBean = new GetPollTask().execute(id).get();
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("GetPollTask", e.getMessage());
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
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("GetMyPollsTask", e.getMessage());
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
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("GetTop100PollsTask", e.getMessage());
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