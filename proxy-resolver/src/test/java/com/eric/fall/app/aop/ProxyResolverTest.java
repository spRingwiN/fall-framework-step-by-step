package com.eric.fall.app.aop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyResolverTest {

    @Test
    public void testProxyResolver() {
        OriginBean origin = new OriginBean();
        origin.name = "Bob";
        assertEquals("Hello, Bob.", origin.hello());

        // create proxy
        OriginBean proxy = new ProxyResolver().createProxy(origin, new PoliteInvocationHandler());

        // Proxy 类名，类似OriginBean$ByteBuddy$9hQwRy3T:
        System.out.println(proxy.getClass().getName());
        // proxy class, not origin class:
        assertNotSame(OriginBean.class, proxy.getClass());
        // proxy.name is null
        assertNull(proxy.name);
        // with @Polite annotation
        assertEquals("Hello, Bob!", proxy.hello());
        // no @Polite annotation
        assertEquals("Morning, Bob.", proxy.morning());














    }

}
