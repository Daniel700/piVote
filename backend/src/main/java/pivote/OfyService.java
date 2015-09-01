package pivote;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import model.AnswerBean;
import model.LogBean;
import model.PollBean;

/**
 * Created by Daniel on 16.08.2015.
 */
public final class OfyService {
    /**
     * Default constructor, never called.
     */
    private OfyService() {
    }

    static {
        factory().register(PollBean.class);
        factory().register(LogBean.class);
    }

    /**
     * Returns the Objectify service wrapper.
     * @return The Objectify service wrapper.
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    /**
     * Returns the Objectify factory service.
     * @return The factory service.
     */
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}