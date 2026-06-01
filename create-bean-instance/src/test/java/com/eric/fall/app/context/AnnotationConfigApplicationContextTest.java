package com.eric.fall.app.context;

import com.eric.instance.context.AnnotationConfigApplicationContext;
import com.eric.instance.io.PropertyResolver;
import com.eric.imported.LocalDateConfiguration;
import com.eric.imported.ZonedDateConfiguration;
import com.eric.scan.ScanApplication;
import com.eric.scan.convert.ValueConvertBean;
import com.eric.scan.custom.annotation.CustomAnnotationBean;
import com.eric.scan.init.AnnotationInitBean;
import com.eric.scan.init.SpecifyInitBean;
import com.eric.scan.nested.OuterBean;
import com.eric.scan.primary.DogBean;
import com.eric.scan.primary.PersonBean;
import com.eric.scan.primary.TeacherBean;
import com.eric.scan.sub1.Sub1Bean;
import com.eric.scan.sub1.Sub2.Sub2Bean;
import com.eric.scan.sub1.Sub2.Sub3.Sub3Bean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationConfigApplicationContextTest {

    AnnotationConfigApplicationContext ctx;

    @BeforeEach
    void setCtx() {
        ctx = new AnnotationConfigApplicationContext(ScanApplication.class, createPropertyResolver());
    }

    @Test
    public void testCustomAnnotation() {
        assertNotNull(ctx.getBean(CustomAnnotationBean.class));
        assertNotNull(ctx.getBean("customAnnotation"));
    }

    @Test
    public void testInitMethod() {
        // test @PostConstruct
        var bean1 = ctx.getBean(AnnotationInitBean.class);
        var bean2 = ctx.getBean(SpecifyInitBean.class);
        assertEquals("Scan App / v1.0", bean1.appName);
        assertEquals("Scan App / v1.0", bean2.appName);
    }

    @Test
    public void testImport() {
        assertNotNull(ctx.getBean(LocalDateConfiguration.class));
        assertNotNull(ctx.getBean("startLocalDate"));
        assertNotNull(ctx.getBean("startLocalDateTime"));
        assertNotNull(ctx.getBean(ZonedDateConfiguration.class));
        assertNotNull(ctx.getBean("startZoneDateTime"));
    }

    @Test
    public void testConverter() {
        var bean = ctx.getBean(ValueConvertBean.class);

        assertNotNull(bean.injectedBoolean);
        assertTrue(bean.injectedBooleanPrimitive);
        assertTrue(bean.injectedBoolean);

        assertNotNull(bean.injectedByte);
        assertEquals((byte)123, bean.injectedBytePrimitive);
        assertEquals((byte)123, bean.injectedByte);

        assertNotNull(bean.injectedShort);
        assertEquals((short)12345, bean.injectedShortPrimitive);
        assertEquals((short)12345, bean.injectedShort);

        // primary 类型有默认值
        assertNotNull(bean.injectedInteger);
        assertEquals(1234567, bean.injectedInteger);
        assertEquals(1234567, bean.injectedIntPrimitive);

        assertNotNull(bean.injectedLong);
        assertEquals(123456789_000L, bean.injectedLongPrimitive);
        assertEquals(123456789_000L, bean.injectedLong);

        assertNotNull(bean.injectedFloat);
        assertEquals(12345.6789f, bean.injectedFloatPrimitive, 0.0001f);
        assertEquals(12345.6789f, bean.injectedFloat, 0.0001f);

        assertNotNull(bean.injectedDouble);
        assertEquals(123456789.87654321, bean.injectedDoublePrimitive, 0.0000000001);
        assertEquals(123456789.87654321, bean.injectedDouble, 0.0000000001);

        assertEquals(LocalDate.parse("2025-12-06"), bean.injectedLocalDate);
        assertEquals(LocalTime.parse("11:25:30"), bean.injectedLocalTime);
        assertEquals(LocalDateTime.parse("2025-12-06T11:25:30"), bean.injectedLocalDateTime);
        assertEquals(ZonedDateTime.parse("2025-12-06T11:25:30+08:00[Asia/Shanghai]"), bean.injectedZonedDateTime);
        assertEquals(Duration.parse("P2DT3H4M"), bean.injectedDuration);
        assertEquals(ZoneId.of("Asia/Shanghai"), bean.injectedZoneId);



    }

    @Test
    public void testNested() {
        assertNotNull(ctx.getBean(OuterBean.class));
        assertNotNull(ctx.getBean(OuterBean.NestedBean.class));
    }

    @Test
    public void testPrimary() {
        var person = ctx.getBean(PersonBean.class);
        assertEquals(TeacherBean.class, person.getClass());
        var dog = ctx.getBean(DogBean.class);
        assertEquals("Husky", dog.type);
    }

    @Test
    public void testSub() {
        assertNotNull(ctx.getBean(Sub1Bean.class));
        assertNotNull(ctx.getBean(Sub2Bean.class));
        assertNotNull(ctx.getBean(Sub3Bean.class));
    }


    PropertyResolver createPropertyResolver() {
        var ps = new Properties();
        ps.put("app.title", "Scan App");
        ps.put("app.version", "v1.0");
        ps.put("jdbc.url", "jdbc:hsqldb:file:testdb.tmp");
        ps.put("jdbc.username", "sa");
        ps.put("jdbc.password", "");
        ps.put("convert.boolean", "true");
        ps.put("convert.byte", "123");
        ps.put("convert.short", "12345");
        ps.put("convert.integer", "1234567");
        ps.put("convert.long", "123456789000");
        ps.put("convert.float", "12345.6789");
        ps.put("convert.double", "123456789.87654321");
        ps.put("convert.localdate", "2025-12-06");
        ps.put("convert.localtime", "11:25:30");
        ps.put("convert.localdatetime", "2025-12-06T11:25:30");
        ps.put("convert.zoneddatetime", "2025-12-06T11:25:30+08:00[Asia/Shanghai]");
        ps.put("convert.duration", "P2DT3H4M");
        ps.put("convert.zoneid", "Asia/Shanghai");
        var pr = new PropertyResolver(ps);
        return pr;



    }
}
