package cn.alphabets.light.log;

import android.content.Context;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by 罗浩 on 14/11/14.
 */
public class LogUtil {
    static LogConfigurator logConfigurator;

    public static void init(Context ctx) {
        logConfigurator = new LogConfigurator();
        File logDir = new File(ctx.getExternalFilesDir(null) + File.separator + "logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        String logFileName = logDir + File.separator + getApplicationName(ctx) + ".log";
        logConfigurator.setFileName(logFileName);
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setFilePattern("[%-5p]%d - [%t][%l] - %m%n");
        logConfigurator.configure();
    }

    public static String getLogDir() {
        return new File(logConfigurator.getFileName()).getParent().toString();
    }

    private static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }
}
