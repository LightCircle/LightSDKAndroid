package cn.alphabets.light.util;

import java.util.List;

/**
 * 字符串操作
 * Created by lin on 14/12/6.
 */
public class StringUtil {

    /**
     * 合并字符串
     * @param list
     * @param separator
     * @return
     */
    public static String join(List<String> list, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }
}
