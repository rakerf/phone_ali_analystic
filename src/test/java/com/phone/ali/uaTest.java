/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: uaTest
 * Author:   Chenfg
 * Date:     2020/5/20 0020 11:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali;

import com.phone.ali.etl.util.UserAgentUtil;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 */
public class uaTest {
    public static void main(String[] args) {
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36"));
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3314.0 Safari/537.36 SE 2.X MetaSr 1.0"));
    }
}
