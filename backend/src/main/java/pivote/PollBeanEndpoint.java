package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

import model.AnswerBean;
import model.PollBean;

import static pivote.OfyService.ofy;


/**
 * An endpoint class we are exposing
 */
@Api(
        name = "pollBeanApi",
        version = "v1",
        resource = "pollBean",
        namespace = @ApiNamespace(
                ownerDomain = "model",
                ownerName = "model",
                packagePath = ""
        )
)
public class PollBeanEndpoint {

    private static final Logger logger = Logger.getLogger(PollBeanEndpoint.class.getName());


    /**
     * This method requests 200 random PollBeans of the backend.
     * Working internally with requesting a key-list that matches your filter options.
     * Then shuffling the key-list per Collections.shuffle method and requesting the desired amount of PollBeans (200 max.) per BatchRequest
     * @param language The language to filter for
     * @param category The category to filter for
     * @return A list of 200 random PollBeans at maximum, less if there are not enough found matching your filter options.
     */
    @ApiMethod(name = "getRandomPollBeans", path = "randomPolls")
    public List<PollBean> getRandomPollBeans(@Named("language") final String language, @Named("category") final String category){

        // Random Polls, Lade komplette Liste mit Keys only und wähle dann zufällig 200 keys aus dieser liste aus, anschließend batch load
        // Wahrscheinlich nur effizient bis ca. 1000 Entities
        // Andere Möglichkeit: beim speichern eines Polls den zuletzt gespeicherten laden und ein attribut des neuen Polls inkrementieren und abspeichern (alles in transaktion)
        // anschließend range query ( von random bis count-1) [count-1 deshalb da auf das letzte eh oft zugegriffen wird)
        // ofy().load().type(PollBean.class).reverse().first().now();
        // ofy().load().type(PollBean.class).startAt().list();


        List<PollBean> randoms;
        randoms = ObjectifyService.run(new Work<List<PollBean>>() {
            @Override
            public List<PollBean> run() {
                List<Key<PollBean>> keyList;
                List<Long> ids = new ArrayList<Long>();

                if (!language.equals("All") && category.equals("All"))
                {
                    //list = ofy().load().type(PollBean.class).filter("language =", language).list();
                    keyList = ofy().load().type(PollBean.class).filter("language =", language).keys().list();

                    Collections.shuffle(keyList);
                    for (int i = 0; i < (keyList.size() < 200 ? keyList.size() : 200); i++){
                        ids.add(keyList.get(i).getId());
                    }
                    return getBatchPollBeans(ids);
                }
                if (language.equals("All") && !category.equals("All")){
                    //list = ofy().load().type(PollBean.class).filter("category =", category).list();
                    keyList = ofy().load().type(PollBean.class).filter("category =", category).keys().list();

                    Collections.shuffle(keyList);
                    for (int i = 0; i < (keyList.size() < 200 ? keyList.size() : 200); i++){
                        ids.add(keyList.get(i).getId());
                    }
                    return getBatchPollBeans(ids);
                }
                if (!language.equals("All") && !category.equals("All")){
                    //list = ofy().load().type(PollBean.class).filter("language =", language).filter("category =", category).list();
                    keyList = ofy().load().type(PollBean.class).filter("language =", language).filter("category =", category).keys().list();

                    Collections.shuffle(keyList);
                    for (int i = 0; i < (keyList.size() < 200 ? keyList.size() : 200); i++){
                        ids.add(keyList.get(i).getId());
                    }
                    return getBatchPollBeans(ids);
                }

                keyList = ofy().load().type(PollBean.class).keys().list();
                Collections.shuffle(keyList);
                for (int i = 0; i < (keyList.size() < 200 ? keyList.size() : 200); i++){
                    ids.add(keyList.get(i).getId());
                }
                return getBatchPollBeans(ids);

            }
        });
        return randoms;
    }



    @ApiMethod(name = "getBatchPollBeans", path = "getBatchPolls", httpMethod = ApiMethod.HttpMethod.POST)
    public List<PollBean> getBatchPollBeans(@Named("ids") final List<Long> ids){
        Map<Long, PollBean> pollBeanMap;

        logger.info("BatchRequest IDs from LIST: " + String.valueOf(ids.size()));

        //Batch request
        pollBeanMap = ObjectifyService.run(new Work<Map<Long, PollBean>>() {
            @Override
            public Map<Long, PollBean> run() {
                return ofy().load().type(PollBean.class).ids(ids);
            }
        });
        //transform into PollBean List
        List<PollBean> beanList = new ArrayList<>();
        for (PollBean pollBean: pollBeanMap.values()) {
            beanList.add(pollBean);
        }
        return beanList;
    }


    /**
     * This method gets the <code>PollBean</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>PollBean</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getPollBean", path = "getPoll")
    public PollBean getPollBean(@Named("id") final Long id) {
        PollBean pollBean;

        pollBean = ObjectifyService.run(new Work<PollBean>() {
            @Override
            public PollBean run() {
                return ofy().load().type(PollBean.class).id(id).now();
            }
        });
        return pollBean;
    }


    /**
     * This inserts a new <code>PollBean</code> object.
     *
     * @param pollBean The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertPollBean", path = "insertPoll")
    public PollBean insertPollBean(final PollBean pollBean) {

        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {
                ofy().save().entity(pollBean).now();
            }
        });

        logger.info("Calling insertPollBean method");
        return pollBean;
    }


    @ApiMethod(name = "getTop100PollBeans", path = "top100Polls")
    public List<PollBean> getTop100PollBean(){

        List<PollBean> topList;

        topList = ObjectifyService.run(new Work<List<PollBean>>() {
            @Override
            public List<PollBean> run() {
                List<PollBean> top100List = ofy().load().type(PollBean.class).order("-overallVotes").limit(100).list();
                return top100List;
            }
        });
        return topList;

    }

    /**
     * This method requests a list of the last 100 PollBeans which a user has created.
     * @param uuid The unique user id for whom the PollBeans are requested
     * @return A list of PollBeans that belongs to a certain user
     */
    @ApiMethod(name = "getMyPollBeans", path = "myPolls")
    public List<PollBean> getMyPollBeans(@Named("uuid") final String uuid){
        List<PollBean> myPolls;

        myPolls = ObjectifyService.run(new Work<List<PollBean>>() {
            @Override
            public List<PollBean> run() {
                List<PollBean> myPolls = ofy().load().type(PollBean.class).filter("UUID =", uuid).order("-creationDate").limit(100).list();
                return myPolls;
            }
        });
        return myPolls;
    }


    @ApiMethod(name = "updatePollBean", path = "updatePoll")
    public void updatePollBean(final PollBean pollBean, @Named("selectedAnswer") final String selectedAnswer){

        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {

                ofy().transactNew(new VoidWork() {
                    @Override
                    public void vrun() {
                        PollBean poll;

                        //Load Poll again to make sure updating the current vote amount + lastVote Date
                        poll = ofy().load().type(PollBean.class).id(pollBean.getId()).now();
                        Date date = new Date();
                        poll.setLastVoted(date);
                        logger.info(date.toString());
                        logger.info(poll.getId().toString());

                        //Increment the related AnswerVotes and OverallVotes
                        for (AnswerBean bean : poll.getAnswerBeans()) {
                            if (bean.getAnswerText().equals(selectedAnswer))
                                bean.setAnswerVotes(bean.getAnswerVotes() + 1);
                        }
                        poll.setOverallVotes(poll.getOverallVotes() + 1);

                        //Save entity with the new amount of votes
                        ofy().save().entity(poll).now();

                    }
                });

            }
        });


    }



}