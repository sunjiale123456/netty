package write;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import server.ProtoMsg;

import java.nio.charset.Charset;
import java.util.List;

public class ProtoMsgDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readInt();
        short msgType = byteBuf.readShort();
        String msg = (String) byteBuf.readCharSequence(length, Charset.forName("utf-8"));
        ProtoMsg protoMsg = new ProtoMsg();
        protoMsg.setMsg(msg);
        protoMsg.setMsgType(msgType);
        protoMsg.setMsgLength(length);

        list.add(protoMsg);
    }
}
