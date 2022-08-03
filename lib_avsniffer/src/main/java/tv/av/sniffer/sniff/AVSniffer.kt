package tv.av.sniffer.sniff

import tv.av.sniffer.headers.*
import tv.av.sniffer.protocol.Protocol
import tv.av.sniffer.protocol.ProtocolHttp

class AVSniffer(
    url: String,
    headers: ArrayList<AVHeader>? = null,
    protocol: Protocol? = null,
    callback: (String?) -> Unit,
) {

    private var mUrl = url
    private var mCallback: (String?) -> Unit = callback
    private val mHeaders = arrayListOf<AVHeader>()
    private var mProtocol: Protocol? = protocol

    init {
        if (protocol == null) mProtocol = ProtocolHttp()
        mHeaders.clear()
        mHeaders.addAll(headers ?: arrayListOf())
        mHeaders.add(MP4Header())
        mHeaders.add(M3U8Header())
        mHeaders.add(FLVHeader())
        mHeaders.add(AVIHeader())
        mHeaders.add(MKVHeader())
    }

    fun sniff() {
        Thread {
            mProtocol?.readBytes(mUrl) { len, bytes ->
                try {
                    var continueRead = false
                    if (len < 0 || bytes == null) return@readBytes continueRead
                    mHeaders.forEach headers@{ header ->
                        if (!header.check(len, bytes)) return@headers
                        continueRead = true
                        mCallback.invoke(header.mediaType())
                    }
                    return@readBytes continueRead
                } catch (e: Exception) {
                    mCallback.invoke(null)
                    e.printStackTrace()
                    return@readBytes true
                }
            }
        }.start()
    }
}