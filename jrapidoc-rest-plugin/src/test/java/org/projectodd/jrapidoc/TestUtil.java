package org.projectodd.jrapidoc;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas "sarzwest" Jiricek on 29.3.15.
 */
public class TestUtil {

    @Test
    public void testTrimSlash(){
        String url = "/qwe/rty/";
        url = RestUtil.trimSlash(url);
        Assert.assertEquals("qwe/rty", url);
        url = "/";
        url = RestUtil.trimSlash(url);
        Assert.assertEquals("", url);
    }
}
