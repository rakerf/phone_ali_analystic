/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: LogUtil
 * Author:   Chenfg
 * Date:     2020/5/20 0020 11:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali.etl.util;

import com.phone.ali.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 * 日志解析的工具类
 * 将所有解析日志的操作封装到该类
 *
 */
public class LogUtil {
    private static final Logger logger = Logger.getLogger(LogUtil.class);

    public static Map<String,String> parserLog(String log){
        Map<String,String> map = new ConcurrentHashMap<String,String>();

        //判断参数是否正确
        if(StringUtils.isNotEmpty(log)){
            String[] fields = log.split("\\^A");

            if(fields.length == 4){
                //存储到map
                map.put(Constants.LOG_IP,fields[0]);
                map.put(Constants.LOG_SERVER_TIME,fields[1].replaceAll("\\.",""));

                //参数列表，单独处理
                String params = fields[3];
                handleParams(params,map);

                //处理ip
                handleIP(map);
                //处理userAgent
                handleUserAgent(map);
            }
        }
        return map;
    }

    /**
     * 将map中的b_iev的值解析成浏览器和操作系统信息，然后存储到map中
     * @param map
     */
    private static void handleUserAgent(Map<String, String> map) {
        if(map.containsKey(Constants.LOG_USERAGENT)){
            UserAgentUtil.UserAgentInfo info = UserAgentUtil.parserUserAgent(map.get(Constants.LOG_USERAGENT));

            //将解析的结果存储到map中
            map.put(Constants.LOG_BROWSER_NAME,info.getBrowserName());
            map.put(Constants.LOG_BROWSER_VERSION,info.getBrowserVersion());
            map.put(Constants.LOG_OS_NAME,info.getOSName());
            map.put(Constants.LOG_OS_VERSION,info.getOSVersion());
        }
    }

    /**
     * 将map中的ip地址转换成国家省市，然后存储到map中
     * @param map
     */
    private static void handleIP(Map<String, String> map) {
        if(map.containsKey(Constants.LOG_IP)){
            IPUtil.RegionInfo info = IPUtil.getRegionInfoByIP(map.get(Constants.LOG_IP));

            //将解析 结果存储到map中
            map.put(Constants.LOG_COUNTRY,info.getCountry());
            map.put(Constants.LOG_PROVINCE,info.getProvince());
            map.put(Constants.LOG_CITY,info.getCity());
        }
    }

    /**
     * 处理参数列表
     * 将url所带的用户自定义数据分拆出来
     * @param params
     * @param map
     */
    private static void handleParams(String params, Map<String, String> map) {
        try {
            if(StringUtils.isNotEmpty(params)){
                int index = params.indexOf("?");
                if(index > 0){
                    String[] fields = params.substring(index + 1).split("&");
                    if(fields.length > 0){
                        for (String field :fields) {
                            String kvs[] = field.split("=");

                            String k = kvs[0];
                            String v = URLDecoder.decode(kvs[1],"utf-8");

                            //判断key是否为空
                            if(StringUtils.isNotEmpty(k)){
                                //存储到map中
                                map.put(k,v);
                            }
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("value进行urldecode解码异常。",e);
        }
    }
}
