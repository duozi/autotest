package com.xn.generate;/**
 * Created by xn056839 on 2016/10/24.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZip {
    private static final Logger logger = LoggerFactory.getLogger(FileZip.class);

    public  void zipFile(String input, String output) {
        File f = new File(input);
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(
                    output));
            zip(out, f, null);
            System.out.println("zip done");
            out.close();
        } catch (FileNotFoundException e) {
            logger.error("zip file error");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        System.out.println("zipping " + f.getAbsolutePath());
        if (f.isDirectory()) {
            File[] fc = f.listFiles();
            if (base != null)
                out.putNextEntry(new ZipEntry(base + "/"));
            base = base == null ? "" : base + "/";
            for (int i = 0; i < fc.length; i++) {
                zip(out, fc[i], base + fc[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1)
                out.write(b);
            in.close();
        }
    }
}
