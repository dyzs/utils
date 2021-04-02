package com.dyzs.review.designpattern;

import com.dyzs.review.designpattern.ch4multition.Emperor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.util.Elements;

public class MyMain {
    private static class Test {
        String name;
        String phone;

        Test(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        @Override
        public boolean equals(Object obj) {
            Test t = (Test) obj;
            String str = t.name + t.phone;
            String str2 = name + phone;
            return str.equals(str2);
        }

        @Override
        public int hashCode() {
            return (name + phone).hashCode();
        }
    }
    public static void main(String[] args) throws ClassNotFoundException {
        String phones = "1588";
        String[] phonesArr = phones.split(",");
        int length = phonesArr.length;
        List<String> list = Arrays.asList(phonesArr);
        System.out.print("le" + length);

        Class clz = Class.forName("com.dyzs.review.privateclass.Db");
        try {
            Object p = clz.newInstance();
            Method m = clz.getDeclaredMethod("Test2");
            m.setAccessible(true);
            m.invoke(p);
            m.invoke(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<LoginInfo> arrayList = new ArrayList<>();
        arrayList.add(new LoginInfo("1"));
        arrayList.add(new LoginInfo("2"));
        // 正确转化方式
        LoginInfo[] strArrayTrue = arrayList.toArray(new LoginInfo[arrayList.size()]);
        for (LoginInfo str : strArrayTrue) {
            System.out.println(str);
        }
    }

    public static class LoginInfo {
        public String id;
        public LoginInfo(String id) {
            this.id = id;
        }
    }
}
