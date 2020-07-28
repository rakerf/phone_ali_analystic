/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: EtlToHdfsRunner
 * Author:   Chenfg
 * Date:     2020/5/20 0020 16:25
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.phone.ali.etl.mr;

import com.phone.ali.common.GlobalConstants;
import com.phone.ali.etl.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

/**
 *
 * @author Chenfg
 * @create 2020/5/20 0020
 * @since 1.0.0
 */
public class EtlToHdfsRunner {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1、
        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS","hdfs://hadoop0001:9000");
//        conf.set("mapreduce.framework.name","yarn");

        //1、获取日期
        //-d 之后的一个参数就是日期参数
        //-d 2020-05-20 -n 3
        handleArgs(conf,args);

        //获取Job
        Job job =Job.getInstance(conf,"EtlToHdfs");

        //设置程序的执行路径
        job.setJarByClass(EtlToHdfsRunner.class);

        //设置mapper类
        job.setMapperClass(EtlToHdfsMapper.class);
        job.setOutputKeyClass(LogWritable.class);
        job.setOutputValueClass(NullWritable.class);

        //设置reduce数量为0
        job.setNumReduceTasks(0);

        //设置输入输出
        handleInputOutput(job);

        System.exit(job.waitForCompletion(true)?0:1);
    }

    private static void handleInputOutput(Job job) throws IOException {
        //获取执行日期
        String date = job.getConfiguration().get(GlobalConstants.RUNNING_DATE);

        Path inPath = new Path("/logs/"+date);
        Path outPath = new Path("/ods/" +date);

        //获取文件系统操作客户端
        FileSystem fs = FileSystem.get(job.getConfiguration());

        //判断输入路径是否存在
        if(fs.exists(inPath)){
            FileInputFormat.setInputPaths(job,inPath);
        }else{
            throw new RuntimeException("输入路径不存在。"+inPath.toString());
        }

        //设置输出
        if(fs.exists(outPath)){
            fs.delete(outPath,true);
        }

        FileOutputFormat.setOutputPath(job,outPath);
    }

    //处理日期参数
    //如果有传入日期，则直接使用传入参数
    //否则，默认获取昨天的日期
    private static void handleArgs(Configuration conf, String[] args) {
        String date = null;
        if (args.length > 0){
            //循环参数
            for (int i = 0; i < args.length; i++) {
                //判断参数中是否出现-d
                if(args[i].equals("-d")){
                    if(i+1 <= args.length){
                        date =args[i+1];
                        break;
                    }
                }
            }
        }

        //判断是否取得date
        if(StringUtils.isEmpty( date)){
            //获取昨天的日期
            date = TimeUtil.getYesterday();
        }

        //将date存储到conf
        conf.set(GlobalConstants.RUNNING_DATE,date);
    }


}
