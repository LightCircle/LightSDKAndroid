package cn.alphabets.light.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by 罗浩 on 14/11/14.
 */
public class ZipUtil {
    private static int BUFFER_SIZE = 8192;

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    /**
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile  Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws IOException
     */
    public static void unzip(String zipFile, String location) throws IOException {
        int size;
        byte[] buffer = new byte[BUFFER_SIZE];


        if (!location.endsWith("/")) {
            location += "/";
        }
        File f = new File(location);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
        try {
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                String path = location + ze.getName();
                File unzipFile = new File(path);

                if (ze.isDirectory()) {
                    if (!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                } else {
                    // check for and create parent directories if they don't exist
                    File parentDir = unzipFile.getParentFile();
                    if (null != parentDir) {
                        if (!parentDir.isDirectory()) {
                            parentDir.mkdirs();
                        }
                    }

                    // unzip the file
                    FileOutputStream out = new FileOutputStream(unzipFile, false);
                    BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                    try {
                        while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            fout.write(buffer, 0, size);
                        }

                        zin.closeEntry();
                    } finally {
                        fout.flush();
                        fout.close();
                    }
                }
            }
        } finally {
            zin.close();
        }

    }
}
