package cn.alphabets.light.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import cn.alphabets.light.network.ContextManager;

/**
 *
 * Created by shen on 15/1/22.
 */
public class Eternal {

    /**
     * 获取保存在ObbDir中的uid
     * @param file 保存的文件名
     * @return
     */
    public static String getString(String file) {

        File obbDir = ContextManager.getInstance().getObbDir();
        if (!obbDir.exists()) {
            return "";
        }

        File dataFile = new File(obbDir.getPath() + File.separator + file);
        if (!dataFile.isFile()) {
            return "";
        }

        try {
            FileInputStream inputStream = new FileInputStream(dataFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            StringBuffer buffer = new StringBuffer();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            reader.close();
            inputStream.close();
            return buffer.toString();
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * 保存uid
     * @param file
     * @param value
     */
    public static void saveString(String file, String value) {

        File obbDir = ContextManager.getInstance().getObbDir();
        if (!obbDir.exists()) {
            obbDir.mkdirs();
        }

        try {
            File dataFile = new File(obbDir.getPath() + File.separator + file);
            if (!dataFile.isFile()) {
                dataFile.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(dataFile);
            outputStream.write(value.getBytes());
            outputStream.close();
        } catch (Exception e) {
        }
    }
}
