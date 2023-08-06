package write;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.ProtoMsg;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsg protoMsg = (ProtoMsg)msg;
        String msgStr = protoMsg.getMsg();
        int msgLength = protoMsg.getMsgLength();
        short msgType = protoMsg.getMsgType();
//        System.out.println("数据-》"+msgStr);
//        System.out.println("长度-》"+msgLength);
//        System.out.println("类型-》"+msgType);
//        System.out.println("------------------");
    }
}
