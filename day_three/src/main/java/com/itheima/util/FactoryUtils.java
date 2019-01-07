package com.itheima.util;

import java.util.ResourceBundle;

public class FactoryUtils {
    //因为这个工厂会有很多很多个类一起做,所以要将初始化工作拿出来
    private static ResourceBundle impl = null;
    static{
        impl = ResourceBundle.getBundle("impl");
        System.out.println(impl);
    }


    public static Object getInstance(String interfaceName) {
        String implClassName = impl.getString(interfaceName);
        try {
            return Class.forName(implClassName).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
