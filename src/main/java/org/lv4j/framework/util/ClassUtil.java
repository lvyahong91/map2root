package org.lv4j.framework.util;



import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 */
public class ClassUtil {
    /*1 获取类加载器*/
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /*2 加载类*/
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls= Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            //记录进log
            throw new RuntimeException(e);
        }
        return cls;
    }

    /*3 获取指定包名下的所有类*/
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls=getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url=urls.nextElement();
                if (url !=null){
                    String protocol=url.getProtocol();
                    if (protocol.equals("file")){  //如果是文件
                        String packagePath=url.getPath().replaceAll("%20","");
                        addClass(classSet,packagePath,packageName);  //获得类的全限定名
                    }else if (protocol.equals("jar")){ //如果是jar包
                        JarURLConnection jarURLConnection= (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null){
                            JarFile jarFile=jarURLConnection.getJarFile(); //取得jar文件
                            if (jarFile != null){
                                Enumeration<JarEntry> jarEntries=jarFile.entries();
                                while (jarEntries.hasMoreElements()){
                                    JarEntry jarEntry=jarEntries.nextElement();
                                    String jarEntryName=jarEntry.getName();  //取得jar里的文件名
                                    if (jarEntryName.endsWith(".class")){  //取得全限定名
                                        String className=jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 辅助方法1 获取类的全限定名
     * @param classSet
     * @param packagePath
     * @param packageName
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files=new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });

        for (File file:files){  //1 是文件的情况
            String fileName=file.getName();
            if (file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtil.isNotEmpty(packageName)){
                    className=packageName+"."+className;  //全限定名，即绝对路径
                }
                doAddClass(classSet,className);
            }else {  //2 不是文件的情况
                String subPackagePath=fileName;
                if (StringUtil.isNotEmpty(packagePath)){
                    subPackagePath=packagePath+"/"+subPackagePath;  //不是文件的情况下，路径用盘符分割
                }
                String subPackageName=fileName;

                if (StringUtil.isNotEmpty(subPackageName)) {
                    subPackageName=packageName+"."+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);  //自调用，此时路径下是文件的情况
            }
        }
    }

    /**
     *  辅助方法2 通过类的全限定名，得到类的实例，并将其放在Set集合
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls=loadClass(className,false);
        classSet.add(cls);
    }
}
