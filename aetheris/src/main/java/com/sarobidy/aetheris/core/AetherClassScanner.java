package com.sarobidy.aetheris.core;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import com.sarobidy.aetheris.annotations.AetherController;
import com.sarobidy.aetheris.exeptions.AetherScanException;


public final class AetherClassScanner {

    private AetherClassScanner() {}

    public static Set<Class<?>> findClassesAnnotatedWith(Class<? extends Annotation> annotationClass,
                                                         String... packages)
            throws AetherScanException {

        Set<Class<?>> result = new HashSet<>();

        try {
            if (packages != null && packages.length > 0) {
                for (String pkg : packages) {
                    result.addAll(scanPackage(pkg.trim(), annotationClass));
                }
            } else {
                result.addAll(findAllClassesAnnotatedWith(annotationClass));
            }

        } catch (IOException e) {
            throw new AetherScanException("Erreur lors du scan du classpath", e);
        }

        return result;
    }


    public static Set<Class<?>> findClasses(String... packages) throws AetherScanException {
        return findClassesAnnotatedWith(AetherController.class, packages);
    }

    public static Set<Class<?>> findAllClasses() throws AetherScanException {
        return findAllClassesAnnotatedWith(AetherController.class);
    }

    private static Set<Class<?>> findAllClassesAnnotatedWith(Class<? extends Annotation> annotationClass)
            throws AetherScanException {
        Set<Class<?>> result = new HashSet<>();

        String classpath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");

        for (String pathEntry : classpath.split(separator)) {
            File dir = new File(pathEntry);
            if (dir.exists() && dir.isDirectory()) {
                result.addAll(scanDirectory(dir, "", annotationClass));
            }
        }

        return result;
    }

    private static Set<Class<?>> scanPackage(String packageName, Class<? extends Annotation> annotationClass)
            throws IOException {
        Set<Class<?>> found = new HashSet<>();
        String path = packageName.replace('.', '/');

        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File dir = new File(resource.getFile());
            if (dir.exists() && dir.isDirectory()) {
                found.addAll(scanDirectory(dir, packageName, annotationClass));
            }
        }
        return found;
    }

    private static Set<Class<?>> scanDirectory(File directory, String packageName,
                                               Class<? extends Annotation> annotationClass) {
        Set<Class<?>> found = new HashSet<>();
        File[] files = directory.listFiles();
        if (files == null) return found;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty()
                        ? file.getName()
                        : packageName + "." + file.getName();
                found.addAll(scanDirectory(file, subPackage, annotationClass));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    if (annotationClass == null || clazz.isAnnotationPresent(annotationClass)) {
                        found.add(clazz);
                    }
                } catch (Throwable ignored) {
                    System.err.println("[AETHERIS][WARN] Classe non chargée : " + className);
                }
            }
        }
        return found;
    }
}
