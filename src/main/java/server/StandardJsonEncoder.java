package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName：StandardJsonEncoder
 * @Description：标准协议的编码器
 */
public class StandardJsonEncoder extends MessageToByteEncoder {

    private String MSG_MARKER = "$";

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf out) throws Exception {

    }
}
