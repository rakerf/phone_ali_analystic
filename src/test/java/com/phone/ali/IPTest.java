/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: IPTest
 * Author:   Chenfg
 * Date:     2020/5/20 0020 9:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali;

import com.phone.ali.etl.util.IPSeeker;
import com.phone.ali.etl.util.IPUtil;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 */
public class IPTest {
    public static void main(String[] args) throws Exception {
//        System.out.println(IPSeeker.getInstance().getCountry("112.244.34.184"));

//        System.out.println(IPUtil.getRegionInfoByIP("112.244.34.184"));

        System.out.println(IPUtil.parserIp1("http://ip.taobao.com/outGetIpInfo?ip=121.234.113.135&accessKey=alibaba-inc","utf-8"));
    }
}
