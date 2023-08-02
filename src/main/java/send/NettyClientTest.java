package send;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClientTest {
    private String host;
    private int port;
    int index = 0;

    public NettyClientTest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    // 接收到服务端发回的消息
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    String message = byteBuf.toString(CharsetUtil.UTF_8);
                                    System.out.println("Received message from server: " + message);

                                    // 在这里可以对接收到的消息进行处理

                                    // 释放资源
                                    byteBuf.release();
                                }
                            });
                        }
                    });

            // 连接到服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();

            // 获取连接的Channel
            Channel channel = future.channel();

            // 使用定时任务发送数据
            channel.eventLoop().scheduleAtFixedRate(() -> {
                String message = "数据开始   - >   "+ index++;
                channel.writeAndFlush(message);
                if(index==100)index=0;
            }, 0, 1, TimeUnit.SECONDS);

            // 等待连接关闭
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//        String host = "127.0.0.1"; // 替换为实际的服务器主机名或IP地址
        String host = "192.168.163.1"; // 替换为实际的服务器主机名或IP地址
        int port = 7777; // 替换为实际的服务器端口号
        NettyClientTest client = new NettyClientTest(host, port);
        client.start();
    }
}

