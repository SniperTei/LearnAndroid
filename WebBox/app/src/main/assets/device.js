/**
 * 设备检测与原生交互工具
 * 用于检测设备类型并提供调用原生代码的接口
 * 适配Vue3项目直接引入使用
 */

class DeviceBridge {
  constructor() {
    this.isIOS = false;
    this.isAndroid = false;
    this.isWeb = false;
    this.setup();
  }

  /**
   * 初始化设备检测
   */
  setup() {
    const ua = navigator.userAgent.toLowerCase();
    this.isIOS = /iphone|ipad|ipod/.test(ua);
    this.isAndroid = /android/.test(ua);
    this.isWeb = !this.isIOS && !this.isAndroid;

    console.log('设备类型检测:', {
      isIOS: this.isIOS,
      isAndroid: this.isAndroid,
      isWeb: this.isWeb
    });
  }

  /**
   * 调用原生方法
   * @param {string} method - 方法名
   * @param {object} params - 参数对象
   * @param {function} callback - 回调函数
   */
  callNative(method, params = {}, callback = null) {
    if (this.isWeb) {
      console.log('Web环境，跳过原生调用:', { method, params });
      return;
    }

    // 生成唯一回调ID
    const callbackId = callback ? `callback_${Date.now()}_${Math.random().toString(36).substr(2, 9)}` : null;

    if (callbackId && callback) {
      console.log('注册回调:', { callbackId });
      // 注册回调函数
      window[callbackId] = (result) => {
        console.log('收到原生回调:', { callbackId, result });
        callback(result);
        // 清理回调函数
        setTimeout(() => {
          delete window[callbackId];
        }, 100);
      };
    }

    if (this.isIOS) {
      this._callIOS(method, params, callbackId);
    } else if (this.isAndroid) {
      this._callAndroid(method, params, callbackId);
    }
  }

  /**
   * 调用iOS原生方法
   * @private
   */
  _callIOS(method, params, callbackId) {
    try {
      // iOS通过WKWebView的messageHandlers调用原生
      if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.nativeBridge) {
        const callParams = {
          method,
          params,
          callbackId
        };
        window.webkit.messageHandlers.nativeBridge.postMessage(callParams);
      } else {
        console.error('iOS原生桥接未注册');
      }
    } catch (error) {
      console.error('调用iOS原生方法失败:', error);
    }
  }

  /**
   * 调用Android原生方法
   * @private
   */
  _callAndroid(method, params, callbackId) {
    try {
      // Android通过addJavascriptInterface注入的对象调用原生
      if (window.Android && window.Android.callNative) {
        console.log('调用Android原生方法:', { method, params, callbackId });
        // 直接调用Android.callNative，参数顺序为method, params, callbackId
        window.Android.callNative(method, JSON.stringify(params), callbackId);
        console.log('Android原生方法调用成功');
      } else {
        console.error('Android原生桥接未注册或方法不存在');
      }
    } catch (error) {
      console.error('调用Android原生方法失败:', error);
    }
  }

  /**
   * 通用调用方法 - 支持Promise
   * @param {string} method - 方法名，支持"模块名.方法名"格式
   * @param {object} params - 参数对象
   * @returns {Promise} 返回Promise对象
   */
  call(method, params = {}) {
    return new Promise((resolve, reject) => {
      this.callNative(method, params, (result) => {
        if (result && result.code === '000000' || result && result.success) {
          resolve(result.data || result);
        } else {
          reject(new Error(result.message || '调用失败'));
        }
      });
    });
  }

  /**
   * 获取设备信息
   * @returns {Promise} 返回Promise对象
   */
  async getDeviceInfo() {
    return this.call('device.getDeviceInfo', {});
  }

  /**
   * 执行耗时操作
   * @returns {Promise} 返回Promise对象
   */
  async doSomethingCostVeryLongTime() {
    return this.call('device.doSomethingCostVeryLongTime', {});
  }

  /**
   * 显示Toast提示
   * @param {string} message - 提示消息
   * @param {number} duration - 显示时长（毫秒）
   */
  showToast(message, duration = 2000) {
    this.callNative('showToast', { message, duration });
  }

  /**
   * 结束当前页面
   */
  finishActivity() {
    this.callNative('finishActivity', {});
  }
}

// 创建单例实例
const deviceBridge = new DeviceBridge();

// 同时支持ES模块和CommonJS
if (typeof module !== 'undefined' && module.exports) {
  module.exports = deviceBridge;
}

if (typeof window !== 'undefined') {
  // 挂载到window对象，方便非模块化环境使用
  window.deviceBridge = deviceBridge;
}

// ES模块导出
try {
  module.exports = deviceBridge;
} catch (e) {
  // 忽略CommonJS导出错误
}

export default deviceBridge;

// 供原生调用的全局方法，用于处理回调
window.nativeCallback = function(callbackId, result) {
  if (window[callbackId] && typeof window[callbackId] === 'function') {
    try {
      // 尝试解析JSON字符串（如果result是字符串）
      const parsedResult = typeof result === 'string' ? JSON.parse(result) : result;
      window[callbackId](parsedResult);
    } catch (e) {
      console.error('处理原生回调时出错:', e);
      // 如果解析失败，直接传递原始结果
      window[callbackId](result);
    }
  }
};