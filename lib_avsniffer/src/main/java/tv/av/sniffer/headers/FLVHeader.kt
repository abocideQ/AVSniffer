package tv.av.sniffer.headers

/**
 *  flv = [header, metadata tag, PreviousTagSize n, tag n+1, .....]

 *  header = F + L + V + version + TypeFlags + TypeFlagsAudio + TypeFlagsReserved + TypeFlagsVideo + DataOffset
version : version
TypeFlags : 应该为0
TypeFlagsAudio : 1 = 有音频
TypeFlagsReserved : 应该为0
TypeFlagsVideo : 1 = 有视频
DataOffset : 头文件长度

 *  PreviousTagSize(4字节长度) : 表示前一个tag长度 = tag header size(11个字节) + tag_header_data size
tag = [audio, video, ...]
 */
class FLVHeader : AVHeader() {

    override fun mediaType(): String {
        return "flv"
    }

    override fun peekLength(): Int {
        return 3 //FLV
    }

    override fun header(): String {
        return "FLV"
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