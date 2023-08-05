package write;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pojo.inOutStation;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class ClickHouseHandler extends ChannelInboundHandlerAdapter {

    private ClickHouseConnection connection;
    private PreparedStatement statement;
    private static int batchSize=0;
    public final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    LocalDateTime batchTime = now();  // 获取当前时间

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 建立 ClickHouse 连接
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser("default");
        properties.setPassword("Gds!23d3");
        ClickHouseDataSource dataSource = new ClickHouseDataSource("jdbc:clickhouse://10.91.125.4:8123", properties);
        connection = dataSource.getConnection();

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
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String msgStr = new String(bytes);

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

        // 准备 ClickHouse 插入语句
        String insertQuery = "INSERT INTO bigdatadb_sjz.ods_online_bus_ad (\n" +
                "angle ,\n" +
                "bizType ,\n" +
                "doorCnt ,\n" +
                "flag ,\n" +
                "height ,\n" +
                "ioStation ,\n" +
                "ioTime ,\n" +
                "lat ,\n" +
                "leaveTime ,\n" +
                "license ,\n" +
                "lineId ,\n" +
                "lineName ,\n" +
                "lng ,\n" +
                "passengerCnt ,\n" +
                "speed ,\n" +
                "stationCode ,\n" +
                "stationId ,\n" +
                "stationName ,\n" +
                "stationNo ,\n" +
                "vehicleId \n" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if(msgStr.contains("ProtoMsg")) {

            String mess = msgStr.split("=")[3];
            String str = mess.replace(")", "");
            inOutStation inOutObj =null;

            try {
                statement = connection.prepareStatement(insertQuery);
                inOutObj = JSON.parseObject(str, inOutStation.class);
                batchSize++;

                // 将数据写入 ClickHouse
                statement.setString(1, "" + inOutObj.getAngle());
                statement.setString(2, "" + inOutObj.getBizType());
                statement.setString(3, "" + inOutObj.getDoorCnt());
                statement.setString(4, "" + inOutObj.getFlag());
                statement.setString(5, "" + inOutObj.getHeight());
                statement.setString(6, "" + inOutObj.getioStation());
                statement.setString(7, "" + inOutObj.getIoTime());
                statement.setString(8, "" + inOutObj.getLat());
                statement.setString(9, "" + inOutObj.getLeaveTime());
                statement.setString(10, "" + inOutObj.getLicense());
                statement.setString(11, "" + inOutObj.getLineId());
                statement.setString(12, "" + inOutObj.getLineName());
                statement.setString(13, "" + inOutObj.getLng());
                statement.setString(14, "" + inOutObj.getPassengerCnt());
                statement.setString(15, "" + inOutObj.getSpeed());
                statement.setString(16, "" + inOutObj.getStationCode());
                statement.setString(17, "" + inOutObj.getStationId());
                statement.setString(18, "" + inOutObj.getStationName());
                statement.setString(19, "" + inOutObj.getStationNo());
                statement.setString(20, "" + inOutObj.getVehicleId());
                statement.addBatch();

                // 判断当前时间是否比之前的时间大于5秒钟 或者 batchSize超过5000
                if (batchSize >= 50 || Duration.between(batchTime,now()).getSeconds()>=5) {
                    statement.executeBatch();
                    System.out.println(batchSize+"数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
                    batchTime = now();
                    batchSize = 0;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 执行关闭连接
        statement.close();
        connection.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}