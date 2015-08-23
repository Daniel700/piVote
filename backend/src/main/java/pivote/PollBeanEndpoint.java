package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;


import model.AnswerBean;
import model.PollBean;
import model.ShardedCounter;

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


    @ApiMethod(name = "getMyPollBeans", path = "myPolls")
    public List<PollBean> getMyPollBeans(@Named("uuid") final String uuid){
        List<PollBean> myPolls;

        myPolls = ObjectifyService.run(new Work<List<PollBean>>() {
            @Override
            public List<PollBean> run() {
                List<PollBean> myPolls = ofy().load().type(PollBean.class).filter("UUID =", uuid).order("-creationDate").list();
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

                        //ToDo: setLastVoted
                        //Load Poll again to make sure updating the current vote amount
                        poll = ofy().load().type(PollBean.class).id(pollBean.getId()).now();

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