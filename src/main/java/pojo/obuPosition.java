package pojo;

// 终端位置信息
public class obuPosition {
    private Double alarm ;
    private Double angle ;
    private Double bizType ;
    private String gpsTime ;
    private Double height ;
    private Double lat ;
    private String license ;
    private Double lineCode ;
    private Double lineId ;
    private Double lng ;
    private Double mile ;
    private Double speed ;
    private Double status ;
    private Long vehicleId ;
    private Double crowding ;

    public obuPosition(Double alarm, Double angle, Double bizType, String gpsTime, Double height, Double lat, String license, Double lineCode, Double lineId, Double lng, Double mile, Double speed, Double status, Long vehicleId, Double crowding) {
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

    public Double getAlarm() {
        return alarm;
    }

    public void setAlarm(Double alarm) {
        this.alarm = alarm;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getBizType() {
        return bizType;
    }

    public void setBizType(Double bizType) {
        this.bizType = bizType;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Double getLineCode() {
        return lineCode;
    }

    public void setLineCode(Double lineCode) {
        this.lineCode = lineCode;
    }

    public Double getLineId() {
        return lineId;
    }

    public void setLineId(Double lineId) {
        this.lineId = lineId;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getMile() {
        return mile;
    }

    public void setMile(Double mile) {
        this.mile = mile;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getStatus() {
        return status;
    }

    public void setStatus(Double status) {
        this.status = status;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getCrowding() {
        return crowding;
    }

    public void setCrowding(Double crowding) {
        this.crowding = crowding;
    }
}
