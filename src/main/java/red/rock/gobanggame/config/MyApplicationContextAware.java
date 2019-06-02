package red.rock.gobanggame.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/2 15:45
 **/
@Component
@Lazy(false)
public class MyApplicationContextAware  implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }


    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
