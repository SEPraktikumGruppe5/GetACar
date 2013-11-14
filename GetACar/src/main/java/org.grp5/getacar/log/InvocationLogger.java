package org.grp5.getacar.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.onami.logging.core.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public class InvocationLogger implements MethodInterceptor {

    @InjectLogger
    Logger logger;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Method '").append(method.getName()).append("' of class '")
                .append(method.getDeclaringClass()).append("' is being invoked.");
        logger.debug(stringBuilder.toString());

        return invocation.proceed();
    }
}