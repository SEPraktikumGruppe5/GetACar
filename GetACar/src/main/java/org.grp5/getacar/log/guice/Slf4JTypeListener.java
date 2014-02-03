package org.grp5.getacar.log.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.grp5.getacar.log.guice.annotation.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * Logging type listener to enable automatic logger injection
 */
public class Slf4JTypeListener implements TypeListener {
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType() == Logger.class
                        && field.isAnnotationPresent(InjectLogger.class)) {
                    typeEncounter.register(new Slf4JMembersInjector<T>(field));
                }
            }
        }
    }
}
