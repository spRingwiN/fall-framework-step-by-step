package com.eric.fall.helloboot;

import com.eric.fall.boot.FallApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) throws Exception {
        // 判断是否从jar/war启动
        String jarFile = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        boolean isJarFile = jarFile.endsWith(".jar") || jarFile.endsWith(".war");
        // 定位 webapp根目录
        String webDir = isJarFile ? "tmp-webapp" : "hello-boot/src/main/webapp";
        if (isJarFile) {
            // 解压到tem-webapp
            Path baseDir = Paths.get(webDir).normalize().toAbsolutePath();
            if (Files.isDirectory(baseDir)) {
                Files.delete(baseDir);
            }
            Files.createDirectories(baseDir);
            System.out.println("extract to: " + baseDir);
            try (JarFile jar = new JarFile(jarFile)) {
                // 获取所有条目的文件流，包括嵌套目录下的文件
                List<JarEntry> entries = jar.stream().sorted(Comparator.comparing(JarEntry::getName)).toList();
                for (JarEntry entry : entries) {
                    Path res = baseDir.resolve(entry.getName());
                    if (!entry.isDirectory()) {
                        System.out.println(res);
                        Files.createDirectories(res.getParent());
                        Files.copy(jar.getInputStream(entry), res);
                    }
                }
            }
            // JVM 退出时自动删除tmp-webapp：
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    // Comparator.reverseOrder()  逆序排序，确保从最深层开始删除，这样可以确保先删除子文件和子目录，最后再删除父目录，避免因目录非空而无法删除的问题
                    Files.walk(baseDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File:: delete);
                } catch (Exception ignore) {

                }
            }));
        }
        FallApplication.run(webDir, isJarFile ? "tmp-webapp" : "hello-boot/target/classes", HelloConfiguration.class, args);
    }

}
