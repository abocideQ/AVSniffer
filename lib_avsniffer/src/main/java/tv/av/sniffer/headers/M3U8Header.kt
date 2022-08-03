package tv.av.sniffer.headers

/**
 *  #EXTM3U          头部
#EXT-X-VERSION:7 版本
#EXT-X-INDEPENDENT-SEGMENTS

 *  多码率
#EXTM3U
#EXT-X-STREAM-INF: PROGRAM-ID=1, BANDWIDTH=1280000
http://example.com/low.m3u8
#EXT-X-STREAM-INF: PROGRAM-ID=1, BANDWIDTH=2560000
http://example.com/mid.m3u8

 *  单码率
#EXTM3U
#EXT-X-TARGETDURATION: 3000
#EXTINF: 3000,
http://example.com/.ts
#EXT-X-ENDLIST
 */
class M3U8Header : AVHeader() {

    override fun mediaType(): String {
        return "m3u8"
    }

    override fun peekLength(): Int {
        return 7 //#EXTM3U
    }

    override fun header(): String {
        return "#EXTM3U"
    }

    override fun check(len: Int, bytes: ByteArray): Boolean {
        try {
            if (len != peekLength()) return false
            return header().toByteArray().contentEquals(
                bytes.copyOfRange(0, header().length)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}