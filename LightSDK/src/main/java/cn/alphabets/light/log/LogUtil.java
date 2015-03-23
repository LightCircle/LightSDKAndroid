package cn.alphabets.light.log;

import android.content.Context;

import org.apache.log4j.Level;

import java.io.File;

import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.util.FileUtil;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 日志收集到文件
 * Created by 罗浩 on 14/11/14.
 */
public class LogUtil {

    private static LogConfigurator configurator;

    /**
     * 初始化文件日志
     */
    public static void init() {

        Context ctx = ContextManager.getInstance();

        configurator = new LogConfigurator();
        File file = new File(FileUtil.getWorkDir() + File.separator + "logs");
        if (!file.exists()) {
            file.mkdirs();
        }

        String logFileName = file + File.separator + getApplicationName(ctx) + ".log";
        configurator.setFileName(logFileName);
        configurator.setRootLevel(Level.DEBUG);
        configurator.setFilePattern("[%-5p]%d - [%t][%l] - %m%n");
        configurator.configure();
    }

    public static String getLogDir() {
        return new File(configurator.getFileName()).getParent().toString();
    }

    private static String getApplicationName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }
}
