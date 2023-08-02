package send;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class helloClient {
    public static void main(String[] args) throws InterruptedException {
        // 1、启动类
        new Bootstrap()
                .group(new NioEventLoopGroup())
                // 选择客户端的实现
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost",7777))
                .sync()
                .channel()
                .writeAndFlush("hello world");
    }
}
