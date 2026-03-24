# 🚀 WebBox Android 容器运行指南

## ✅ 配置完成

Android 项目已配置好，可以加载医美 H5 应用！

---

## 📋 配置总结

### 容器配置文件
**文件**: `app/src/main/assets/container_config.json`

```json
{
  "appId": "com.yimei.h5",
  "appName": "医美 H5",
  "homeUrl": "http://10.0.2.2:5173",
  "allowedDomains": ["localhost", "10.0.2.2", "156.226.180.172"],
  "enabledFeatures": [
    "device",
    "camera",
    "location",
    "share",
    "clipboard",
    "network"
  ]
}
```

### 关键配置说明

| 配置项 | 值 | 说明 |
|--------|-----|------|
| **homeUrl** | `http://10.0.2.2:5173` | H5 开发服务器地址 |
| **allowCleartext** | `true` | 允许 HTTP（开发环境） |
| **主题色** | `#FFE91E63` | 粉色主题 |
| **启用功能** | 全部 6 个 | 完整功能支持 |

---

## 🎯 运行步骤

### Step 1: 启动 H5 开发服务器

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnVue/yimei-demo
npm run dev
```

**期望输出**:
```
VITE v7.1.11 ready in 500 ms

➜  Local:   http://localhost:5173/
➜  Network: http://192.168.x.x:5173/
➜  press h + enter to show help
```

✅ 确认服务器启动成功，并记录端口（默认 5173）

---

### Step 2: 启动 Android 模拟器

#### 选项 A: 使用 Android Studio

1. 打开 Android Studio
2. 点击 AVD Manager 📱
3. 选择一个模拟器（Android 10+ API 29+）
4. 点击启动 ▶️

#### 选项 B: 使用命令行

```bash
# 查看可用的模拟器
emulator -list-avds

# 启动模拟器
emulator -avd <模拟器名称>
```

✅ 确保模拟器已启动并进入系统

---

### Step 3: 设置端口转发（重要！）

Android 模拟器需要端口转发才能访问主机的 H5 服务：

```bash
# 查看模拟器端口
adb devices

# 设置端口转发：将模拟器的 5173 端口转发到主机的 5173 端口
adb reverse tcp:5173 tcp:5173

# 验证转发
adb reverse list
```

**期望输出**:
```
tcp:5173 tcp:5173
```

✅ 确认端口转发成功

---

### Step 4: 编译并安装应用

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnAndroid/WebBox

# 方式 1: 使用 Gradle（推荐）
./gradlew installDebug

# 方式 2: 使用 Android Studio
# 打开项目，点击 Run ▶️ 按钮
```

**期望输出**:
```
BUILD SUCCESSFUL in 30s
70 actionable tasks: 10 executed, 60 up-to-date
Installing app-debug.apk...
Success
```

✅ 应用已安装到模拟器

---

### Step 5: 启动应用

模拟器会自动启动应用，或手动点击应用图标

**启动流程**:
1. 显示 "医美 H5" 启动画面
2. 加载配置...
3. 容器已就绪
4. 自动跳转到 H5 页面

✅ 看到医美 H5 登录页面

---

## 🧪 功能测试

### 测试 1: 设备信息获取

**在浏览器控制台**（Chrome DevTools）:

```javascript
// 打开 DevTools
chrome://inspect

// 连接到 WebView
// 在 Console 中输入：
import device from '@/utils/device';
device.getDeviceInfo((result) => {
  console.log('设备信息:', result);
});
```

**期望结果**:
```
{
  code: "000000",
  msg: "success",
  data: {
    deviceModel: "sdk_gphone64_x86_64",
    osVersion: "Android 12",
    appVersion: "1.0"
  },
  success: true
}
```

---

### 测试 2: 拍照功能

```javascript
device.takePhoto('camera', (result) => {
  console.log('拍照结果:', result);
  if (result.success) {
    // 显示图片
    document.getElementById('preview').src = result.data.imageBase64;
  }
});
```

**期望结果**:
- 打开相机界面
- 拍照后返回 Base64 图片
- 图片显示在页面上

---

### 测试 3: 剪贴板功能

```javascript
// 设置剪贴板
device.setClipboard('测试文本', (result) => {
  console.log('设置结果:', result);
});

// 获取剪贴板
device.getClipboard((result) => {
  console.log('剪贴板内容:', result.data.text);
});
```

**期望结果**:
- 成功设置和获取剪贴板内容
- 返回 `{ code: "000000", success: true }`

---

### 测试 4: 网络状态

```javascript
device.getNetworkType((result) => {
  console.log('网络类型:', result.data.type);
  console.log('是否可用:', result.data.available);
});
```

**期望结果**:
```
网络类型: wifi (或 cellular)
是否可用: true
```

---

## 🐛 故障排查

### 问题 1: 应用白屏或加载失败

**症状**: WebView 显示白屏或 ERR_CONNECTION_REFUSED

**检查**:
```bash
# 1. 确认 H5 服务器是否运行
curl http://localhost:5173

# 2. 确认端口转发是否设置
adb reverse list

# 3. 查看应用日志
adb logcat | grep -i "webbox\|container\|webview"
```

**解决**:
```bash
# 重新设置端口转发
adb reverse tcp:5173 tcp:5173

# 重启应用
adb shell am force-stop com.sniper.webbox
adb shell am start -n com.sniper.webbox/.container.ContainerActivity
```

---

### 问题 2: JS Bridge 调用失败

**症状**: 控制台显示 `Android容器未注册`

**检查**:
```javascript
// 在 DevTools Console 中检查
console.log('Android:', window.Android);
```

**期望**: 应该输出一个对象，而不是 `undefined`

**解决**:
- 确认使用的是 WebBox 容器（不是普通浏览器）
- 确认 device.js 已修复
- 查看日志: `adb logcat | grep -i "jsbridge\|native"`

---

### 问题 3: 权限错误

**症状**: 控制台显示 `模块 'xxx' 不存在或未授权`

**检查**:
```bash
# 查看配置
cat app/src/main/assets/container_config.json | grep enabledFeatures
```

**解决**:
- 确认功能已启用: `"enabledFeatures": ["camera", ...]`
- 重新安装应用: `./gradlew installDebug`

---

### 问题 4: 图片无法加载

**症状**: 图片显示破碎图标或 ERR_BLOCKED_BY_CLIENT

**检查**:
- 查看网络配置: `cat app/src/main/res/xml/network_security_config.xml`
- 确认域名在白名单中

**解决**:
```bash
# 添加图片域名到白名单
# 编辑 network_security_config.xml
# 重启应用
```

---

## 📊 调试技巧

### 1. Chrome DevTools 远程调试

```bash
# 查看可调试的 WebView
chrome://inspect

# 点击 "inspect" 链接
# - Elements: 查看 DOM
# - Console: 执行 JS
# - Network: 查看网络请求
# - Sources: 调试代码
```

### 2. 查看应用日志

```bash
# 实时查看所有日志
adb logcat

# 过滤 WebBox 相关日志
adb logcat | grep -i "webbox\|container\|jsbridge"

# 过滤特定标签
adb logcat -s "WebBox:*" "ContainerActivity:*"
```

### 3. 清除应用数据

```bash
# 完全卸载
adb uninstall com.sniper.webbox

# 清除数据（保留应用）
adb shell pm clear com.sniper.webbox

# 重启应用
adb shell am start -n com.sniper.webbox/.container.ContainerActivity
```

---

## 🎨 自定义配置

### 修改主题色

编辑 `container_config.json`:

```json
{
  "theme": {
    "statusBarColor": "#FF6200EE",    // 状态栏颜色
    "toolbarColor": "#FF6200EE",      // 工具栏颜色
    "toolbarTitleColor": "#FFFFFFFF"  // 标题颜色
  }
}
```

### 修改启用功能

```json
{
  "enabledFeatures": [
    "device",    // ✅ 启用设备信息
    "camera",    // ✅ 启用相机/相册
    "location",  // ❌ 禁用地理位置
    "share"      // ❌ 禁用分享
  ]
}
```

### 切换到不同的 H5 项目

```json
{
  "homeUrl": "http://10.0.2.2:8080",  // 其他项目
  "appName": "其他应用"
}
```

---

## 📱 常用命令

### 应用管理

```bash
# 安装应用
./gradlew installDebug

# 卸载应用
adb uninstall com.sniper.webbox

# 启动应用
adb shell am start -n com.sniper.webbox/.container.ContainerActivity

# 停止应用
adb shell am force-stop com.sniper.webbox
```

### 端口转发

```bash
# 设置端口转发
adb reverse tcp:5173 tcp:5173

# 查看所有转发
adb reverse list

# 移除转发
adb reverse --remove tcp:5173
```

### 文件传输

```bash
# 从模拟器拉取文件
adb pull /sdcard/Pictures/demo.jpg

# 推送文件到模拟器
adb push local.txt /sdcard/

# 查看模拟器文件
adb shell ls -l /sdcard/
```

---

## 🎯 成功标志

当你看到以下内容，说明配置成功！

### 启动画面
```
✅ 医美 H5 标题
✅ 粉色主题 (#FFE91E63)
✅ 进度条显示
```

### 控制台日志
```
📦 容器配置摘要
══════════════════════════════════════════════
📦 容器配置摘要
══════════════════════════════════════════════
应用 ID:     com.yimei.h5
应用名称:    医美 H5
首页 URL:    http://10.0.2.2:5173
白名单域名:  3 个
启用功能:    6 个
显示导航栏:  true
明文流量:    true
══════════════════════════════════════════════
```

### H5 页面加载
```
✅ 登录页面正常显示
✅ 样式正常加载
✅ 可以点击交互
```

### JS Bridge 调用
```
✅ getDeviceInfo 成功
✅ takePhoto 成功
✅ 回调正常执行
```

---

## 📚 相关文档

1. **[README.md](../README.md)** - WebBox 项目文档
2. **[CONFIG_GUIDE.md](../CONFIG_GUIDE.md)** - 配置指南
3. **[WEBBOX_USAGE_GUIDE.md](../LearnVue/yimei-demo/WEBBOX_USAGE_GUIDE.md)** - H5 使用指南

---

## ✅ 准备就绪！

所有配置已完成，你可以开始运行了！

**快速命令**:
```bash
# 1. 启动 H5 服务
cd /Users/zhengnan/Sniper/Developer/github/LearnVue/yimei-demo
npm run dev

# 2. 启动模拟器（新终端）
emulator -avd <模拟器名称>

# 3. 设置端口转发（新终端）
adb reverse tcp:5173 tcp:5173

# 4. 安装应用（新终端）
cd /Users/zhengnan/Sniper/Developer/github/LearnAndroid/WebBox
./gradlew installDebug
```

祝运行顺利！🎉

---

**配置完成时间**: 2026-03-24
**适配项目**: 医美 H5 (yimei-demo)
**容器版本**: WebBox v1.0
