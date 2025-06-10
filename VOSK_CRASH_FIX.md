# Vosk SIGSEGV 崩溃修复指南

## 问题描述
在 macOS ARM64 (Apple Silicon) 环境下，应用启动时可能遇到 Vosk 语音识别库导致的 SIGSEGV 崩溃错误：

```
SIGSEGV (0xb) at pc=0x000000013479c4e0, pid=37808, tid=29703
Problematic frame:
C  [libvosk.dylib+0x9c4e0]  Model::Ref()+0x8
```

## 根本原因
- Vosk 原生库在 macOS ARM64 架构上存在兼容性问题
- 手动加载 `libvosk.dylib` 时可能导致内存访问违规
- 原生 C++ 库的内存管理问题

## 解决方案

### 方案1：自动检测与跳过（已实现）
应用现在会自动检测 macOS ARM64 环境，并跳过 Vosk 初始化：

```java
// 自动检测并跳过有问题的环境
if (osName.contains("mac") && osArch.contains("aarch64")) {
    logger.warn("检测到 macOS ARM64 架构，跳过 Vosk 初始化");
    return;
}
```

### 方案2：配置禁用 Vosk
在 `application.properties` 中添加：

```properties
# 完全禁用 Vosk 语音识别
vosk.enabled=false
```

### 方案3：使用替代 STT 服务
配置其他语音识别服务：

1. **阿里云语音识别**
   - provider: `aliyun`
   - 需要阿里云 AccessKey

2. **腾讯云语音识别**
   - provider: `tencent`
   - 需要腾讯云 SecretId/SecretKey

3. **讯飞语音识别**
   - provider: `xfyun`
   - 需要讯飞 AppID/ApiKey

## 验证修复
启动应用后，查看日志：

✅ **成功跳过**：
```
检测到 macOS ARM64 架构，暂时跳过 Vosk 初始化以避免 native library 崩溃
请考虑使用其他 STT 服务（如：阿里云、腾讯云等）
```

✅ **配置禁用**：
```
Vosk 语音识别已通过配置禁用 (vosk.enabled=false)
```

## 注意事项
1. 这是 Vosk 库本身的问题，不是应用代码问题
2. 修复后应用将正常启动，但需要配置其他 STT 服务才能使用语音识别功能
3. 建议在生产环境中使用云端 STT 服务以获得更好的识别准确率

## 相关 Issue
- Vosk macOS ARM64 兼容性问题
- 原生库内存管理问题
- 建议关注 Vosk 官方更新 