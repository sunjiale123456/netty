package send;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import server.Message;
import server.ProtoMsg;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClientTest {
    private String host;
    private int port;
    int index = 0;
    private static int MAX_RETRIES = 1000;

    public NettyClientTest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

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
                                    // 接收到服务端发回的消息 字符
//                                    ByteBuf byteBuf = (ByteBuf) msg;
//                                    String message = byteBuf.toString(CharsetUtil.UTF_8);
//                                    System.out.println("Received message from server: " + message);

                                    // 在这里可以对接收到的消息进行处理
                                    // 发送数据的逻辑
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    byte[] bytes = new byte[byteBuf.readableBytes()];
                                    byteBuf.readBytes(bytes);
                                    String receivedData = new String(bytes);
                                    System.out.println("Received data: " + receivedData);
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
//                String message = "$数据开始   - >   "+ (index++)+"$";
                String message = "ProtoMsg(msgType = 2, msgLength = 411, msg = {\n" +
                        "\t\"angle\": 220,\n" +
                        "\t\"bizType\": 2,\n" +
                        "\t\"doorCnt\": 1,\n" +
                        "\t\"flag\": 4,\n" +
                        "\t\"height\": 0,\n" +
                        "\t\"ioStation\": 3,\n" +
                        "\t\"ioTime\": \"2020-12-18 11:52:35\",\n" +
                        "\t\"lat\": 113.918317,\n" +
                        "\t\"leaveTime\": 0,\n" +
                        "\t\"license\": \"川A12001\",\n" +
                        "\t\"lineId\": 1204426124640320,\n" +
                        "\t\"lineName\": \"120\",\n" +
                        "\t\"lng\": 22.549599,\n" +
                        "\t\"passengerCnt\": 14,\n" +
                        "\t\"speed\": 0.0,\n" +
                        "\t\"stationCode\": 1214,\n" +
                        "\t\"stationId\": 1204426126639181,\n" +
                        "\t\"stationName\": \"金马广场\",\n" +
                        "\t\"stationNo\": 4,\n" +
                        "\t\"vehicleId\": 1204428111396928\n" +
                        "})";
                //创建协议包对象

                ProtoMsg protoMsg = new ProtoMsg();


                // 准备要推送的字节数据
                byte[] data = message.getBytes();

                // 创建ByteBuf，并写入数据
                ByteBuf buffer = Unpooled.buffer();
                buffer.writeBytes(data);

                // 推送字节数据
                channel.writeAndFlush(buffer);

//                ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
//                index++;
//                channel.writeAndFlush(buffer);
            }, 0, 500, TimeUnit.MILLISECONDS);

       // 等待连接关闭
            future.channel().closeFuture().sync();
            group.shutdownGracefully();
}

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1"; // 替换为实际的服务器主机名或IP地址
//        String host = "10.180.13.130"; // 替换为实际的服务器主机名或IP地址
        int port = 7777; // 替换为实际的服务器端口号
        NettyClientTest client = new NettyClientTest(host, port);
        client.start();
    }
}

