package com.example.pay.utils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface BookParser
{
    /**
     * 解析输入流 得到Book对象集合
     * @param is
     * @return
     * @throws Exception
     */
    Map<String, String> parse(InputStream is) throws Exception;

    /**
     * 序列化Book对象集合 得到XML形式的字符串
     * @param books
     * @return
     * @throws Exception
     */
    String serialize(List<String> books) throws Exception;
}