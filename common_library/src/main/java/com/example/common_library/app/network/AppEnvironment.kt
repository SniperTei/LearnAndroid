package com.example.common_library.app.network

enum class AppEnv {
    DEV,
    TEST,
    PRD
}
class AppEnvironment {
    companion object {
        private const val DEV_URL = "https://www.wanandroid.com/"
        private const val TEST_URL = "https://www.wanandroid.com/"
        private const val PRD_URL = "https://www.wanandroid.com/"

        private var currentEnv: AppEnv = AppEnv.PRD

        fun setEnv(env: AppEnv) {
            currentEnv = env
        }

//        fun getEnv(): AppEnv {
//            return currentEnv
//        }

        fun getBaseURL(): String {
            return when (currentEnv) {
                AppEnv.DEV -> {
                    DEV_URL
                }

                AppEnv.TEST -> {
                    TEST_URL
                }

                AppEnv.PRD -> {
                    PRD_URL
                }
            }
        }
    }
}