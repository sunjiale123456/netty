package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName：StandardJsonEncoder
 * @Description：标准协议的解码器
 * @Author：wanglei
 * @Date：2020/12/16 5:24 下午
 */
public class StandardJsonDecoder extends ByteToMessageDecoder {

    private String MSG_MARKER = "$";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //读取消息类型
        short type = in.readShort();
        //读取消息长度
        int dataLength = in.readInt();

        byte[] dataBytes = new byte[dataLength];
        in.readBytes(dataBytes);
        String msgJson = new String(dataBytes, "UTF-8");

        ProtoMsg protoMsg = new ProtoMsg();
        protoMsg.setMsg(msgJson);
        protoMsg.setMsgLength(dataLength);
        protoMsg.setMsgType(type);
//
//        System.out.println("消息长度：【{}】，消息类型：【{}】msg:【{}】", dataLength, MsgEnum.printDesc(type), protoMsg.toString());
        return protoMsg;
    }

    /**
     * 是否为消息
     *
     * @param in
     * @return
     */
    private boolean isHead(ByteBuf in) {
        byte[] checkHeader = new byte[MSG_MARKER.getBytes().length];
        if (in.readableBytes() < MSG_MARKER.getBytes().length) {
            return false;
        }
        in.getBytes(in.readerIndex(), checkHeader);
        return Arrays.equals(MSG_MARKER.getBytes(), checkHeader);
    }

    /**
     * 查找标识尾
     *
     * @param bs
     * @param offset
     * @return
     */
    private int findEnd(ByteBuf bs, int offset) {
        boolean flag = false;
        byte[] check;
        int i = offset;
        for (; i < bs.readableBytes(); i += MSG_MARKER.getBytes().length) {
            //每次进来清空下
            check = new byte[MSG_MARKER.getBytes().length];
            bs.getBytes(i, check);
            if (Arrays.equals(MSG_MARKER.getBytes(), check)) {
                flag = true;
                break;
            }
        }
        return flag ? i : -1;
    }

}
