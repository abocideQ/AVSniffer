package tv.av.sniffer.headers

import android.media.MediaFormat

/**
 * RIFF + LIST + 普通快

 * RIFF 块
Chunk ID （字符串"RIFF"）
Chunk  len (RIFF数据长度)
Chunk  type  （形式类型 或者 列表类型 。如： “AVI”,”WAVE” ……. ）
Chunk data  (RIFF 实际数据)

 * LIST 块
Chunk ID  (字符串“LIST”)
Chunk  len (LIST数据长度)
Chunk  type (形式类型或者列表类型，如 hdrl 、strl、INFO、movi)
Chunk  data (LIST 实际数据)

 * 普通块(avih strh strf JUNK 、idxl)
Chunk ID  (字符串)
Chunk  len (数据长度)
Chunk  data (数据)
 */
class AVIHeader : AVHeader() {

    override fun mediaType(): String {
        return "avi"
    }

    override fun peekLength(): Int {
        return 4 //RIFF
    }

    override fun header(): String {
        return "RIFF"
    }

    override fun check(len: Int, bytes: ByteArray): Boolean {
        try {
            if (len != peekLength()) return false
            return header().toByteArray().contentEquals(
                bytes.copyOfRange(0, header().length)
            )
        } catch (e: Exception) {
            return false
        }
    }
}