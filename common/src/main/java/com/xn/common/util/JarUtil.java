package com.xn.common.util;/**
 * Created by xn056839 on 2016/10/27.
 */

import com.xn.common.service.GetPara;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class JarUtil {
    private static final Logger logger = LoggerFactory.getLogger(JarUtil.class);

    public static URLClassLoader addJar(String jarPath) {
        checkNew(jarPath);
        URLClassLoader loader = null;
        try {
            File jar = new File(jarPath);
            URL[] urls = new URL[]{jar.toURI().toURL()};
            loader = new URLClassLoader(urls);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception("load  jar error");
        } finally {
            return loader;
        }

    }

    public static void checkNew(String jarPath)  {
        GetPara getPara = new GetPara();
        String path = getPara.getPath();
        File profile = new File(path + "suite/jar.properties");
        String checkurl = StringUtil.getPro("jar.properties", "checkurl");
        String repository = StringUtil.getConfig(profile, "repository","snapshots");
        String artifact = StringUtil.getConfig(profile, "artifact","");
        String group = StringUtil.getConfig(profile, "group","");
        String version = StringUtil.getConfig(profile, "version","");

        checkurl = String.format(checkurl, repository, group, artifact, version);


        String newJarName = getNewJarName(checkurl);
        File file = new File(jarPath);
        File[] files = file.listFiles();
        if (files.length == 0) {
            try {
                downloadJar(repository, artifact, group, version, jarPath,newJarName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String oldJar = files[0].getAbsolutePath();
            String oldJarName = oldJar.substring(oldJar.lastIndexOf("/") + 1);
            if (!newJarName.equals("") && !oldJarName.equals(newJarName)) {

                try {
                    downloadJar(repository, artifact,  group, version, jarPath,newJarName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File oldJarFile=new File(oldJar);
                oldJarFile.delete();
            }

        }
    }

    public static void downloadJar(String r, String a, String g, String v, String jarPath,String name) throws Exception {
        String downloadUrl = StringUtil.getPro("jar.properties", "newurl");
        downloadUrl = String.format(downloadUrl, r, g, a, v);
        System.out.println(downloadUrl);
        try {

            File f = new File(jarPath+"/"+name);
            URL httpurl = new URL(downloadUrl);
            FileUtils.copyURLToFile(httpurl, f);
            if (!f.exists()) {
                logger.error("download jar failed!");
                throw new Exception("download jar failed!");
            }

        } catch (Exception e) {

            logger.error("download jar failed!");
            throw new Exception("download jar failed!");
        }

    }

    public static String getNewJarName(String urlString) {

        //获得返回的xml
        URL url = null;
        String jarName = "";
        try {
            url = new URL(urlString);
            URLConnection urlConnection = null;
            urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(500);
            InputStream xmlInputStream = null;

            xmlInputStream = urlConnection.getInputStream();
            byte[] testByteArr = new byte[0];

            testByteArr = new byte[xmlInputStream.available()];

            xmlInputStream.read(testByteArr);

            String content = new String(testByteArr);
            //获得最新的jar的名字
            Document document = null;

            document = DocumentHelper.parseText(content);
            Element root = document.getRootElement();
            Element data = root.element("data");
            String jarPath = data.elementText("repositoryPath");
            jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarName;


    }

    public static void main(String[] args) {
        checkNew("D:/jar");
    }
}
