package tv.av.sniffer.protocol

abstract class Protocol {
    abstract fun readBytes(url: String, checker: ((Int, ByteArray?) -> Boolean))
}