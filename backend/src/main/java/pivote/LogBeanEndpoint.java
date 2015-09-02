package pivote;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;

import java.util.logging.Logger;

import model.LogBean;

import static pivote.OfyService.ofy;

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



    @ApiMethod(name = "insertLogBean", path = "insertLog")
    public Void insertLogBean (final LogBean bean) {

        logger.info("Save LogBean: " + bean.getUuid() + "    " + bean.getErrorPath() + "      " + bean.getErrorText());
        ObjectifyService.run(new VoidWork() {
            @Override
            public void vrun() {
                ofy().save().entity(bean).now();
            }
        });

        return null;
    }




}