package tv.av.sniffer.headers

/**
 * EBML头
1A 45 DF A3
 */
class MKVHeader : AVHeader() {

    override fun mediaType(): String {
        return "mkv"
    }

    override fun peekLength(): Int {
        return 4 //1A 45 DF A3
    }

    override fun header(): String {
        return (0x1A45DFA3).toString() //1A 45 DF A3
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