package write;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import server.ProtoMsg;

import java.nio.charset.Charset;

public class ProtoMsgEncoder extends MessageToByteEncoder<ProtoMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg protoMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(protoMsg.getMsgLength());
        byteBuf.writeShort(protoMsg.getMsgType());
        byteBuf.writeCharSequence(protoMsg.getMsg(), Charset.forName("utf-8"));
    }
}
