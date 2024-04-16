package edu.rpi.legup.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LegupUtils {

    private static final Logger LOGGER = Logger.getLogger(LegupUtils.class.getName());

    /**
     * Scans all classes accessible from the context class loader which belong to the given package
     * and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException if class is not in package
     * @throws IOException if file is not found
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');

        URL url = LegupUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        if (jarPath.contains(".jar")) {
            List<Class> css = findClassesZip(jarPath, path);
            return css.toArray(new Class[css.size()]);
        }

        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else {
                if (file.getName().endsWith(".class")) {
                    classes.add(
                            Class.forName(
                                    packageName
                                            + '.'
                                            + file.getName()
                                                    .substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    private static List<Class> findClassesZip(String path, String packageName)
            throws IOException, ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory()
                    && entry.getName().endsWith(".class")
                    && entry.getName().startsWith(packageName)) {
                String className = entry.getName().replace('/', '.');
                classes.add(
                        Class.forName(
                                className.substring(0, className.length() - ".class".length())));
            }
        }
        return classes;
    }
}
