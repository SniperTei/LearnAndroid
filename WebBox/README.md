# WebBox

<div align="center">

**🚀 Android Hybrid 应用框架 - 原生与 H5 的完美融合**

[![Android](https://img.shields.io/badge/Android-10%2B-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/API-29%2B-brightgreen.svg)](https://developer.android.com/guide/topics/manifest/uses-sdk-element)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[快速开始](#快速开始) • [架构设计](#架构设计) • [功能特性](#功能特性) • [API文档](#docsapi文档)

</div>

---

## 📖 项目简介

WebBox 是一个基于 **Kotlin** 开发的 Android Hybrid 应用框架，采用原生 Android + WebView 的混合开发模式。通过自研的 **JS Bridge** 桥接技术，实现 H5 页面与原生功能的双向通信，既保留了原生应用的性能优势，又具备了 Web 开发的灵活性。

### ✨ 核心特性

- 🔒 **安全可靠** - 白名单机制、多层校验、SSL 证书验证
- 🌉 **JS Bridge** - 高效的原生与 H5 双向通信桥接
- 🎨 **模块化设计** - 低耦合、高内聚、易于扩展
- 🚀 **协程支持** - 基于 Kotlin Coroutines 的异步网络请求
- 📱 **现代化架构** - 遵循 Android 最佳实践和 Material Design
- 🔧 **灵活配置** - 支持多环境切换、动态配置管理

---

## 🛠 技术栈

| 类别 | 技术选型 |
|------|---------|
| **开发语言** | Kotlin 1.9+ |
| **最低版本** | Android 10 (API 29) |
| **目标版本** | Android 14 (API 36) |
| **网络请求** | OkHttp4 + Retrofit2 + Gson |
| **异步处理** | Kotlin Coroutines |
| **WebView** | Android System WebView |
| **依赖注入** | 单例模式管理器 |

---

## 🏗 架构设计

### 整体架构图

```
┌─────────────────────────────────────────────────────────┐
│                      WebBox Application                  │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌──────────────┐    │
│  │ LoginActivity│  │RegisterActivity│ │ WebActivity │    │
│  └─────────────┘  └─────────────┘  └──────────────┘    │
│         │                 │                  │           │
│  ┌──────┴─────────────────┴──────────────────┴──────┐   │
│  │              BaseActivity (基类层)                │   │
│  │    • 生命周期管理 • Loading管理 • Toast封装      │   │
│  └──────────────────────────────────────────────────┘   │
│         │                     │                         │
│  ┌──────┴─────────────────────┴──────────┐              │
│  │          Manager Layer (管理层)        │              │
│  │  • AppUserManager   • NetworkManager  │              │
│  │  • JSBridgeManager  • AppConfig       │              │
│  └────────────────────────────────────────┘              │
│         │                     │                         │
│  ┌──────┴─────────────────────┴──────────┐              │
│  │          Network Layer (网络层)        │              │
│  │  • OkHttp • Interceptor • SSL配置     │              │
│  └────────────────────────────────────────┘              │
└─────────────────────────────────────────────────────────┘
```

### JS Bridge 架构

```
┌──────────────┐         ┌──────────────┐
│   H5 Page    │────────▶│  WebView     │
└──────────────┘         └──────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │  JSBridgeImpl    │
                       │  (接口注入)       │
                       └──────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │ JSBridgeManager  │
                       │  (安全校验+路由)  │
                       └──────────────────┘
                                │
                ┌───────────────┼───────────────┐
                ▼               ▼               ▼
         ┌──────────┐    ┌──────────┐   ┌──────────┐
         │ Device   │    │ UserInfo │   │ Camera   │
         │ Handler  │    │ Handler  │   │ Handler  │
         └──────────┘    └──────────┘   └──────────┘
```

---

## 🎯 功能特性

### 已实现功能

| 模块 | 功能 | 状态 |
|------|------|------|
| **用户模块** | 用户登录/注册 | ✅ |
| **用户模块** | Token 管理 (自动注入) | ✅ |
| **用户模块** | 用户信息持久化 | ✅ |
| **WebView** | 基础容器 (导航栏、返回栈) | ✅ |
| **WebView** | 页面加载进度管理 | ✅ |
| **WebView** | 混合内容安全控制 | ✅ |
| **JS Bridge** | 设备信息获取 | ✅ |
| **JS Bridge** | 用户信息同步 | ✅ |
| **JS Bridge** | 相机拍照 | ✅ |
| **JS Bridge** | 相册多选 | ✅ |
| **JS Bridge** | 图片 Base64 压缩 | ✅ |
| **网络层** | 统一异常处理 | ✅ |
| **网络层** | 请求/响应日志 | ✅ |
| **网络层** | SSL 证书验证 | ✅ |

---

## 🚀 快速开始

### 环境要求

- **JDK**: 11 或更高版本
- **Android Studio**: Hedgehog (2023.1.1) 或更高版本
- **Gradle**: 8.0 或更高版本
- **Android SDK**: API 29 (Android 10) 或更高版本

### 克隆项目

```bash
git clone https://github.com/your-username/WebBox.git
cd WebBox
```

### 配置环境

#### 1. 切换 API 环境

编辑 `app/src/main/java/com/sniper/webbox/config/AppConfig.kt`:

```kotlin
object H5Url {
    const val LOCAL_DEV = "http://localhost:5173"
    const val TEST = "http://app-test.sniper14.online"
    const val PROD = "https://app.sniper14.online"

    // 修改这里切换环境
    const val CURRENT = LOCAL_DEV  // 可选: LOCAL_DEV, TEST, PROD
}
```

#### 2. 配置网络安全 (开发环境)

确保 `app/src/main/res/xml/network_security_config.xml` 包含开发域名:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <!-- 本地开发 -->
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
        <!-- 测试环境 -->
        <domain includeSubdomains="true">app-test.sniper14.online</domain>
        <domain includeSubdomains="true">api-test.sniper14.online</domain>
    </domain-config>
</network-security-config>
```

#### 3. 同步依赖

```bash
./gradlew sync
```

### 运行应用

```bash
# 连接 Android 设备或启动模拟器
adb devices

# 安装并运行
./gradlew installDebug
```

---

## 📁 项目结构

```
app/src/main/java/com/sniper/webbox/
├── base/                      # 基础框架层
│   ├── activity/
│   │   └── BaseActivity.kt   # Activity基类: 生命周期、Loading、Toast
│   └── fragment/
│       └── BaseFragment.kt   # Fragment基类
│
├── network/                   # 网络层
│   ├── config/
│   │   └── NetworkConfig.kt  # 网络配置: 环境切换、超时、SSL
│   ├── manager/
│   │   └── NetworkManager.kt # OkHttp封装、拦截器管理
│   ├── api/
│   │   ├── ApiService.kt     # Retrofit API接口定义
│   │   ├── ApiRequest.kt     # 请求封装
│   │   └── ApiResponse.kt    # 响应封装
│   ├── exception/
│   │   └── NetworkException.kt
│   └── enums/
│       ├── RequestMethod.kt  # GET/POST/PUT/DELETE
│       └── ParamType.kt      # JSON/FORM/NONE
│
├── web/                       # H5容器层
│   ├── activity/
│   │   └── WebActivity.kt    # WebView容器: 页面加载、导航栏管理
│   └── bridge/               # JS Bridge桥接
│       ├── JSBridgeManager.kt     # 核心调度: 安全校验、路由
│       ├── JSBridgeImpl.kt        # 接口实现
│       ├── callback/
│       │   └── JSCallback.kt      # H5回调接口
│       └── handler/
│           ├── JSHandler.kt       # Handler接口定义
│           ├── DeviceHandler.kt   # 设备信息实现
│           ├── UserInfoHandler.kt # 用户信息实现
│           └── CameraHandler.kt   # 相机/相册实现
│
├── user/                      # 用户模块
│   ├── activity/
│   │   ├── LoginActivity.kt      # 登录页面
│   │   └── RegisterActivity.kt   # 注册页面
│   ├── manager/
│   │   └── AppUserManager.kt     # 用户状态管理: Token、用户信息
│   └── model/
│       ├── UserInfo.kt           # 用户信息模型
│       ├── LoginResponseData.kt  # 登录响应模型
│       └── RegisterResponseData.kt
│
├── config/                    # 应用配置
│   └── AppConfig.kt           # 环境配置: 本地/测试/生产
│
├── utils/                     # 工具类
│   └── AppUtil.kt
│
└── MainActivity.kt           # 主入口
```

---

## 🔌 核心模块说明

### 1. JS Bridge 使用指南

#### H5 调用原生方法

```javascript
// 调用设备信息接口
window.Android.callNative(
  "device.getDeviceInfo",  // 方法名: 模块.函数
  '{}',                     // 参数 (JSON字符串)
  Date.now().toString()    // 回调ID (时间戳)
);

// 回调函数
window.onNativeCallback = function(callbackId, code, msg, data) {
  console.log("Native callback:", { callbackId, code, msg, data });
};
```

#### 已支持的 JS Bridge API

| 模块 | 方法 | 参数 | 返回值 |
|------|------|------|--------|
| **device** | `getDeviceInfo` | - | `{ deviceModel, osVersion, appVersion }` |
| **userInfo** | `getUserInfo` | - | `{ token, tokenType, userInfo, isLoggedIn }` |
| **camera** | `takePhoto` | - | `base64` (图片字符串) |
| **camera** | `selectImage` | - | `base64[]` (图片数组) |
| **camera** | `showImagePickerDialog` | - | 弹出选择对话框 |

#### 示例代码

```javascript
// 获取用户信息
window.Android.callNative('userInfo.getUserInfo', '{}', Date.now().toString());

// 拍照
window.Android.callNative('camera.takePhoto', '{}', Date.now().toString());

// 从相册选择
window.Android.callNative('camera.selectImage', '{}', Date.now().toString());
```

### 2. 网络请求使用指南

#### 定义 API 接口

```kotlin
object UserApi {
    private const val BASE_PATH = "api/v1/users"

    suspend fun login(identifier: String, password: String): LoginResponseData {
        val params = mapOf(
            "identifier" to identifier,
            "password" to password
        )

        val request = object : ApiRequest {
            override fun getApiPath() = "$BASE_PATH/login"
            override fun getRequestMethod() = RequestMethod.POST
            override fun getParams() = params
            override fun getParamType() = ParamType.MAP
            override fun needAuth() = false
        }

        val responseJson = NetworkManager.execute(request)
        val gson = Gson()
        val response = gson.fromJson(responseJson, ApiResponse::class.java)

        if (response.code != "000000") {
            throw NetworkException(response.code, response.msg)
        }

        return gson.fromJson(gson.toJson(response.data), LoginResponseData::class.java)
    }
}
```

#### 在 Activity 中使用

```kotlin
lifecycleScope.launch {
    try {
        val result = UserApi.login("user@example.com", "password")
        // 保存 Token
        AppUserManager.saveAuthInfo(result.access_token, result.token_type)
        showShortToast("登录成功")
    } catch (e: NetworkException) {
        showShortToast("登录失败: ${e.message}")
    } catch (e: Exception) {
        showShortToast("网络错误: ${e.message}")
    }
}
```

### 3. 用户状态管理

```kotlin
// 初始化 (在 Application.onCreate 中)
AppUserManager.init(context)

// 保存登录信息
AppUserManager.saveAuthInfo(accessToken, "bearer")

// 保存用户详细信息
AppUserManager.saveUserInfo(userInfo)

// 检查登录状态
if (AppUserManager.isLoggedIn()) {
    // 已登录
}

// 获取 Token (自动注入到请求头)
val token = AppUserManager.getFullAuthToken() // "bearer xxx"

// 退出登录
AppUserManager.logout()
```

---

## 🔐 安全机制

### JS Bridge 安全防护

1. **白名单机制** - 只允许预定义的模块被调用
2. **方法名校验** - 正则验证方法名格式 (`^[a-zA-Z0-9_.]+$`)
3. **长度限制** - 方法名最大 100 字符
4. **主线程保护** - 所有回调在主线程执行

### WebView 安全配置

```kotlin
// 禁用文件访问
webSettings.allowFileAccess = false
webSettings.allowFileAccessFromFileURLs = false
webSettings.allowUniversalAccessFromFileURLs = false

// 混合内容策略
webSettings.mixedContentMode = if (开发环境) {
    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
} else {
    WebSettings.MIXED_CONTENT_NEVER_ALLOW
}
```

### 网络层安全

- ✅ 生产环境强制 SSL 证书验证
- ✅ Token 自动注入 (Bearer 认证)
- ✅ 日志脱敏 (Token 显示为 `Bearer ***`)
- ⚠️ 开发环境可选 SSL 跳过 (需明确配置)

---

## 🔧 配置说明

### 环境切换

在 `AppConfig.kt` 中修改:

```kotlin
const val CURRENT = H5Url.TEST  // 切换到测试环境
```

API 地址会自动匹配对应环境的后端服务。

### 网络配置

在 `NetworkConfig.kt` 中可调整:

```kotlin
var connectTimeout: Long = 30000      // 连接超时 (毫秒)
var readTimeout: Long = 30000         // 读取超时
var writeTimeout: Long = 30000        // 写入超时
var logEnable: Boolean = true         // 是否启用日志
var ignoreSSLVerify: Boolean = false // 是否忽略SSL (仅测试环境)
```

---

## 📱 常见问题

### 1. 模拟器无法访问后端服务?

使用 `10.0.2.2` 代替 `localhost`:

```kotlin
const val LOCAL_DEV = "http://10.0.2.2:8000"
```

### 2. WebView 加载 H5 页面白屏?

检查网络安全配置:
- 确认 `network_security_config.xml` 包含域名
- 生产环境必须使用 HTTPS
- 检查 `AndroidManifest.xml` 的 `usesCleartextTraffic`

### 3. JS Bridge 调用无响应?

确认:
- 方法名格式正确: `模块名.函数名`
- 模块在白名单中
- WebView 已注入接口: `addJavascriptInterface(jsBridgeImpl, "Android")`

### 4. 相机权限被拒绝?

检查:
- `AndroidManifest.xml` 已声明 `CAMERA` 权限
- Android 6+ 需要运行时请求权限 (已封装)
- 模拟器无摄像头, 使用真机测试

---

## 🤝 贡献指南

欢迎贡献代码! 请遵循以下规范:

1. **代码风格**: 遵循 [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
2. **提交规范**: 使用语义化提交信息 (Conventional Commits)
3. **Pull Request**: 提交 PR 前请确保:
   - ✅ 代码通过编译
   - ✅ 添加必要的注释
   - ✅ 更新相关文档

---

## 📄 许可证

```
MIT License

Copyright (c) 2025 WebBox

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 📞 联系方式

- **作者**: WebBox Team
- **邮箱**: support@webbox.com
- **官网**: https://webbox.com

---

<div align="center">

**如果这个项目对你有帮助, 请给个 ⭐️ Star 支持一下!**

Made with ❤️ by WebBox Team

</div>
