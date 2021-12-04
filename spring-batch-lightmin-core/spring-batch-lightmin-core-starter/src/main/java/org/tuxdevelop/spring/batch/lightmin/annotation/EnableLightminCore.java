package org.tuxdevelop.spring.batch.lightmin.annotation;

import java.lang.annotation.*;

/**
 * @author Marcel Becker
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableLightminService
public @interface EnableLightminCore {
}
