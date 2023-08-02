package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;

import java.util.concurrent.TimeUnit;



/**
 * @Description：处理类
 * @Author：
 * @Date：
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    public final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private static final int RECONNECT_DELAY_SECONDS = 5;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String format = timeFormat.format(System.currentTimeMillis());
        System.out.println("连接信息：" + ctx.channel() +"   "+format);
        // 向客户端发送数据
        String message = "欢迎连接到服务器";
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 接收到的消息:{ "+message+" }  "+timeFormat.format(System.currentTimeMillis()));
        if (message instanceof ProtoMsg) {
            ProtoMsg protoMsg = (ProtoMsg) message;
            short msgType = protoMsg.getMsgType();
            String msg = protoMsg.getMsg();
            System.out.println("接收到 [{}] 的消息:{}" + ctx.channel().remoteAddress() + message);
        }
        // 向客户端发送响应数据
        String response = "已收到你的消息   " +timeFormat.format(System.currentTimeMillis());
        ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常被动关闭
        cause.printStackTrace();
//        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 在连接断开时被调用
//        logger.info("连接已断开，将尝试重连...");

        // 延迟一定时间后进行重连
        ctx.channel().eventLoop().schedule(this::reconnect, RECONNECT_DELAY_SECONDS, TimeUnit.SECONDS);

        super.channelInactive(ctx);
    }

    private void reconnect() {
//        try {
//            // 进行重连
//            Bootstrap bootstrap = new Bootstrap();
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            if (future.isSuccess()) {
//                logger.info("重新连接成功");
//            } else {
//                logger.error("重新连接失败");
//            }
//        } catch (InterruptedException e) {
//            logger.error("重连过程中发生异常", e);
//            Thread.currentThread().interrupt();
//        }
    }
}
