package fr.hyriode.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 05/03/2022 at 18:51
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HyriEventHandler {

    /**
     * The priority of the event
     *
     * @return A {@link HyriEventPriority}
     */
    HyriEventPriority priority() default HyriEventPriority.NORMAL;

}
