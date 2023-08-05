package pojo;

// 终端位置信息
public class obuPosition {
    private int alarm ;
    private int angle ;
    private int bizType ;
    private int gpsTime ;
    private int height ;
    private int lat ;
    private String license ;
    private int lineCode ;
    private int lineId ;
    private int lng ;
    private int mile ;
    private int speed ;
    private int status ;
    private long vehicleId ;
    private int crowding ;

    public obuPosition(int alarm, int angle, int bizType, int gpsTime, int height, int lat, String license, int lineCode, int lineId, int lng, int mile, int speed, int status, long vehicleId, int crowding) {
        this.alarm = alarm;
        this.angle = angle;
        this.bizType = bizType;
        this.gpsTime = gpsTime;
        this.height = height;
        this.lat = lat;
        this.license = license;
        this.lineCode = lineCode;
        this.lineId = lineId;
        this.lng = lng;
        this.mile = mile;
        this.speed = speed;
        this.status = status;
        this.vehicleId = vehicleId;
        this.crowding = crowding;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(int gpsTime) {
        this.gpsTime = gpsTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getLineCode() {
        return lineCode;
    }

    public void setLineCode(int lineCode) {
        this.lineCode = lineCode;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public int getMile() {
        return mile;
    }

    public void setMile(int mile) {
        this.mile = mile;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getCrowding() {
        return crowding;
    }

    public void setCrowding(int crowding) {
        this.crowding = crowding;
    }
}
