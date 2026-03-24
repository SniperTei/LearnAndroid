# 📱 真机调试配置完成

## ✅ 已修改配置

### 容器配置
**文件**: `app/src/main/assets/container_config.json`

```json
{
  "homeUrl": "http://192.168.229.128:5173",  // ✅ 改为局域网 IP
  "allowedDomains": ["localhost", "192.168.229.128", "156.226.180.172"]
}
```

### 网络安全配置
**文件**: `app/src/main/res/xml/network_security_config.xml`

已添加 `192.168.229.128` 到白名单

---

## 📶 准备工作（重要！）

### 1. 确保 H5 开发服务器正在运行

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnVue/yimei-demo
npm run dev
```

**确认输出**:
```
VITE v7.1.11 ready in 500 ms

➜  Local:   http://localhost:5173/
➜  Network: http://192.168.229.128:5173/  ✅ 真机用这个
```

### 2. 确保手机和电脑在同一 WiFi 网络

- ✅ 手机连接的 WiFi 与电脑相同
- ✅ 防火墙允许 Vite 5173 端口

### 3. 测试电脑 IP 是否可访问

**在手机浏览器中打开**:
```
http://192.168.229.128:5173
```

**应该看到**: 医美 H5 登录页面

---

## 🚀 运行步骤

### Step 1: 重新编译安装

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnAndroid/WebBox

# 清理旧的构建
./gradlew clean

# 重新编译
./gradlew assembleDebug

# 安装到真机（通过 USB 连接）
./gradlew installDebug
```

### Step 2: 在手机上打开应用

1. 找到 "WebBox" 或 "医美 H5" 应用图标
2. 点击启动

**应该看到**:
- ✅ 显示 "医美 H5" 启动画面
- ✅ 粉色主题
- ✅ 自动跳转到 H5 登录页

---

## 🐛 故障排查

### 问题 1: 真机无法访问网页

**症状**: 白屏或 ERR_CONNECTION_REFUSED

#### 检查 1: 电脑 IP 地址

```bash
# 查看电脑 IP（可能不同）
ifconfig | grep "inet " | grep -v 127.0.0.1
```

**如果 IP 不同**，修改 `container_config.json`:
```json
{
  "homeUrl": "http://新的IP:5173",
  "allowedDomains": ["新的IP", "192.168.229.128"]
}
```

#### 检查 2: 防火墙

```bash
# macOS 允许 Vite 端口
# 系统偏好设置 → 安全性与隐私 → 防火墙 → 防火墙选项
# 添加 Node.js（或其他允许入站连接）
```

或者临时关闭防火墙测试：

#### 检查 3: Vite 服务器监听地址

**确认 vite.config.js**:
```javascript
server: {
  host: '0.0.0.0',  // ✅ 监听所有网络接口
  port: 5173
}
```

#### 检查 4: 手机和电脑在同一网络

```
电脑: 192.168.229.128
手机: 设置 → WLAN → 当前网络 → 查看IP
确保前三位相同: 192.168.x.x
```

---

### 问题 2: Vite 显示 "Network: use --host to expose"

**在手机浏览器中无法访问**

#### 解决方案 1: 使用 --host 参数

```bash
# 修改 package.json
"dev": "vite --host"

# 或直接运行
npm run dev -- --host
```

#### 解决方案 2: 手动指定监听地址

**vite.config.js**:
```javascript
server: {
  host: '0.0.0.0',  // ✅ 已配置
  port: 5173,
  strictPort: true  // 如果端口被占用则失败
}
```

---

### 问题 3: CORS 错误

**症状**: 控制台显示 CORS 策略错误

**确认 vite.config.js**:
```javascript
server: {
  cors: true,  // ✅ 已启用 CORS
}
```

---

## 🔍 调试技巧

### 1. 查看应用日志

```bash
# 实时日志
adb logcat | grep -i "webbox\|container\|webview"
```

### 2. Chrome DevTools 远程调试

```bash
# 1. 启用 USB 调试
adb shell setprop debug.wxdebug 1

# 2. 在 Chrome 中访问
chrome://inspect

# 3. 找到 WebView 并点击 inspect
```

### 3. 查看网络请求

在 Chrome DevTools 中:
- Network 标签页
- 查看加载的资源
- 检查是否有 404 或 CORS 错误

---

## 📊 地址总结

| 场景 | 地址 | 用途 |
|------|------|------|
| **模拟器** | `http://10.0.2.2:5173` | 模拟器访问主机 |
| **真机（局域网）** | `http://192.168.229.128:5173` | ✅ 当前配置 |
| **本地测试** | `http://localhost:5173` | 电脑浏览器 |

---

## 🎯 成功标志

当你看到以下内容，说明配置成功：

### 应用启动
```
✅ 应用正常启动
✅ 显示 "医美 H5"
✅ 进度加载
✅ 跳转到 H5 页面
```

### H5 页面
```
✅ 登录界面正常显示
✅ 样式加载正确
✅ 可以点击交互
✅ 无网络错误
```

### Chrome DevTools Console
```
✅ 无 CORS 错误
✅ Vite HMR 更新正常
✅ JS Bridge 调用成功
```

---

## 🔄 如果 IP 地址变化

### 电脑 IP 变了怎么办？

```bash
# 1. 查看新的 IP
ifconfig | grep "inet " | grep -v 127.0.0.1

# 2. 修改配置文件
vim app/src/main/assets/container_config.json

# 3. 重新编译安装
./gradlew assembleDebug && ./gradlew installDebug
```

---

## 💡 提示

### Vite 开发服务器已正确配置

```javascript
server: {
  host: '0.0.0.0',  // ✅ 监听所有网络接口
  port: 5173,
  cors: true      // ✅ 允许跨域
}
```

### 验证手机是否可以访问

**在手机浏览器中测试**:
```
http://192.168.229.128:5173
```

如果能看到 H5 页面，说明网络通畅！

---

## ✅ 配置完成

**当前配置**:
- ✅ homeUrl: `http://192.168.229.128:5173`（你的电脑 IP）
- ✅ allowedDomains: 已包含局域网 IP
- ✅ networkSecurity: 允许 HTTP
- ✅ Vite: 监听 0.0.0.0（所有网络）

**现在重新运行即可**:
```bash
./gradlew clean
./gradlew installDebug
```

---

**如果还有问题**，请告诉我：
1. 浏览器显示的具体错误（ERR_CONNECTION_REFUSED？）
2. 手机和电脑是否在同一 WiFi
3. 电脑的防火墙设置

祝真机调试顺利！📱✨
