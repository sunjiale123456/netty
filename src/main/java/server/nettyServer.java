
package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *
 * 监听公交平台的接入，接收动态实时数据
 */
public class nettyServer{

    public static int innerListenPort = 7777;

    public void start() {

        ServerBootstrap bootStrap = new ServerBootstrap();// 引导辅助程序  
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup wordGroup = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接  
        System.out.println("listen port:" + innerListenPort);
        try {
            bootStrap.group(bossGroup, wordGroup);
            bootStrap.channel(NioServerSocketChannel.class);// 设置nio类型的channel
            bootStrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(0, 0, 2, TimeUnit.MINUTES));
//                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(10240, Unpooled.wrappedBuffer("$".getBytes())));
//                        ch.pipeline().addLast("decoder", new StandardJsonDecoder());
//                        ch.pipeline().addLast("encoder", new StandardJsonEncoder());
                        // 将数据转为字符
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ServerHandler());
                    }
            });

            bootStrap.bind(innerListenPort).sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功  

        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            wordGroup.shutdownGracefully();
        }
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void restart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
