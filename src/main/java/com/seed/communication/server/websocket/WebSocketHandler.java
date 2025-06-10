package com.seed.communication.server.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seed.communication.common.MessageHandler;
import com.seed.communication.common.SessionManager;
import com.seed.entity.SysDevice;
import com.seed.service.SysDeviceService;
import com.seed.utils.JsonUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Resource
    private SessionManager sessionManager;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private SysDeviceService sysDeviceService;

    @Override
    public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session) {
        Map<String, String> headers = getHeadersFromSession(session);
        String deviceIdAuth = headers.get("device-Id");
        String token = headers.get("Authorization");
        if (deviceIdAuth == null || deviceIdAuth.isEmpty()) {
            logger.error("设备ID为空");
            try {
                session.close(CloseStatus.BAD_DATA.withReason("设备ID为空"));
            } catch (IOException e) {
                logger.error("关闭WebSocket连接失败", e);
            }
            return;
        }
//        if (token != null && !token.isEmpty()) {//优先使用token判断
//            token = token.replace("Bearer ", "");
//            SysDevice device = new SysDevice();
//            device.setCode(token);
//            device.setDeviceId(deviceIdAuth);
//            device.setCreateTime(DateUtil.offsetMonth(DateUtil.date(), -1));//设置过期时间，目前给一个月有效期
//            if(sysDeviceService.queryVerifyCode(device) == null){//没有有效token
//                logger.error("设备提供的token不正确");
//                try {
//                    session.close(CloseStatus.BAD_DATA.withReason("设备提供的token不正确"));
//                } catch (IOException e) {
//                    logger.error("关闭WebSocket连接失败", e);
//                }
//                return;
//            }
//        }else{

        messageHandler.afterConnection(new WebSocketSession(session), deviceIdAuth);
        logger.info("WebSocket连接建立成功 - SessionId: {}, DeviceId: {}", session.getId(), deviceIdAuth);

    }

    @Override
    protected void handleTextMessage(org.springframework.web.socket.WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        SysDevice device = sessionManager.getDeviceConfig(sessionId);
        String payload = message.getPayload();
        String deviceId = null;
        if (device == null) {
            deviceId = getHeadersFromSession(session).get("device-Id");
            if (deviceId == null) {
                logger.error("无法确定设备ID");
                return;
            }
        }
        // 首先尝试解析JSON消息
        try {
            JsonNode jsonNode = JsonUtil.OBJECT_MAPPER.readTree(payload);
            String messageType = jsonNode.path("type").asText();

            // hello消息应该始终处理，无论设备是否绑定
            if ("hello".equals(messageType)) {
                handleHelloMessage(session, jsonNode);
                return;
            }
            messageHandler.handleTextMessage(sessionId, jsonNode, deviceId);
        } catch (Exception e) {
            logger.error("handleTextMessage处理失败", e);
        }
    }

    @Override
    protected void handleBinaryMessage(org.springframework.web.socket.WebSocketSession session, BinaryMessage message) {
        String sessionId = session.getId();
        SysDevice device = sessionManager.getDeviceConfig(sessionId);
        if (device == null) {
            return;
        }
        messageHandler.handleBinaryMessage(sessionId, message.getPayload().array());
    }

    @Override
    public void afterConnectionClosed(org.springframework.web.socket.WebSocketSession session, CloseStatus status) {
        String sessionId = session.getId();
        messageHandler.afterConnectionClosed(sessionId);
        logger.info("WebSocket连接关闭 - SessionId: {}, 状态: {}", sessionId, status);
    }

    @Override
    public void handleTransportError(org.springframework.web.socket.WebSocketSession session, Throwable exception) {
        // 检查是否是客户端正常关闭连接导致的异常
        if (isClientCloseRequest(exception)) {
            // 客户端主动关闭，记录为信息级别日志而非错误
            logger.info("WebSocket连接被客户端主动关闭 - SessionId: {}", session.getId());
        } else {
            // 真正的传输错误
            logger.error("WebSocket传输错误 - SessionId: {}", session.getId(), exception);
        }
    }

    /**
     * 判断异常是否由客户端主动关闭连接导致
     */
    private boolean isClientCloseRequest(Throwable exception) {
        // 检查常见的客户端关闭连接导致的异常类型
        if (exception instanceof IOException) {
            String message = exception.getMessage();
            if (message != null) {
                return message.contains("Connection reset by peer") ||
                    message.contains("Broken pipe") ||
                    message.contains("Connection closed") ||
                    message.contains("远程主机强迫关闭了一个现有的连接");
            }
            // 处理EOFException，这通常是客户端关闭连接导致的
            return exception instanceof java.io.EOFException;
        }
        return false;
    }

    private void handleHelloMessage(org.springframework.web.socket.WebSocketSession session, JsonNode jsonNode) {
        String sessionId = session.getId();
        logger.info("收到hello消息 - SessionId: {}, JsonNode: {}", sessionId, jsonNode);

        // 解析音频参数
        JsonNode audioParams = jsonNode.path("audio_params");
        String format = audioParams.path("format").asText();
        int sampleRate = audioParams.path("sample_rate").asInt();
        int channels = audioParams.path("channels").asInt();
        int frameDuration = audioParams.path("frame_duration").asInt();

        logger.info("客户端音频参数 - 格式: {}, 采样率: {}, 声道: {}, 帧时长: {}ms",
                format, sampleRate, channels, frameDuration);

        // 回复hello消息
        ObjectNode response = JsonUtil.OBJECT_MAPPER.createObjectNode();
        response.put("type", "hello");
        response.put("transport", "websocket");
        response.put("session_id", sessionId);

        // 添加音频参数（可以根据服务器配置调整）
        ObjectNode responseAudioParams = response.putObject("audio_params");
        responseAudioParams.put("format", format);
        responseAudioParams.put("sample_rate", sampleRate);
        responseAudioParams.put("channels", channels);
        responseAudioParams.put("frame_duration", frameDuration);

        try {
            session.sendMessage(new TextMessage(response.toString()));
        } catch (Exception e) {
            logger.error("发送hello响应失败", e);
        }
    }

    private Map<String, String> getHeadersFromSession(org.springframework.web.socket.WebSocketSession session) {
        // 尝试从请求头获取设备ID
        String[] deviceKeys = { "device-Id", "mac_address", "uuid", "Authorization" };

        Map<String, String> headers = new HashMap<>();

        for (String key : deviceKeys) {
            String value = session.getHandshakeHeaders().getFirst(key);
            if (value != null) {
                headers.put(key, value);
            }
        }
        // 尝试从URI参数中获取
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                for (String key : deviceKeys) {
                    String paramPattern = key + "=";
                    int startIdx = query.indexOf(paramPattern);
                    if (startIdx >= 0) {
                        startIdx += paramPattern.length();
                        int endIdx = query.indexOf('&', startIdx);
                        headers.put(key, endIdx >= 0 ? query.substring(startIdx, endIdx) : query.substring(startIdx));
                    }
                }
            }
        }
        return headers;
    }
}