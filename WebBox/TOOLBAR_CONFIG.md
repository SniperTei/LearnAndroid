# 🔧 导航栏显示/隐藏配置说明

## ✅ 已完成配置

WebBox 应用现在支持通过配置文件控制导航栏的显示和隐藏，**默认隐藏**。

---

## 📋 配置方式

### 方式 1: 修改容器配置文件（推荐）

**文件**: `app/src/main/assets/container_config.json`

```json
{
  "theme": {
    "showToolbar": false,  // false = 隐藏导航栏，true = 显示导航栏
    "showBackButton": true,
    "showCloseButton": false
  }
}
```

**配置说明**：
- `showToolbar`: 控制整个导航栏的显示/隐藏
  - `false`: 隐藏导航栏（默认），H5 页面全屏显示
  - `true`: 显示导航栏
- `showBackButton`: 是否显示返回按钮（仅在 `showToolbar: true` 时生效）
- `showCloseButton`: 是否显示关闭按钮（仅在 `showToolbar: true` 时生效）

### 方式 2: 通过 Intent 传递参数

如果从其他 Activity 启动 WebActivity：

```kotlin
val intent = Intent(context, WebActivity::class.java).apply {
    putExtra(WebActivity.EXTRA_URL, "https://example.com")
    putExtra(WebActivity.EXTRA_TITLE, "页面标题")
    putExtra(WebActivity.EXTRA_SHOW_TOOLBAR, false)  // false = 隐藏导航栏
}
context.startActivity(intent)
```

---

## 🎯 默认行为

### ✅ 已修改的默认值

1. **配置文件默认值** (`ContainerConfig.kt`):
   ```kotlin
   val showToolbar: Boolean = false  // 默认隐藏
   ```

2. **Intent 默认值** (`WebActivity.kt`):
   ```kotlin
   showToolbar = intent.getBooleanExtra(EXTRA_SHOW_TOOLBAR, false)  // 默认隐藏
   ```

3. **当前配置文件** (`container_config.json`):
   ```json
   {
     "theme": {
       "showToolbar": false
     }
   }
   ```

---

## 🚀 使用场景

### 场景 1: 全屏 H5 应用（推荐）

```json
{
  "theme": {
    "showToolbar": false
  }
}
```

**效果**：
- ✅ H5 页面全屏显示
- ✅ 最大化内容区域
- ✅ 类似原生应用体验

### 场景 2: 带导航栏的 H5 应用

```json
{
  "theme": {
    "showToolbar": true,
    "showBackButton": true,
    "showCloseButton": false
  }
}
```

**效果**：
- ✅ 顶部显示导航栏
- ✅ 显示返回按钮
- ✅ 显示页面标题
- ✅ 适合需要导航功能的场景

### 场景 3: 不同页面不同配置

```kotlin
// 首页：全屏显示
startWebActivity(context, url = "home", showToolbar = false)

// 详情页：带导航栏
startWebActivity(context, url = "detail", showToolbar = true)
```

---

## 📱 配置优先级

从高到低：

1. **Intent 参数** (`EXTRA_SHOW_TOOLBAR`) - 最高优先级
2. **配置文件** (`container_config.json` 中的 `theme.showToolbar`)
3. **代码默认值** (`false`)

---

## 🔄 修改配置后重新编译

### 1. 修改配置文件

```bash
vim app/src/main/assets/container_config.json
# 修改 "showToolbar": false/true
```

### 2. 清理并重新编译

```bash
cd /Users/zhengnan/Sniper/Developer/github/LearnAndroid/WebBox

./gradlew clean
./gradlew assembleDebug
./gradlew installDebug
```

### 3. 验证效果

```bash
# 安装后打开应用，查看导航栏是否按预期显示/隐藏
adb shell pm list packages | grep webbox
```

---

## 🎨 导航栏样式配置

即使隐藏导航栏，也可以配置其他样式：

```json
{
  "theme": {
    "showToolbar": false,  // 隐藏导航栏
    "statusBarColor": "#FFE91E63",  // 状态栏颜色（仍然生效）
    "toolbarColor": "#FFE91E63",  // 导航栏颜色（如果显示）
    "toolbarTitleColor": "#FFFFFFFF"  // 标题颜色（如果显示）
  }
}
```

---

## 💡 H5 页面导航建议

如果隐藏了原生导航栏，H5 页面需要自己实现导航功能：

### JavaScript 返回示例

```javascript
// 使用 JS Bridge 返回
function goBack() {
  if (window.Android && window.Android.callNative) {
    window.Android.callNative(
      'webview.goBack',  // 方法
      '{}',               // 参数
      Date.now().toString()  // callbackId
    );
  }
}
```

### H5 自定义导航栏

```html
<div class="h5-header">
  <button onclick="goBack()">返回</button>
  <h1>页面标题</h1>
</div>
```

---

## ✅ 配置完成

**当前配置**：
- ✅ 导航栏默认隐藏
- ✅ 配置文件已更新
- ✅ 代码默认值已修改
- ✅ WebActivity 正确应用配置

**测试步骤**：
```bash
./gradlew clean
./gradlew installDebug
# 打开应用，验证导航栏已隐藏
```

---

## 📝 相关文件

| 文件 | 修改内容 |
|------|---------|
| `app/src/main/assets/container_config.json` | `showToolbar: false` |
| `app/src/main/java/.../ContainerConfig.kt` | 默认值改为 `false` |
| `app/src/main/java/.../WebActivity.kt` | Intent 默认值改为 `false` |

---

**配置完成！导航栏现在默认隐藏。** 🎉
