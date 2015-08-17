package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;

import java.util.logging.Level;
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
     * This method gets the <code>PollBean</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>PollBean</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getPollBean")
    public PollBean getPollBean(@Named("id") Long id) {
        // TODO: Implement this function

        logger.info("Calling getPollBean method");
        return null;
    }

    /**
     * This inserts a new <code>PollBean</code> object.
     *
     * @param pollBean The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertPollBean")
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
}