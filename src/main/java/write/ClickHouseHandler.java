package write;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import pojo.inOutStation;
import pojo.obuPosition;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import server.ProtoMsg;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class ClickHouseHandler extends ChannelInboundHandlerAdapter {

    private static ClickHouseConnection connection;
    private static PreparedStatement statementInOut;
    private static PreparedStatement statementObuPos;
    private static writeHandler writeHandler;
    private static int batchSizeInOut=0;
    private static int batchSizeObuPos=0;
    public final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static LocalDateTime batchTime = now();  // 获取当前时间

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 建立 ClickHouse 连接
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser("default");
        properties.setPassword("Gds!23d3");
        ClickHouseDataSource dataSource = new ClickHouseDataSource("jdbc:clickhouse://10.91.125.4:8123/bigdatadb_sjz", properties);
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        writeHandler = new writeHandler(connection);

        System.out.println("连接信息：" + ctx.channel() +"   "+timeFormat.format(System.currentTimeMillis()));

        // 向客户端发送数据
        String message = "欢迎连接到服务器";
        ByteBuf responseBuf = ctx.alloc().buffer();
        responseBuf.writeBytes(message.getBytes());
        ctx.writeAndFlush(responseBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理接收到的数据 字节流
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        String msgStr = new String(bytes);

        //对象流
        ProtoMsg protoMsg = (ProtoMsg)msg;
        String msgStr = protoMsg.getMsg();
        short msgType = protoMsg.getMsgType();

        // 向客户端发送响应数据  推送字节数据
        String response = "已收到你的消息   " +timeFormat.format(System.currentTimeMillis());
        byte[] data = response.getBytes();
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(data);
        ctx.channel().writeAndFlush(buffer);

        //字符流
//        System.out.println(msg.toString());
//        String msgStr = msg.toString();
//        System.out.println(msgStr);
//        String mess = receivedData.split("=")[3];
//        System.out.println("--------------------");

//        writeHandler writeHandler = new writeHandler(connection);

        // 根据信息进行分数据
        if(msgType==1) {
            try {
                //进出站数据
                inOutStation inOutObj = JSON.parseObject(msgStr, inOutStation.class);
                if(batchSizeInOut!=0)statementInOut.addBatch(writeHandler.inoutSql);
                statementInOut = writeHandler.toInOutTable(statementInOut,inOutObj);
                statementInOut.addBatch();
                batchSizeInOut++;
            } catch (JSONException e) {
                System.out.println("进出站数据异常");
            }
        }else if(msgType==2){
            try {
                //终端位置数据
                obuPosition position = JSON.parseObject(msgStr, obuPosition.class);
                if(batchSizeObuPos!=0)statementInOut.addBatch(writeHandler.PositionSql);
                statementObuPos = writeHandler.toPosition(statementObuPos,position);
                statementObuPos.addBatch();
                batchSizeObuPos++;
            } catch (JSONException e) {
                System.out.println("终端位置异常");
            }
        }

            //进出站
            // 判断当前时间是否比之前的时间大于5秒钟 或者 batchSize超过5000 数据写入 ClickHouse
//            statementInOut.addBatch();
//            if (batchSizeInOut >= 50 || Duration.between(batchTime,now()).getSeconds()>=5) {
//                statementInOut.executeBatch();
//                System.out.println(batchSizeInOut + "进出站 数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
//                batchTime = now();
//                batchSizeInOut = 0;
//            }
//            //终端位置
//            // 判断当前时间是否比之前的时间大于5秒钟 或者 batchSize超过5000 数据写入 ClickHouse
//            statementObuPos.addBatch();
//            if (batchSizeObuPos >= 50 || Duration.between(batchTime,now()).getSeconds()>=5) {
//                statementObuPos.executeBatch();
//                System.out.println(batchSizeObuPos + "终端位置 数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
//                batchTime = now();
//                batchSizeObuPos = 0;
//            }
//            // 提交事务
//            connection.commit();

            LocalDateTime now = now();
            if (batchSizeObuPos >= 100 || batchSizeInOut >=100) {
                statementInOut.executeBatch();
                statementObuPos.executeBatch();
                batchTime = now;
                System.out.println("提交之前"+timeFormat.format(System.currentTimeMillis()));
                connection.commit();
                System.out.println("提交之后"+timeFormat.format(System.currentTimeMillis()));
                System.out.println(batchSizeInOut + "进出站 数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
                System.out.println(batchSizeObuPos + "终端位置 数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
                batchSizeInOut = 0;
                batchSizeObuPos = 0;
            }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 执行关闭连接
        statementInOut.close();
        statementObuPos.close();
        connection.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}