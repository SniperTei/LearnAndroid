/**
 * JS Bridge 工具类
 * 用于JS与原生App之间的通信
 */
(function(window) {
    var jsBridge = {
        // 唯一标识，用于生成回调ID
        uniqueId: 1,
        // 存储回调函数
        callbacks: {},
        
        /**
         * 调用原生方法
         * @param {string} method - 方法名，格式为'模块名.方法名'
         * @param {Object} params - 参数对象
         * @param {Function} successCallback - 成功回调函数
         * @param {Function} errorCallback - 失败回调函数
         */
        call: function(method, params, successCallback, errorCallback) {
            // 生成唯一的回调ID
            var callbackId = 'cb_' + this.uniqueId++;
            
            // 存储回调函数
            this.callbacks[callbackId] = {
                success: successCallback,
                error: errorCallback
            };
            
            // 构造调用数据
            var data = {
                method: method,
                params: params || {},
                callbackId: callbackId
            };
            
            // 调用原生方法
            try {
                // 通过Android接口调用原生方法
                window.Android.callNative(JSON.stringify(data));
                log('调用原生方法: ' + method);
            } catch (e) {
                log('调用原生方法失败: ' + e.message, 'error');
                // 如果调用失败，直接执行错误回调
                if (errorCallback) {
                    errorCallback({code: '900001', msg: '调用原生方法失败: ' + e.message});
                }
            }
        },
        
        /**
         * 处理原生回调
         * @param {string} callbackId - 回调ID
         * @param {string} status - 状态，'success'或'error'
         * @param {Object} result - 回调结果
         */
        handleCallback: function(callbackId, status, result) {
            var callback = this.callbacks[callbackId];
            if (callback) {
                try {
                    if (status === 'success' && callback.success) {
                        callback.success(result);
                    } else if (status === 'error' && callback.error) {
                        callback.error(result);
                    }
                } catch (e) {
                    log('执行回调失败: ' + e.message, 'error');
                }
                // 执行完回调后删除
                delete this.callbacks[callbackId];
            }
        }
    };
    
    /**
     * 日志输出函数
     * @param {string} message - 日志消息
     * @param {string} type - 日志类型，'info'(默认)或'error'
     */
    function log(message, type) {
        type = type || 'info';
        
        // 创建日志项
        var logItem = document.createElement('div');
        logItem.className = 'log-item ' + type;
        
        // 添加时间戳
        var timestamp = new Date().toLocaleTimeString();
        logItem.textContent = '[' + timestamp + '] ' + message;
        
        // 添加到日志容器
        var logContainer = document.getElementById('logContainer');
        if (logContainer) {
            logContainer.appendChild(logItem);
            // 滚动到底部
            logContainer.scrollTop = logContainer.scrollHeight;
        }
        
        // 同时输出到控制台
        if (type === 'error') {
            console.error(message);
        } else {
            console.log(message);
        }
    }
    
    // 暴露全局方法，供原生调用
    window.jsBridge = jsBridge;
    window.log = log;
    
    // 初始化日志
    log('JS Bridge 初始化完成');
})(window);

/**
 * 原生回调入口函数
 * 此函数将被原生代码调用
 */
function onNativeCallback(callbackId, status, resultJson) {
    try {
        var result = JSON.parse(resultJson);
        jsBridge.handleCallback(callbackId, status, result);
    } catch (e) {
        log('解析回调数据失败: ' + e.message, 'error');
    }
}