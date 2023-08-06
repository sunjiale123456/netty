package pojo;
import guigu.MessageProtocol;
import guigu.MyMessageDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import server.ProtoMsg;
import write.ProtoMsgEncoder;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static java.time.ZonedDateTime.now;


public class NettyClientTestLoop {
    private String host;
    private int port;

    private static short msgType = 1;
    private static long number = 1;
    private static long index = 1;
    private static String message;

    public NettyClientTestLoop(String host, int port) {
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
                protected void initChannel(NioSocketChannel ch) {
                    //协议编码器
                    ch.pipeline().addLast(new ProtoMsgEncoder());
                    ch.pipeline().addLast(new MyMessageDecoder()); //加入解码器
                    // 正常
//                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                         // 接收到服务端发回的消息 字符
                         // ByteBuf byteBuf = (ByteBuf) msg;
                         // String message = byteBuf.toString(CharsetUtil.UTF_8);
                         // System.out.println("Received message from server: " + message);
                         //在这里可以对接收到的消息进行处理
                         //发送数据的逻辑 字节

//                        ByteBuf byteBuf = (ByteBuf) msg;
//                        byte[] bytes = new byte[byteBuf.readableBytes()];
//                        byteBuf.readBytes(bytes);
//                        String receivedData = new String(bytes);
//                        System.out.println("Received data: " + receivedData);

                        MessageProtocol messageProtocol = (MessageProtocol)msg;
                        int len = messageProtocol.getLen();
                        byte[] content = messageProtocol.getContent();
                        String message = new String(content, Charset.forName("utf-8"));
                        System.out.println("Received data: " + message +"长度："+len);
                    }
                });
            }
        });

        ChannelFuture future=null;
        while (future==null) {
            try {
                // 连接到服务器
                future = bootstrap.connect(host, port).sync();
                // 获取连接的Channel
                Channel channel = future.channel();

                // 使用定时任务发送数据
                channel.eventLoop().scheduleAtFixedRate(() -> {
//               String message = "$数据开始   - >   "+ (index++)+"$";
                 if(msgType==1) {
                     // 进出站数据
                     message = "{\n" +
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
                             "}";
                     msgType=2;
                 }else {
                     //终端数据
                     message = "{\n" +
                             "\t\"alarm\": 0,\n" +
                             "\t\"angle\": 163,\n" +
                             "\t\"bizType\": 0,\n" +
                             "\t\"gpsTime\": \"2020-12-18 11:52:29\",\n" +
                             "\t\"height\": 499,\n" +
                             "\t\"lat\": 30.397653,\n" +
                             "\t\"license\": \"粤B01465D\",\n" +
                             "\t\"lineCode\": 0,\n" +
                             "\t\"lineId\": 0,\n" +
                             "\t\"lng\": 104.071048,\n" +
                             "\t\"mile\": 0,\n" +
                             "\t\"speed\": 0,\n" +
                             "\t\"status\": 3,\n" +
                             "\t\"vehicleId\": 1204498656854080,\n" +
                             " \"crowding\": -1\n" +
                             "}";
                     msgType=1;
                 }

                //创建协议包对象

                int length = message.getBytes(Charset.forName("utf-8")).length;
                ProtoMsg protoMsg = new ProtoMsg();
                protoMsg.setMsgType(msgType);
                protoMsg.setMsgLength(length);
                protoMsg.setMsg(message);
                channel.writeAndFlush(protoMsg);


                // 准备要推送的字节数据
                //byte[] data = message.getBytes();

                // 创建ByteBuf，并写入数据
                //ByteBuf buffer = Unpooled.buffer();
                //buffer.writeBytes(data);

                // 推送字节数据
                // channel.writeAndFlush(buffer);
                System.out.println("发送数据"+number++);
                if(number==50000){
                    try {
                        TimeUnit.MINUTES.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                   // 字符流
                   // ByteBuf buffer = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);
                   // index++;
                   // channel.writeAndFlush(buffer);
                }, 0, 50, TimeUnit.MILLISECONDS);
                   // 连接成功，跳出重试循环
                        break;
            } catch (Exception e) {
                // 连接失败，进行重试
                index++;
                Thread.sleep(10000);
                System.err.println("连接失败，进行第 " + index + " 次重试...             " + now());
            }
        }
        future.channel().closeFuture().sync();
        group.shutdownGracefully();
}

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1"; // 替换为实际的服务器主机名或IP地址
//        String host = "10.180.13.130"; // 替换为实际的服务器主机名或IP地址
        int port = 7777; // 替换为实际的服务器端口号
        NettyClientTestLoop client = new NettyClientTestLoop(host, port);
        client.start();
    }
}

