package tv.av.sniffer.headers

import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

/**
 *  ftyp box(文件头 only one) = ftyp(Box) + moov(Movie Box) + mdat(Media Data Box) + free(Box)

BOX = header + body
box body：box的数据，比如mdat中body部分存储的媒体数据。
box header：box的元数据
header = size(4byte 必有) + type(4byte 必有) + largeSize(8byte size==0时存在) + version(1byte 存在即为FULL BOX) + flags(3bit 存在即为FULL BOX)
如果size等于0：则存在 largeSize 8byte
如果size等于1：当前box为文件的最后一个box，通常包含在mdat box中
如果type==uuid，则表示是自定义扩展类型 size或largesize随后的16字节，为自定义类型的值（extended_type）
FullBox主要在moov中的box用到, 如moov.mvhd

ftyp(规范,一般在文件的开头处（只有固定大小的文件签名可以在其前面, 兼容早期允许不包含ftyp box。):
major brand(32bit字符) + minor version(32bit整数) + compatible brands[](32bit字符数组)
如果没有ftyp: ftyp:major_brand mp41，minor_version 0，compatible_brands mp41

major_brand isom(不能直接使用该规范)、mp41、mp42、avc1、qt等, 表示用哪种格式来解析当前的文件

minor_version：major_brand 的说明信息, 版本号等

compatible_brands：兼容的brand列表, 比如 mp41 兼容 isom。

isom(ISO Base Media file): MPEG-4Part12中一种基础文件格式, MP4/3gp/QT 等封装格式都基于这种基础文件格式衍生
MP4遵循的规范 mp41/mp42 都基于isom衍生
isom不能做为major_brand, 需要使用具体的brand 如mp41, isom没有定义具体的 文件扩展名/mime type

Brand	        Extension    Mime Type
MP4	        mp41、mp42	    .mp4         video/mp4,audio/mp4,application/mp4
3GPP	    various,e.g     .3gp         video/3gpp,audio/3gpp
3GPP2	    3g2a	        .3g2	     video/3gpp2,audio/3gpp2
quickTime	“qt”	        .mov         video/quicktime

 *  moov box(MP4信息 only one): 必须包含以下三种Atom中的一种：mvhd/cmov/rmra

mvhd box：Movie Header Atom, 存放未压缩过的影片信息的头容器
creation_time: 文件创建时间
modification_time: 文件修改时间
timescale: 一秒包含的时间单位(整数) 如timescale=1000, track.duration=10000, track实际时长=10000/1000=10s
duration：影片时长(整数), 等于时间最长的track的duration；
rate：推荐的播放速率，32bit整数，高16位/低16位分别代表 整数/小数(16.16), 0x0001 0000代表1.0, 正常播放速度
volume：播放音量, 16bit整数, 高8位/低8位分别代表 整数/小数(8.8), 0x01 00表示1.0, 最大音量
matrix：视频转换矩阵
next_track_ID：32bit整数, 非0, 一般忽略, 若添加新track到影片时, 可使用的track id, 大于所有已用id

cmov box：Compressed Movie Atom, 压缩过的电影信息容器(不常用)

rmra box：Reference Movie Atom, 参考电影信息容器(不常用)

trak box: 轨道相关信息, 必须包含 tkhd box + mdia box
tkhd: track header box
mdia: track媒体类型 sample信息 sample数据 = mdhd(MediaHeader) + minf(MediaInfo) + hdlr(HandlerReference)
.......................
......................

udta box: 用户自定义信息

 *  mdat box(媒体数据)
 */
class MP4Header : AVHeader() {

    override fun mediaType(): String {
        return "mp4"
    }

    override fun peekLength(): Int {
        return 4 + 4 //size + ftyp
    }

    override fun header(): String {
        return "ftyp"
    }

    override fun check(len: Int, bytes: ByteArray): Boolean {
        try {
            if (len != peekLength()) return false
            val byteBuffer = ByteBuffer.wrap(bytes)
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
//            val ftSize = ByteBuffer.wrap(byteBuffer.array().copyOfRange(0, 4)).int
            val offset = 4
            return header().toByteArray().contentEquals(
                byteBuffer.array().copyOfRange(offset, offset + header().length)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}