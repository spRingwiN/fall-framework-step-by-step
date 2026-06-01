package com.eric.fall.app.io;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.sub.AnnoScan;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceResolverTest {

    @Test
    public void scanClass() {
        var pkg = "com.eric.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6)
                        .replace("/", ".").replace("\\", ".");
            }
            return null;
        });
        Collections.sort(classes);
        System.out.println(classes);
        String[] listClasses = new String[] {
                "com.eric.scan.convert.ValueConverterBean",
                "com.eric.scan.destroy.AnnotationDestroyBean",
                "com.eric.scan.init.SpecifyInitBean",
                "com.eric.scan.proxy.OriginBean",
                "com.eric.scan.proxy.FirstProxyBeanPostProcessor",
                "com.eric.scan.proxy.SecondProxyPostProcessor",
                "com.eric.scan.nested.OuterBean",
                "com.eric.scan.sub1.Sub1Bean",
                "com.eric.scan.sub1.sub2.Sub2Bean",
                "com.eric.scan.sub1.sub2.sub3.Sub3Bean",
                "com.eric.scan.sub1.sub2.sub3.Sub3NonePublicClass",
                "com.eric.scan.sub1.sub2.sub3.Sub3Bean$Sub3InnerClass",
                "com.eric.scan.sub1.sub2.sub3.Sub3Bean$Sub3StaticClass",
        };
        for (String clazz : listClasses) {
            assertTrue(classes.contains(clazz));
        }
    }


    @Test
    public void scanJar() {
        var pkg = PostConstruct.class.getPackageName();
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace("/", ".").replace("\\", ".");

            }
            return null;
        });
        // classes in jar
        System.out.println(PostConstruct.class.getName());
        assertTrue(classes.contains(PostConstruct.class.getName()));
        // jakarta.annotation.sub.AnnoScan is defined in classes:
        assertTrue(classes.contains(AnnoScan.class.getName()));
    }

    @Test
    public void scanTxt() {
        var pkg = "com.eric.scan";
        var rr = new ResourceResolver(pkg);
        List<String> classes = rr.scan(res -> {
            String name = res.name();
            if (name.endsWith(".txt")) {
                return name.replace("\\", "/");
            }
            return null;
        });
        Collections.sort(classes);
        assertArrayEquals(new String[] {
                "com/eric/scan/sub1/sub1.txt",
                "com/eric/scan/sub1/sub2/sub2.txt",
                "com/eric/scan/sub1/sub2/sub3/sub3.txt",
                },
                classes.toArray(String[]::new));
    }




















}
