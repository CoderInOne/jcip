package xunshan.jcip.util;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ReflectionUtilsTest {

    @Test
    public void getFieldByName() {
        String foo = "foo";
        assertNotNull(ReflectionUtils.getFieldByName(foo, "value"));
    }

    @Test(expected = ReflectionException.class)
    public void getNoSuchField() {
        String foo = "foo";
        ReflectionUtils.getFieldByName(foo, "value1");
    }
}