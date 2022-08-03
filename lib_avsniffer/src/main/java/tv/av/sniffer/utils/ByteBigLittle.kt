package tv.av.sniffer.utils

object ByteBigLittle {

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     *
     * @param  byteBig[]
     * @return byte[]
     */
    fun byteBig2ByteLittle(byteBig: ByteArray): ByteArray? {
        val b = ByteArray(4)
        b[0] = byteBig[3]
        b[1] = byteBig[2]
        b[2] = byteBig[1]
        b[3] = byteBig[0]
        return b
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     *
     * @param n int
     * @return byte[]
     */
    fun int2ByteLittle(n: Int): ByteArray? {
        val b = ByteArray(4)
        b[0] = (n and 0xff).toByte()
        b[1] = (n shr 8 and 0xff).toByte()
        b[2] = (n shr 16 and 0xff).toByte()
        b[3] = (n shr 24 and 0xff).toByte()
        return b
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n int
     * @return byte[]
     */
    fun int2ByteBig(n: Int): ByteArray? {
        val b = ByteArray(4)
        b[3] = (n and 0xff).toByte()
        b[2] = (n shr 8 and 0xff).toByte()
        b[1] = (n shr 16 and 0xff).toByte()
        b[0] = (n shr 24 and 0xff).toByte()
        return b
    }
}