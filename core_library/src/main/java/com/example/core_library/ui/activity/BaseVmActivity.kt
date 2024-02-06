package com.example.core_library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.common_library.app.base.viewmodel.BaseViewModel
import com.example.common_library.ext.getVmClazz

abstract class BaseVmActivity<VM: BaseViewModel>: AppCompatActivity() {
    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun startLoading(message: String = "正在加载中...")

    abstract fun stopLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
//        NetworkStateManager.instance.mNetworkStateCallback.observeInActivity(this, Observer {
//            onNetworkStateChanged(it)
//        })
    }

    /**
     * 网络变化监听 子类重写
     */
//    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
//        mViewModel.loadingChange.startLoading.observeInActivity(this, Observer {
//            showLoading(it)
//        })
//        //关闭弹窗
//        mViewModel.loadingChange.stopLoading.observeInActivity(this, Observer {
//            dismissLoading()
//        })
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
//    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
//        viewModels.forEach { viewModel ->
//            //显示弹窗
//            viewModel.loadingChange.startLoading.observeInActivity(this, Observer {
//                showLoading(it)
//            })
//            //关闭弹窗
//            viewModel.loadingChange.stopLoading.observeInActivity(this, Observer {
//                dismissLoading()
//            })
//        }
//    }
}