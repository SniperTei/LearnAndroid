# ✅ 真机调试快速指南

## 🎯 配置已更新

**你的电脑 IP**: `192.168.229.128`
**真机访问地址**: `http://192.168.229.128:5173`

---

## 🚀 快速运行

### 1️⃣ 确保 H5 服务运行中

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnVue/yimei-demo
npm run dev
```

✅ 看到 `Network: http://192.168.229.128:5173/` 即可

### 2️⃣ 确认手机和电脑在同一 WiFi

**检查手机**:
- 设置 → WLAN → 当前连接的网络
- 查看是否与电脑同一网络

### 3️⃣ 安装应用

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnAndroid/WebBox
./gradlew clean
./gradlew installDebug
```

### 4️⃣ 在手机上打开应用

找到 "医美 H5" 或 "WebBox" 应用图标，点击启动

---

## ✅ 成功标志

```
✅ 显示 "医美 H5" 启动画面
✅ 粉色主题
✅ 自动跳转到 H5 登录页
✅ 可以正常交互
```

---

## 🐛 如果无法访问

### 检查 1: 电脑 IP 是否正确

```bash
ifconfig | grep "inet " | grep -v 127.0.0.1
```

如果不是 `192.168.229.128`，修改 `container_config.json`:
```json
{
  "homeUrl": "http://新IP:5173",
  "allowedDomains": ["新IP"]
}
```

### 检查 2: 手机浏览器能否访问

**在手机浏览器中打开**:
```
http://192.168.229.128:5173
```

**应该看到**: 医美 H5 登录页面

### 检查 3: Vite 是否监听所有接口

确认 `vite.config.js` 中有:
```javascript
server: {
  host: '0.0.0.0'  // ✅ 必须是这个
}
```

### 检查 4: 防火墙

```bash
# macOS: 系统偏好设置 → 安全性与隐私 → 防火墙
# 确保允许 Node.js 或入站连接
```

---

## 🔄 IP 地址变化后

```bash
# 1. 查看新 IP
ifconfig | grep "inet " | grep -v 127.0.0.1

# 2. 修改配置文件
vim app/src/main/assets/container_config.json
# 改 homeUrl 和 allowedDomains

# 3. 重新安装
./gradlew assembleDebug && ./gradlew installDebug
```

---

**详细文档**: [REAL_DEVICE_SETUP.md](./REAL_DEVICE_SETUP.md)

**配置完成** ✅
**真机调试就绪** 📱
