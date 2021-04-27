package com.dyzs.udppaser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jdk.nashorn.internal.parser.Token;

/**
 * 测试用 udp 发送文件数据合并
 *
 * token开始时间戳，挂断时间戳，拨号和挂断时间间隔
 */
public class UdpLogCombine {
    ArrayList<TokenLog> listLogs;

    public UdpLogCombine() {
        try {
            File logFile = new File("C:\\Users\\DELL\\Desktop\\TokenLog_sendUdp_2021-04-23-20-54-47.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "UTF-8"));
            String line;
            TokenLog tokenLog = new TokenLog();
            listLogs = new ArrayList<>();
            while((line = reader.readLine()) != null) {
                if (line.contains("===================")) {
                    continue;
                }
                if (line.contains("Token 开始时间")) {
                    tokenLog = new TokenLog();
                    tokenLog.tokenStartTime = line;
                }
                if (line.contains("Token 结束时间与结果")) {
                    tokenLog.tokenEndTime = line;
                }
                if (line.contains("拨号时间")) {
                    tokenLog.startCallTime = line;
                }
                if (line.contains("接通时间")) {
                    tokenLog.callOffHookTime = line;
                }
                if (line.contains("挂断时间")) {
                    tokenLog.endCallTime = line;
                    listLogs.add(tokenLog);
                }
            }
            reader.close();
            /* 计算时间 */
            for (TokenLog log : listLogs) {
                log.timeStrTurnToLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨号和接通时间间隔
     */
    public void writeCallConsumeTime() {
        String fileName = "call_consume_";
        ArrayList<String> list = new ArrayList<>();
        for (TokenLog log : listLogs) {
            list.add("" + log.callConsumeTime);
        }
        FileUtils.writeLog(list, fileName);
    }

    public void writeTokenTimestamp() {
        String fileName = "token_time_";
        ArrayList<String> list = new ArrayList<>();
        for (TokenLog log : listLogs) {
            list.add("TokenStart:" + log.tokenStartTimeLong);
            list.add("CallEnd:" + log.endCallTimeLong);
        }
        FileUtils.writeLog(list, fileName);
    }


    public static class TokenLog {
        public String tokenStartTime;
        public String tokenEndTime;
        public String startCallTime;
        public String callOffHookTime;/* 接通时间 */
        public String endCallTime;

        public long tokenStartTimeLong = 0L;
        public long tokenEndTimeLong = 0L;
        public long startCallTimeLong = 0L;
        public long callOffHookTimeLong = 0L;/* 接通时间 */
        public long endCallTimeLong = 0L;

        public long callConsumeTime = 0L;

        public void timeStrTurnToLong() {
            if (tokenStartTime != null) {
                int is = tokenStartTime.lastIndexOf("[");
                is += 1;
                int es = tokenStartTime.length() - 1;
                String splitStr = tokenStartTime.substring(is, es);
                tokenStartTimeLong = DateUtils.getTimeMillis(splitStr, DateUtils.DATE_FORMAT_PATTERN_S_1);
            }
            if (tokenEndTime != null) {
                int is = tokenEndTime.lastIndexOf("[");
                is += 1;
                int es = tokenEndTime.length() - 1;
                String splitStr = tokenEndTime.substring(is, es);
                tokenEndTimeLong = DateUtils.getTimeMillis(splitStr, DateUtils.DATE_FORMAT_PATTERN_S_1);
            }
            if (startCallTime != null) {
                int is = startCallTime.lastIndexOf("[");
                is += 1;
                int es = startCallTime.length() - 1;
                String splitStr = startCallTime.substring(is, es);
                startCallTimeLong = DateUtils.getTimeMillis(splitStr, DateUtils.DATE_FORMAT_PATTERN_S_1);
            }
            if (callOffHookTime != null) {
                int is = callOffHookTime.lastIndexOf("[");
                is += 1;
                int es = callOffHookTime.length() - 1;
                String splitStr = callOffHookTime.substring(is, es);
                callOffHookTimeLong = DateUtils.getTimeMillis(splitStr, DateUtils.DATE_FORMAT_PATTERN_S_1);
            }
            if (endCallTime != null) {
                int is = endCallTime.lastIndexOf("[");
                is += 1;
                int es = endCallTime.length() - 1;
                String splitStr = endCallTime.substring(is, es);
                endCallTimeLong = DateUtils.getTimeMillis(splitStr, DateUtils.DATE_FORMAT_PATTERN_S_1);
            }
            callConsumeTime = callOffHookTimeLong - startCallTimeLong;
        }
    }

}
