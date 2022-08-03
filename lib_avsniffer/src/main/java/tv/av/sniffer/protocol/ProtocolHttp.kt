package tv.av.sniffer.protocol

import okhttp3.*
import java.io.InputStream

/**
 * by okhttp
 */
class ProtocolHttp : Protocol() {

    companion object {
        private var Client: OkHttpClient? = null
        private const val BUFFER_MAX_SIZE = 32 * 4
    }

    override fun readBytes(url: String, checker: (Int, ByteArray?) -> Boolean) {
        var response: Response? = null
        var inputStream: InputStream? = null
        try {
            if (Client == null) Client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            response = Client?.newCall(request)?.execute()
            if (response?.code != 200) throw Exception("http code != 200")
            inputStream = response.body?.byteStream()
            inputStream ?: throw Exception("response.body.byteStream() == null")
            var index = 0
            val bytes = ByteArray(BUFFER_MAX_SIZE)
            var interrupt = false
            while (!interrupt) {
                //read bytes
                if (index >= BUFFER_MAX_SIZE) throw Exception("OVER BUFFER_MAX_SIZE")
                val c = inputStream.read()
                if (c == -1) throw Exception("inputStream.read() return -1")
                bytes[index] = c.toByte()
                index++
                //response
                interrupt = checker.invoke(index, bytes)
            }
        } catch (e: Exception) {
            checker.invoke(-1, null)
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                response?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}