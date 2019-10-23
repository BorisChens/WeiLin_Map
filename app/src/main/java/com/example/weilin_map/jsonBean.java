package com.example.weilin_map;

import java.util.List;

public class jsonBean  {
    private String  code;
    private String msg;
    private List<extend>extend_01;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setExtend_01(List<extend> extend_01) {
        this.extend_01 = extend_01;
    }

    public List<extend> getExtend_01() {
        return extend_01;
    }


    @Override
    public String toString() {
        return "{code:"+code+",msg:"+msg+",extend:{"+extend_01+"}}";
    }
}
