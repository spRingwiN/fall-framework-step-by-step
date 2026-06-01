package com.eric.fall.app.context;

import com.eric.definitions.context.AnnotationConfigApplication;
import com.eric.definitions.context.BeanDefinition;
import com.eric.definitions.io.PropertyResolver;
import com.eric.imported.LocalDateConfiguration;
import com.eric.imported.ZonedDateConfiguration;
import com.eric.scan.ScanApplication;
import com.eric.scan.custom.annotation.CustomAnnotationBean;
import com.eric.scan.nested.OuterBean;
import com.eric.scan.primary.PersonBean;
import com.eric.scan.primary.StudentBean;
import com.eric.scan.primary.TeacherBean;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AnnotationConfigApplicationTest {

    @Test
    public void testAnnotationConfigApplication() {
        var ctx = new AnnotationConfigApplication(ScanApplication.class, createPropertyResolver());
        // @CustomAnnotation
        assertNotNull(ctx.findBeanDefinition(CustomAnnotationBean.class));
        assertNotNull(ctx.findBeanDefinition("customAnnotation"));

        // @Import
        assertNotNull(ctx.findBeanDefinition(LocalDateConfiguration.class));
        assertNotNull(ctx.findBeanDefinition("startLocalDate"));
        assertNotNull(ctx.findBeanDefinition("startLocalDateTime"));
        assertNotNull(ZonedDateConfiguration.class);
        assertNotNull(ctx.findBeanDefinition("startZoneDateTime"));

        // nested:
        assertNotNull(ctx.findBeanDefinition(OuterBean.class));
        assertNotNull(ctx.findBeanDefinition(OuterBean.NestedBean.class));

        BeanDefinition studentDef = ctx.findBeanDefinition(StudentBean.class);
        BeanDefinition teacherDef = ctx.findBeanDefinition(TeacherBean.class);
        // 2 PersonBean
        List<BeanDefinition> defs = ctx.findBeanDefinitions(PersonBean.class);
        assertSame(studentDef, defs.get(0));
        assertSame(teacherDef, defs.get(1));
        // 1 @Primary PersonBean
        BeanDefinition personPrimaryDef = ctx.findBeanDefinition(PersonBean.class);
        assertSame(teacherDef, personPrimaryDef);
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
        ps.put("convert.int", "1234567");
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
