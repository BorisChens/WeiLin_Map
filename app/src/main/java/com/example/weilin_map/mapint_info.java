package com.example.weilin_map;

import java.util.Date;

public class mapint_info {
    //dimension为纬度,longtitude为经度
    /*
    public mapint_info(int code, String msg, int rid, int notifytype, Date requestid,Date deviceid,String gatewayid,String serviceid,
                       String servicetype,int devicenumber,String dimension,int nsflag,String longitude,String weflag,String eventtime){
        this.code=code;
        this.msg=msg;
        this.rid=rid;
        this.notifytype=notifytype;
        this.requestid=requestid;
        this.deviceid=deviceid;
        this.gatewayid=gatewayid;
        this.serviceid=serviceid;
        this.servicetype=servicetype;
        this.devicenumber=devicenumber;
        this.dimension=dimension;
        this.nsflag=nsflag;
        this.longitude=longitude;
        this.weflag=weflag;
        this.eventtime=eventtime;
    }
*/
//    private int code;
//    private  String msg;
    private int rid;
    private int notifytype;
    private Date requestid;
    private Date deviceid;
    private String gatewayid;
    private String serviceid;
    private String servicetype;
    private int devicenumber;
    private String dimension;
    private int nsflag;
    private String longitude;
    private String weflag;
    private String eventtime;

    /*
    public void setCode(int code){
        this.code=code;
    }

    public int getCode(){
        return code;
    }

    public void setMsg(String msg){
        this.code=code;
    }

    public String getMsg(){
        return msg;
    }

*/
    public void setRid(int rid){
        this.rid=rid;
    }
    public int getRid(){
        return rid;
    }

    public void setNotifytype(int notifytype) {
        this.notifytype = notifytype;
    }

    public int getNotifytype() {
        return notifytype;
    }

    public void setRequestid(Date requestid) {
        this.requestid = requestid;
    }

    public Date getRequestid() {
        return requestid;
    }

    public void setDeviceid(Date deviceid) {
        this.deviceid = deviceid;
    }

    public Date getDeviceid() {
        return deviceid;
    }

    public void setGatewayid(String gatewayid) {
        this.gatewayid = gatewayid;
    }

    public String getGatewayid() {
        return gatewayid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setDevicenumber(int devicenumber) {
        this.devicenumber = devicenumber;
    }

    public int getDevicenumber() {
        return devicenumber;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDimension() {
        return dimension;
    }

    public void setNsflag(int nsflag) {
        this.nsflag = nsflag;
    }

    public int getNsflag() {
        return nsflag;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setWeflag(String weflag) {
        this.weflag = weflag;
    }

    public String getWeflag() {
        return weflag;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }

    public String getEventtime() {
        return eventtime;
    }


    @Override
    public String toString() {
        return "rid:"+rid+",notify:"+notifytype+",requestid"+requestid+",deviceid"+deviceid+
                ",gatewayid"+gatewayid+",serviceid"+serviceid+",servicetype"+servicetype
                +",devicenumber"+devicenumber+",dimension"+dimension+",nsflag"+nsflag+",longitude"+longitude
                +",weflag"+weflag+",eventtime"+eventtime;
    }
}
