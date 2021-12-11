package com.gateoftruth.oklibrary

import android.util.Log
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response

abstract class ResultCallBack : BaseProgressListener {

    val urlToBeanMap = hashMapOf<String, DownloadBean>()

    abstract fun start()

    abstract fun response(call: Call, response: Response)

    abstract fun failure(call: Call, e: Exception)

    abstract fun responseBodyGetNull(call: Call, response: Response)

    abstract fun otherException(call: Call, response: Response, e: Exception)

    open fun requestAbandon(request: Request, requestObject: RequestObject) {
        Log.e(
            OkSimpleConstant.OKSIMPLE_TAG,
            "Same Request!!! This request has been abandoned!!!,detail:$requestObject"
        )
    }

}