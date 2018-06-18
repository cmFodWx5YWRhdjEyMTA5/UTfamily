package com.ufotech.ufo.utfamily.model;

import com.google.gson.Gson;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/30    下午 11:38
 *  @description ：接收樹莓派傳來的數值
 *  -----------------------------------------------------------------------------------------
 */
public class SensorDatas {

    /**
     * {"ArduinoSensorData":"1,2,3"}
     */

    private String ArduinoSensorData;

    public String getArduinoSensorData() {
        return ArduinoSensorData;
    }

    public void setArduinoSensorData(String arduinoSensorData) {
        ArduinoSensorData = arduinoSensorData;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
