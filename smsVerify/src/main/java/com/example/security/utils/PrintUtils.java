package com.example.security.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class PrintUtils {
    public static void showRequestParmas(HttpServletRequest request){
        Map<String, String[]> map = new HashMap<String, String[]>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            map.put(paramName, paramValues);
        }
        String requestString = JSON.toJSONString(map);
        System.out.println("======================");
        System.out.println("request: "+requestString);
        System.out.println("======================");
    }

    public static void showReponseParmas(HttpServletResponse response){
        Map<String, String> map = new HashMap<String, String>();
        Collection paramNames = response.getHeaderNames();
        Iterator iterator = paramNames.iterator();
        while (iterator.hasNext()){
            String paramName = (String) iterator.next();
            String paramValues = response.getHeader(paramName);
            map.put(paramName, paramValues);
        }
        String requestString = JSON.toJSONString(map);
        System.out.println("======================");
        System.out.println("request: "+requestString);
        System.out.println("======================");
    }
}
