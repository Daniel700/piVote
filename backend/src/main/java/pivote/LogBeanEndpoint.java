package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import model.LogBean;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "logBeanApi",
        version = "v1",
        resource = "logBean",
        namespace = @ApiNamespace(
                ownerDomain = "model",
                ownerName = "model",
                packagePath = ""
        )
)
public class LogBeanEndpoint {

    private static final Logger logger = Logger.getLogger(LogBeanEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(LogBean.class);
    }

    /**
     * Returns the {@link LogBean} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code LogBean} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "logBean/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public LogBean get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting LogBean with ID: " + id);
        LogBean logBean = ofy().load().type(LogBean.class).id(id).now();
        if (logBean == null) {
            throw new NotFoundException("Could not find LogBean with ID: " + id);
        }
        return logBean;
    }

    /**
     * Inserts a new {@code LogBean}.
     */
    @ApiMethod(
            name = "insert",
            path = "logBean",
            httpMethod = ApiMethod.HttpMethod.POST)
    public LogBean insert(LogBean logBean) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that logBean.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(logBean).now();
        logger.info("Created LogBean with ID: " + logBean.getId());

        return ofy().load().entity(logBean).now();
    }

    /**
     * Updates an existing {@code LogBean}.
     *
     * @param id      the ID of the entity to be updated
     * @param logBean the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code LogBean}
     */
    @ApiMethod(
            name = "update",
            path = "logBean/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public LogBean update(@Named("id") Long id, LogBean logBean) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(logBean).now();
        logger.info("Updated LogBean: " + logBean);
        return ofy().load().entity(logBean).now();
    }

    /**
     * Deletes the specified {@code LogBean}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code LogBean}
     */
    @ApiMethod(
            name = "remove",
            path = "logBean/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(LogBean.class).id(id).now();
        logger.info("Deleted LogBean with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "logBean",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<LogBean> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<LogBean> query = ofy().load().type(LogBean.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<LogBean> queryIterator = query.iterator();
        List<LogBean> logBeanList = new ArrayList<LogBean>(limit);
        while (queryIterator.hasNext()) {
            logBeanList.add(queryIterator.next());
        }
        return CollectionResponse.<LogBean>builder().setItems(logBeanList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(LogBean.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find LogBean with ID: " + id);
        }
    }
}