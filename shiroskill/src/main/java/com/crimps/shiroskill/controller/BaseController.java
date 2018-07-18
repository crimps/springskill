package com.crimps.shiroskill.controller;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
    /**
     * 返回操作结果
     * @param flag true:成功|fasle:失败
     * @param msg 说明
     * @return resultMap
     */
    protected Map<String, Object> getResultMap(boolean flag, String msg){
        Map<String, Object> resultMap = new HashMap<>();
        if(flag){
            resultMap.put("code", "0");
        }else{
            resultMap.put("code", "-1");
        }
        resultMap.put("msg", msg);
        return resultMap;
    }
}
