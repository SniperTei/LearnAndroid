# WebBox 容器配置指南

## 📖 配置文件说明

WebBox 容器通过 JSON 配置文件驱动，实现"一次编译，多应用运行"。只需修改配置文件，就能将同一个 APK 变成不同的应用。

---

## 🎯 快速开始

### 1. 复制配置文件

将 `assets/container_config.json` 复制一份，重命名为 `container_config_xxx.json`（xxx 是你的应用标识）。

### 2. 修改配置

编辑新配置文件，修改以下关键字段：

```json
{
  "appId": "com.yourapp.id",          // 应用唯一标识（必须）
  "appName": "你的应用名称",            // 应用名称（必须）
  "homeUrl": "https://yourapp.com",   // 首页 URL（必须）
  "allowedDomains": [                 // 域名白名单（推荐）
    "yourapp.com",
    "api.yourapp.com"
  ]
}
```

### 3. 加载配置

修改 `ContainerActivity.kt`，加载新的配置文件：

```kotlin
// 从 assets 加载指定配置文件
ContainerConfigManager.loadFromAssets(this, "container_config_xxx.json")
```

### 4. 编译运行

```bash
./gradlew assembleDebug
```

---

## 📋 配置字段详解

### 基础配置

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `appId` | String | ✅ | 应用唯一标识，用于隔离存储、Cookie 等 |
| `appName` | String | ✅ | 应用名称，显示在标题栏、通知栏等 |
| `homeUrl` | String | ✅ | 首页 URL，容器启动后加载的第一个页面 |
| `allowedDomains` | Array<String> | ❌ | 域名白名单，用于安全控制 |

### 功能配置

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `enabledFeatures` | Array<String> | ❌ | 启用的功能列表（见下文） |

#### 可用功能列表

| 功能 | 说明 | JS Bridge 方法 |
|------|------|----------------|
| `device` | 设备信息 | `device.getDeviceInfo()` |
| `camera` | 拍照/相册 | `camera.takePhoto()`, `camera.selectImage()` |
| `location` | 地理位置 | `location.getCurrentLocation()`, `location.watchPosition()` |
| `share` | 分享 | `share.shareToWeChat()`, `share.shareToSystem()` |
| `clipboard` | 剪贴板 | `clipboard.setClipboard()`, `clipboard.getClipboard()` |
| `network` | 网络状态 | `network.getNetworkType()`, `network.isNetworkAvailable()` |

### 主题配置

| 字段 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `theme.statusBarColor` | String | `"#FF000000"` | 状态栏颜色（十六进制） |
| `theme.toolbarColor` | String | `"#FFFFFFFF"` | 工具栏背景颜色 |
| `theme.toolbarTitleColor` | String | `"#FF000000"` | 工具栏标题颜色 |
| `theme.showToolbar` | Boolean | `true` | 是否显示导航栏 |
| `theme.showBackButton` | Boolean | `true` | 是否显示返回按钮 |
| `theme.showCloseButton` | Boolean | `false` | 是否显示关闭按钮 |

### 网络安全配置

| 字段 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `networkSecurity.allowCleartext` | Boolean | `false` | 是否允许明文流量（HTTP） |
| `networkSecurity.trustedDomains` | Array<String> | `[]` | 额外的信任域名（自签名证书） |

### WebView 配置

| 字段 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `webview.javaScriptEnabled` | Boolean | `true` | 是否启用 JavaScript |
| `webview.domStorageEnabled` | Boolean | `true` | 是否启用 DOM 存储 |
| `webview.databaseEnabled` | Boolean | `true` | 是否启用数据库 |
| `webview.cacheEnabled` | Boolean | `true` | 是否启用缓存 |
| `webview.userAgentSuffix` | String | `null` | User-Agent 后缀 |
| `webview.defaultZoom` | Float | `1.0` | 默认缩放比例 |
| `webview.minZoom` | Float | `0.5` | 最小缩放比例 |
| `webview.maxZoom` | Float | `2.0` | 最大缩放比例 |
| `webview.pageLoadTimeout` | Long | `30000` | 页面加载超时时间（毫秒） |

---

## 🌟 配置示例

### 示例 1: 商城应用

```json
{
  "appId": "com.shop.mall",
  "appName": "购物商城",
  "homeUrl": "https://mall.example.com",
  "allowedDomains": ["mall.example.com"],
  "enabledFeatures": ["device", "camera", "share", "location"],
  "theme": {
    "statusBarColor": "#FFE91E63",
    "toolbarColor": "#FFE91E63",
    "toolbarTitleColor": "#FFFFFFFF"
  }
}
```

### 示例 2: 外卖应用

```json
{
  "appId": "com.food.delivery",
  "appName": "美食外卖",
  "homeUrl": "https://food.example.com",
  "allowedDomains": ["food.example.com"],
  "enabledFeatures": ["device", "location", "camera"],
  "theme": {
    "statusBarColor": "#FFFF9800",
    "toolbarColor": "#FFFF9800"
  }
}
```

### 示例 3: 本地开发环境

```json
{
  "appId": "com.example.dev",
  "appName": "开发环境",
  "homeUrl": "http://10.0.2.2:5173",
  "allowedDomains": ["localhost", "10.0.2.2"],
  "enabledFeatures": ["device", "camera", "location", "share", "clipboard", "network"],
  "networkSecurity": {
    "allowCleartext": true
  }
}
```

---

## 🔒 安全建议

### 1. 使用域名白名单

```json
{
  "allowedDomains": [
    "yourapp.com",
    "api.yourapp.com"
  ]
}
```

### 2. 生产环境禁用明文流量

```json
{
  "networkSecurity": {
    "allowCleartext": false
  }
}
```

### 3. 最小权限原则

只启用必要的功能：

```json
{
  "enabledFeatures": ["device", "camera"]
  // 不要启用所有功能！
}
```

---

## 🐛 常见问题

### 1. 配置文件加载失败

**错误**: `配置加载失败 请确保 assets/container_config.json 存在`

**解决**:
- 确认配置文件在 `app/src/main/assets/` 目录下
- 确认文件名正确
- 确认 JSON 格式有效（可使用在线工具验证）

### 2. 功能调用失败

**错误**: `模块 'xxx' 不存在或未授权`

**解决**:
- 在 `enabledFeatures` 中添加对应功能
- 确认功能名称拼写正确

### 3. WebView 无法加载页面

**检查**:
- `homeUrl` 是否正确
- `allowedDomains` 是否包含目标域名
- 生产环境是否使用了 HTTPS
- `networkSecurity.allowCleartext` 是否正确配置

---

## 📊 配置验证

容器启动时会自动验证配置：

```kotlin
val (valid, error) = ContainerConfigManager.validateConfig()
if (!valid) {
    // 配置无效，显示错误信息
    showError("配置无效\n$error")
}
```

验证项目：
- ✅ `appId` 不能为空
- ✅ `appName` 不能为空
- ✅ `homeUrl` 不能为空且格式正确
- ✅ `enabledFeatures` 中的功能必须有效
- ✅ `allowedDomains` 中的域名格式必须正确

---

## 🚀 高级用法

### 动态加载配置

```kotlin
// 从远程服务器下载配置
suspend fun loadRemoteConfig(url: String) {
    val configJson = withContext(Dispatchers.IO) {
        URL(url).readText()
    }
    ContainerConfigManager.loadFromJson(configJson)
}
```

### 多环境切换

```kotlin
// 根据构建类型加载不同配置
val configFile = if (BuildConfig.DEBUG) {
    "container_config_dev.json"
} else {
    "container_config_prod.json"
}
ContainerConfigManager.loadFromAssets(this, configFile)
```

---

需要更多帮助？查看 [README.md](../README.md) 或提交 Issue。
