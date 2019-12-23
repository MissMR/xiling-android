package com.xiling.shared.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bigbyto on 18/07/2017.
 */

public class StringUtils {
    public static boolean isWX(String content) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(content);
        return m.matches();
    }


    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || (c.size() == 0);
    }

}
