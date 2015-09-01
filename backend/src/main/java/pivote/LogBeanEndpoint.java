package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Date;
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
    public LogBean insert(@Named("errorPath") String errorPath, @Named("errorText") String errorText, @Named("uuid") String uuid) {

        final LogBean bean = new LogBean();
        bean.setErrorPath(errorPath);
        bean.setErrorText(errorText);
        bean.setInsertDate(new Date());
        bean.setUuid(uuid);

        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {
                ofy().save().entity(bean).now();
            }
        });

        return bean;
    }




}