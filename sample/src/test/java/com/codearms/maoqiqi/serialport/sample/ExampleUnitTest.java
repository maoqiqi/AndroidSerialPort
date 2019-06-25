package com.codearms.maoqiqi.serialport.sample;

import com.codearms.maoqiqi.serialport.utils.StringUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        String data = "我是测试数据123ABC(edf)";
        String hexStr = StringUtils.str2HexStr(data);
        // e68891e698afe6b58be8af95e695b0e68dae3132334142432865646629
        System.out.println(hexStr);
        String str = StringUtils.hexStr2Str(hexStr);
        System.out.println(str);
    }
}