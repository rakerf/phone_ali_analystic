/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: EtlToHdfsMapper
 * Author:   Chenfg
 * Date:     2020/5/20 0020 11:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali.etl.mr;

import com.phone.ali.common.Constants;
import com.phone.ali.etl.util.LogUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 */
public class EtlToHdfsMapper extends Mapper<LongWritable, Text,LogWritable, NullWritable> {
    private static final Logger logger = Logger.getLogger(EtlToHdfsMapper.class);
    private static LogWritable k = new LogWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        //全局计数
        context.getCounter("DataNums","AllRecords").increment(1);

        //空行处理
        if(StringUtils.isEmpty(line)){
            //全局计数
            context.getCounter("DataNums","failRecords").increment(1);
            return ;
        }

        //调用logUtil的pasrserLog方法，返回一个map，然后循环map，将map的数据转存到LogWriable
        Map<String, String> map = LogUtil.parserLog(line);

        if(map.size() == 0 ){
            context.getCounter("DataNums","failRecords").increment(1);
            return ;
        }

        //按照事件类型将数据分别输出
        //获取事件名
        String eventName = map.get(Constants.LOG_EVENT_NAME);
        Constants.EventEnum event = Constants.EventEnum.valueOfAlias(eventName);

        LogWritable log = null;
        switch (event){
            case LANUCH:
//                log = handleLaunch(map);
//                break;
            case PAGEVIEW:
            case EVENT:
            case CHARGEREQUEST:
            case CHARGESUCCESS:
            case CHARGEREFUND:
                handleLog(map,context);
                break;
            default:
                break;
        }
//        context.write(log, NullWritable.get());
    }

    private void handleLog(Map<String, String> map, Context context) {
        //循环map
        for (Map.Entry<String,String> en :map.entrySet()) {
            switch (en.getKey()){
                case "ver" : k.setVer(en.getValue());break;
                case "s_time" : k.setS_time(en.getValue());break;
                case "en" : k.setEn(en.getValue());break;
                case "u_ud" : k.setU_ud(en.getValue());break;
                case "u_mid" : k.setU_mid(en.getValue());break;
                case "u_sd" : k.setU_sd(en.getValue());break;
                case "c_time" : k.setC_time(en.getValue());break;
                case "l" : k.setL(en.getValue());break;
                case "b_iev" : k.setB_iev(en.getValue());break;
                case "b_rst" : k.setB_rst(en.getValue());break;
                case "p_url" : k.setP_url(en.getValue());break;
                case "p_ref" : k.setP_ref(en.getValue());break;
                case "tt" : k.setTt(en.getValue());break;
                case "pl" : k.setPl(en.getValue());break;
                case "ip" : k.setIp(en.getValue());break;
                case "oid" : k.setOid(en.getValue());break;
                case "on" : k.setOn(en.getValue());break;
                case "cua" : k.setCua(en.getValue());break;
                case "cut" : k.setCut(en.getValue());break;
                case "pt" : k.setPt(en.getValue());break;
                case "ca" : k.setCa(en.getValue());break;
                case "ac" : k.setAc(en.getValue());break;
                case "kv_" : k.setKv_(en.getValue());break;
                case "du" : k.setDu(en.getValue());break;
                case "browserName" : k.setBrowserName(en.getValue());break;
                case "browserVersion" : k.setBrowserVersion(en.getValue());break;
                case "osName" : k.setOsName(en.getValue());break;
                case "osVersion" : k.setOsVersion(en.getValue());break;
                case "country" : k.setCountry(en.getValue());break;
                case "province" : k.setProvince(en.getValue());break;
                case "city" : k.setCity(en.getValue());break;
            }
        }

        try {
            context.write(k,NullWritable.get());
            //全局计数
            context.getCounter("DataNums","SuccessRecords").increment(1);
        } catch (IOException e) {
            context.getCounter("DataNums","failRecords").increment(1);
            logger.error("etl的map输出异常",e);
        } catch (InterruptedException e) {
            context.getCounter("DataNums","failRecords").increment(1);
            logger.error("etl的map输出异常",e);
        }
    }
}
