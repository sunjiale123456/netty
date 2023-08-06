package write;

import pojo.inOutStation;
import pojo.obuPosition;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class writeHandler {
    private ClickHouseConnection connection;
    private PreparedStatement statement;
    private static PreparedStatement statementInOut;
    private static PreparedStatement statementObuPos;

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
    public String PositionSql = "INSERT INTO ods_online_obu_position (\n" +
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

    public writeHandler(ClickHouseConnection connection) throws SQLException {
        statementInOut= connection.prepareStatement(inoutSql);;
        statementObuPos= connection.prepareStatement(PositionSql);
    }
    public PreparedStatement toInOutTable (PreparedStatement statementInOut,inOutStation inOutObj) throws SQLException {
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
        return statementInOut;
    }

    public PreparedStatement toPosition (PreparedStatement statementObuPos,obuPosition position) throws SQLException {
//        statementObuPos = connection.prepareStatement(PositionSql);
//        statementObuPos.addBatch(PositionSql);
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
        return statementObuPos;
    }


}
