package write;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import guigu.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import pojo.inOutStation;
import pojo.obuPosition;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.ClickHousePreparedStatement;
import ru.yandex.clickhouse.settings.ClickHouseProperties;
import server.ProtoMsg;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

public class ClickHouseBatchWriter extends ChannelInboundHandlerAdapter {
    private final ClickHouseConnection connection;
    private int batchSize = 0;
    private static List<inOutStation> inOutBatchData;
    private static List<obuPosition> obuBatchData;
    public final static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static LocalDateTime batchTime = now();  // 获取当前时间

    public ClickHouseBatchWriter() throws SQLException {
        // 建立 ClickHouse 连接
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setUser("default");
        properties.setPassword("Gds!23d3");
        ClickHouseDataSource dataSource = new ClickHouseDataSource("jdbc:clickhouse://10.91.125.4:8123/bigdatadb_sjz", properties);
        this.connection = dataSource.getConnection();
        this.connection.setAutoCommit(false);
        this.inOutBatchData = new ArrayList<>();
        this.obuBatchData = new ArrayList<>();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接信息：" + ctx.channel() +"   "+timeFormat.format(System.currentTimeMillis()));

        // 向客户端发送数据
        String message = "欢迎连接到服务器";
//        ByteBuf responseBuf = ctx.alloc().buffer();
//        responseBuf.writeBytes(message.getBytes());
//        ctx.writeAndFlush(responseBuf);

        byte[] content = message.getBytes(Charset.forName("utf-8"));
        int length = message.getBytes(Charset.forName("utf-8")).length;
        MessageProtocol messageProtocol = new MessageProtocol();
        //创建协议包对象
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //对象流
        ProtoMsg protoMsg = (ProtoMsg)msg;
        String msgStr = protoMsg.getMsg();
        short msgType = protoMsg.getMsgType();

        if(msgType==1) {
            try {
                //进出站数据
                inOutStation inOutObj = JSON.parseObject(msgStr, inOutStation.class);
                this.inOutBatchData.add(inOutObj);
                batchSize++;
            } catch (JSONException e) {
                System.out.println("进出站数据异常");
            }
        }else if(msgType==2){
            try {
                //终端位置数据
                obuPosition position = JSON.parseObject(msgStr, obuPosition.class);
                this.obuBatchData.add(position);
                batchSize++;
            } catch (JSONException e) {
                System.out.println("终端位置异常");
            }
        }

        if (batchSize>= 1000 || Duration.between(batchTime,now()).getSeconds()>=5) {
            writeBatchData();
            connection.commit();
            batchTime = now();
            System.out.println(batchSize + " 数据已经批量写入。。" + timeFormat.format(System.currentTimeMillis()));
            batchSize=0;
        }
    }

//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("连接信息：" + ctx.channel() +"   "+timeFormat.format(System.currentTimeMillis()));
//
//        // 向客户端发送数据
//        String message = "欢迎连接到服务器";
//        ByteBuf responseBuf = ctx.alloc().buffer();
//        responseBuf.writeBytes(message.getBytes());
//        ctx.writeAndFlush(responseBuf);
//    }

    private void writeBatchData() throws SQLException {
        //进出站
        if (!inOutBatchData.isEmpty()) {
            try (
                    ClickHousePreparedStatement statementInOut = (ClickHousePreparedStatement) connection.prepareStatement(inoutSql)) {
                for (inOutStation inOutObj : inOutBatchData) {
                    // 解析字节数据并设置参数
                    statementInOut.setString(1, "" + inOutObj.getAngle());
                    statementInOut.setString(2, "" + inOutObj.getBizType());
                    statementInOut.setString(3, "" + inOutObj.getDoorCnt());
                    statementInOut.setString(4, "" + inOutObj.getFlag());
                    statementInOut.setString(5, "" + inOutObj.getHeight());
                    statementInOut.setString(6, "" + inOutObj.getioStation());
                    statementInOut.setString(7, "" + inOutObj.getIoTime());
                    statementInOut.setString(8, "" + inOutObj.getLat());
                    statementInOut.setString(9, "" + inOutObj.getLeaveTime());
                    statementInOut.setString(10, "" + inOutObj.getLicense());
                    statementInOut.setString(11, "" + inOutObj.getLineId());
                    statementInOut.setString(12, "" + inOutObj.getLineName());
                    statementInOut.setString(13, "" + inOutObj.getLng());
                    statementInOut.setString(14, "" + inOutObj.getPassengerCnt());
                    statementInOut.setString(15, "" + inOutObj.getSpeed());
                    statementInOut.setString(16, "" + inOutObj.getStationCode());
                    statementInOut.setString(17, "" + inOutObj.getStationId());
                    statementInOut.setString(18, "" + inOutObj.getStationName());
                    statementInOut.setString(19, "" + inOutObj.getStationNo());
                    statementInOut.setString(20, "" + inOutObj.getVehicleId());
                    statementInOut.addBatch();
                }
                statementInOut.executeBatch();
                inOutBatchData.clear();
            }
        }

        if(!obuBatchData.isEmpty() ) {
            try (
                    ClickHousePreparedStatement statementObuPos = (ClickHousePreparedStatement) connection.prepareStatement(positionSql)) {
                for (obuPosition position : obuBatchData) {
                    // 解析字节数据并设置参数
                    statementObuPos.setString(1, "" + position.getAlarm());
                    statementObuPos.setString(2, "" + position.getAngle());
                    statementObuPos.setString(3, "" + position.getBizType());
                    statementObuPos.setString(4, "" + position.getGpsTime());
                    statementObuPos.setString(5, "" + position.getHeight());
                    statementObuPos.setString(6, "" + position.getLat());
                    statementObuPos.setString(7, "" + position.getLicense());
                    statementObuPos.setString(8, "" + position.getLineCode());
                    statementObuPos.setString(9, "" + position.getLineId());
                    statementObuPos.setString(10, "" + position.getLng());
                    statementObuPos.setString(11, "" + position.getMile());
                    statementObuPos.setString(12, "" + position.getSpeed());
                    statementObuPos.setString(13, "" + position.getStatus());
                    statementObuPos.setString(14, "" + position.getVehicleId());
                    statementObuPos.setString(15, "" + position.getCrowding());
                    statementObuPos.addBatch();
                }
                statementObuPos.executeBatch();
                obuBatchData.clear();
            }
        }
    }
    // 进出站
    public String inoutSql = "INSERT INTO ods_online_bus_ad (\n" +
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

    // 终端位置
    public String positionSql = "INSERT INTO ods_online_obu_position (\n" +
            "alarm  ,\n" +
            "angle  ,\n" +
            "bizType  ,\n" +
            "gpsTime  ,\n" +
            "height  ,\n" +
            "lat  ,\n" +
            "license  ,\n" +
            "lineCode  ,\n" +
            "lineId  ,\n" +
            "lng  ,\n" +
            "mile  ,\n" +
            "speed  ,\n" +
            "status  ,\n" +
            "vehicleId  ,\n" +
            "crowding \n"+
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
