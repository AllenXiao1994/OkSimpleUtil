package com.gateoftruth.oklibrary

import okhttp3.Call
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile


abstract class FileResultCallBack(private val isMainThread: Boolean = true) :
    ResultCallBack() {


    override fun start() {

    }


    override fun response(call: Call, response: Response) {
        val responseBody = response.body
        if (responseBody == null) {
            if (isMainThread) {
                OkSimple.mainHandler.post {
                    responseBodyGetNull(call, response)
                }
            } else {
                responseBodyGetNull(call, response)
            }
            return
        }
        try {
            val url = response.request.url.toString()
            val bean = urlToBeanMap[url] ?: DownloadBean()
            val inputStream = responseBody.byteStream()
            val file = File(bean.filePath, bean.filename)
            val randomAccessFile = RandomAccessFile(file, "rw")
            randomAccessFile.seek(bean.downloadLength)
            inputStream.use { input ->
                var bytesCopied: Long = bean.downloadLength
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytes = input.read(buffer)
                while (bytes >= 0) {
                    randomAccessFile.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = input.read(buffer)
                    downloadProgress(url, bean.contentLength, bytesCopied)
                }
            }
            randomAccessFile.close()
            if (isMainThread) {
                OkSimple.mainHandler.post {
                    returnTheFile(file, call, response)
                }
            } else {
                returnTheFile(file, call, response)
            }

        } catch (e: Exception) {
            if (isMainThread) {
                OkSimple.mainHandler.post {
                    otherException(call, response, e)
                }
            } else {
                otherException(call, response, e)
            }

        }
    }

    open fun returnTheFile(file: File, call: Call, response: Response) {
        if (!file.exists() || file.length() == 0L) {
            otherException(call, response, IOException("File Download Failure"))
        } else {
            finish(file)
        }
    }

    override fun otherException(call: Call, response: Response, e: Exception) {
        failure(call, e)
    }

    override fun responseBodyGetNull(call: Call, response: Response) {

    }

    override fun downloadProgress(url: String, total: Long, current: Long) {
        OkSimple.mainHandler.post {
            downloadProgressOnMainThread(url, total, current)
        }
    }

    override fun uploadProgress(fileName: String, total: Long, current: Long) {

    }

    override fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long) {

    }

    abstract fun finish(file: File)


}