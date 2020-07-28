/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserAgentUtil
 * Author:   Chenfg
 * Date:     2020/5/20 0020 11:08
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali.etl.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 */
public class UserAgentUtil {
    private static final Logger logger = Logger.getLogger(UserAgentUtil.class);
    static UASparser uaSparser = null;
    //初始化代码块
    static {
        try {
            uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            logger.error("获取uaSparser异常",e);
        }
    }

    /**
     * 解析userAgent，生成浏览器、操作系统信息
     * @param userAgent
     * @return
     */
    public static UserAgentInfo parserUserAgent(String userAgent){
        UserAgentInfo info = new UserAgentInfo();

        //1、检查传入参数
        if(StringUtils.isNotEmpty(userAgent)){
            try {
                cz.mallat.uasparser.UserAgentInfo userInfo = uaSparser.parse(userAgent);

                if(userInfo != null){
                    //将UserAgentInfo中的值取出来赋值给info
                    info.setBrowserName(userInfo.getUaFamily());
                    info.setBrowserVersion(userInfo.getBrowserVersionInfo());
                    info.setOSName(userInfo.getOsCompany());
                    info.setOSVersion(userInfo.getOsName());
                }
            } catch (IOException e) {
                logger.error("解析userAgent异常",e);
            }
        }

        return info;
    }

    /**
     * 封装解析后的用户代理信息
     */
    public static class UserAgentInfo{
        private String browserName = "";
        private String browserVersion = "";
        private String OSName = "";
        private String OSVersion = "";

        public UserAgentInfo() {
        }

        public UserAgentInfo(String browserName, String browserVersion, String OSName, String OSVersion) {
            this.browserName = browserName;
            this.browserVersion = browserVersion;
            this.OSName = OSName;
            this.OSVersion = OSVersion;
        }

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getOSName() {
            return OSName;
        }

        public void setOSName(String OSName) {
            this.OSName = OSName;
        }

        public String getOSVersion() {
            return OSVersion;
        }

        public void setOSVersion(String OSVersion) {
            this.OSVersion = OSVersion;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", OSName='" + OSName + '\'' +
                    ", OSVersion='" + OSVersion + '\'' +
                    '}';
        }
    }
}
