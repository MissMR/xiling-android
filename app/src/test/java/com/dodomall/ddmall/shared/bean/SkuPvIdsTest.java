package com.dodomall.ddmall.shared.bean;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * created by Jigsaw at 2019/3/12
 */
public class SkuPvIdsTest {

    @Test
    public void isMatch() {
        String str = "111,222,333";
        String str2 = "111,333";
        List<String> list = Collections.singletonList(str);
        System.out.println(list.toString());
        System.out.println(list.toArray());
        System.out.println(list.size());

        boolean result = Collections.disjoint(Arrays.asList(1, 2), Arrays.asList(3));
        System.out.println("result:" + result);
    }
}