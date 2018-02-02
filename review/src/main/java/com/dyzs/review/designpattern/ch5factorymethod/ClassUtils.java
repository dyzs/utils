package com.dyzs.review.designpattern.ch5factorymethod;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author maidou, created on 2018/2/2.
 */
@SuppressWarnings("all")
public class ClassUtils {
    //给一个接口，返回这个接口的所有实现类
    public static List<Class> getAllClassByInterface (Class c) {
        List<Class> returnList = new ArrayList<>();

        //如果不是一个接口，则不做处理
        if (c.isInterface()) {
            // 获取当前包名
            String packageName = c.getPackage().getName();

            try {
                // 获取当前包名下以及子包的所有类
                List<Class> allClass = getClasses(packageName);
                //判断是否是同一个接口
                for (int i= 0; i < allClass.size(); i++){
                    if (c.isAssignableFrom(allClass.get(i))) { //判断是不是一个接口
                        if (!c.equals(allClass.get(i))) { //本身不加进去
                            returnList.add(allClass.get(i));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

    /**
     * 从一个包中查找出所有的类，在jar包中不能查找
     */
    public static List<Class> getClasses (String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
