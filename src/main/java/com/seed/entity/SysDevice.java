package com.seed.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 设备表
 * 
 * @author Joey
 * 
 */
@JsonIgnoreProperties({ "startTime", "endTime", "start", "limit", "userId", "code" })
public class SysDevice extends Base {
    public static final String DEVICE_STATE_ONLINE = "1";
    public static final String DEVICE_STATE_OFFLINE = "0";

    private String deviceId;

    private String sessionId;

    /**
     * 模型Id
     */
    private Integer modelId;

    /*
     * 语音识别Id
     */
    private Integer sttId;

    /*
     * 角色Id
     */
    private Integer roleId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备状态
     */
    private String state;

    /**
     * 设备对话次数
     */
    private Integer totalMessage;

    /**
     * 验证码
     */
    private String code;

    /**
     * 音频文件
     */
    private String audioPath;

    /**
     * 最后在线时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastLogin;

    /**
     * WiFi名称
     */
    private String wifiName;

    /**
     * IP
     */
    private String ip;

    /**
     * 芯片型号
     */
    private String chipModelName;

    /**
     * 芯片类型
     */
    private String type;

    /**
     * 固件版本
     */
    private String version;

    private Float vadEnergyTh;
    private Float vadSpeechTh;
    private Float vadSilenceTh;
    private Integer vadSilenceMs;

    /**
     * 可用全局function的名称列表(逗号分割)，为空则使用所有全局function
     */
    private String functionNames;

    // 辅助字段，不对应数据库表
    private Integer ttsId;
    private String voiceName;

    public Integer getModelId() {
        return modelId;
    }

    public SysDevice setModelId(Integer modelId) {
        this.modelId = modelId;
        return this;
    }

    public Integer getSttId() {
        return sttId;
    }

    public SysDevice setSttId(Integer sttId) {
        this.sttId = sttId;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysDevice setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public SysDevice setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public SysDevice setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public SysDevice setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public Float getVadEnergyTh() {
        return vadEnergyTh;
    }

    public SysDevice setVadEnergyTh(Float vadEnergyTh) {
        this.vadEnergyTh = vadEnergyTh;
        return this;
    }

    public Float getVadSpeechTh() {
        return vadSpeechTh;
    }

    public SysDevice setVadSpeechTh(Float vadSpeechTh) {
        this.vadSpeechTh = vadSpeechTh;
        return this;
    }

    public Float getVadSilenceTh() {
        return vadSilenceTh;
    }

    public SysDevice setVadSilenceTh(Float vadSilenceTh) {
        this.vadSilenceTh = vadSilenceTh;
        return this;
    }

    public Integer getVadSilenceMs() {
        return vadSilenceMs;
    }

    public SysDevice setVadSilenceMs(Integer vadSilenceMs) {
        this.vadSilenceMs = vadSilenceMs;
        return this;
    }

    public String getState() {
        return state;
    }

    public SysDevice setState(String state) {
        this.state = state;
        return this;
    }

    public Integer getTotalMessage() {
        return totalMessage;
    }

    public SysDevice setTotalMessage(Integer totalMessage) {
        this.totalMessage = totalMessage;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SysDevice setCode(String code) {
        this.code = code;
        return this;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public SysDevice setAudioPath(String audioPath) {
        this.audioPath = audioPath;
        return this;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public SysDevice setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public String getWifiName() {
        return wifiName;
    }

    public SysDevice setWifiName(String wifiName) {
        this.wifiName = wifiName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SysDevice setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getChipModelName() {
        return chipModelName;
    }

    public SysDevice setChipModelName(String chipModelName) {
        this.chipModelName = chipModelName;
        return this;
    }

    public String getType() {
        return type;
    }

    public SysDevice setType(String type) {
        this.type = type;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public SysDevice setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(String functionNames) {
        this.functionNames = functionNames;
    }

    public Integer getTtsId() {
        return ttsId;
    }

    public void setTtsId(Integer ttsId) {
        this.ttsId = ttsId;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    @Override
    public String toString() {
        return "SysDevice [deviceId=" + deviceId + ", sessionId=" + sessionId + ", deviceName=" + deviceName
                + ", state=" + state + ", totalMessage=" + totalMessage + ", code=" + code + ", audioPath=" + audioPath
                + ", lastLogin=" + lastLogin + ", wifiName=" + wifiName + ", ip=" + ip + ", chipModelName="
                + chipModelName + ", type=" + type + ", version=" + version + ", functionNames=" + functionNames;
    }
}