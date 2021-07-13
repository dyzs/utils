package com.dyzs.calllogwirter;

import android.support.annotation.Nullable;
import android.text.TextUtils;


public class CallRecord implements Comparable<CallRecord>{
    public String ring_times;

    private long id;
    private String name;
    private String phone;
    private String identityName;
    private long date;
    private long duration;
    private String location;
    private String subscriptionId;
    ///** Call log type for incoming calls. */
    //        public static final int INCOMING_TYPE = 1;
    //        /** Call log type for outgoing calls. */
    //        public static final int OUTGOING_TYPE = 2;
    //        /** Call log type for missed calls. */
    //        public static final int MISSED_TYPE = 3;
    //        /** Call log type for voicemails. */
    //        public static final int VOICEMAIL_TYPE = 4;
    //        /** Call log type for calls rejected by direct user action. */
    //        public static final int REJECTED_TYPE = 5;
    //        /** Call log type for calls blocked automatically. */
    //        public static final int BLOCKED_TYPE = 6;
    //        OPPO reno 机型设有单独拦截列表，call log type 值为 27 或 -5
    private int type;
    private long lastModify;
    private String identifyId;

    private boolean socialShowName;

    private String reportId;

    private String simpleDate;

    private boolean blackList;
    public void setInBlackList(boolean inBlackList){
        this.blackList=inBlackList;
    }

    public boolean isBlackList(){
        return blackList;
    }

    public CallRecord(long id, String name, String phone, long date, long duration, String location, int type, long lastModify, String subId){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.date=date;
        this.duration=duration;
        this.location=location;
        this.type=type;
        this.lastModify=lastModify;
        this.subscriptionId=subId;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof CallRecord){
            return ((CallRecord) obj).id==id;
        }
        return false;
    }


    public String getSimpleDate(){
        return "";
    }

    @Override
    public int compareTo(CallRecord o) {
        if(date==o.date) return 0;
        return o.date-date>0?1:-1;
    }

    public String getSafeName(){
        if(TextUtils.isEmpty(name)) return "";
        return name;
    }

    public String getSafeIdentify(){
        if(TextUtils.isEmpty(identifyId)) return "";
        return identifyId;
    }

    public void setLastModify(long lastModify){
        this.lastModify=lastModify;
    }

    public long getLastModify(){
        return lastModify;
    }

    public String getSubscriptionId(){
        return subscriptionId;
    }

    public void setSocialShowName(boolean showName){
        this.socialShowName=showName;
    }

    public boolean isSocialShowName(){
        return socialShowName;
    }

    public void setReportId(String reportId){
        this.reportId=reportId;
    }

    public String getReportId(){
        return reportId;
    }

    public boolean shouldSyncData(){
        return !TextUtils.isEmpty(getIdentifyId()) || isBlackList()|| isSocialShowName() || !TextUtils.isEmpty(reportId);
    }


    @Override
    public String toString() {
        return "CallRecord{" +
                "id=" + id +
                ", dateStr='" + CommonUtils.getTimeYMDHMS(date) + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", identityName='" + identityName + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", type=" + type +
                ", lastModify=" + lastModify +
                ", identifyId='" + identifyId + '\'' +
                ", socialShowName=" + socialShowName +
                ", reportId='" + reportId + '\'' +
                ", simpleDate='" + simpleDate + '\'' +
                ", blackList=" + blackList +
                ", ring_times=" + ring_times +
                '}';
    }


    public String toStringV2() {
        return "{" +
                "dateStr='" + CommonUtils.getTimeYMDHMS(date) + '\'' +
                ", phone='" + phone + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", type=" + type +
                ", ring_times=" + ring_times +
                '}';
    }
}
