package pojo;

public class inOutStation {
    private int angle ;
    private int bizType ;
    private int doorCnt ;
    private int flag ;
    private int height ;
    private int ioStation ;
    private String ioTime ;
    private Double lat ;
    private int leaveTime ;
    private String license ;
    private long lineId ;
    private String lineName ;
    private Double lng ;
    private int passengerCnt ;
    private Float speed ;
    private int stationCode ;
    private long stationId ;
    private String stationName ;
    private int stationNo ;
    private long vehicleId ;

    public inOutStation(int angle, int bizType, int doorCnt, int flag, int height, int ioStation, String ioTime, Double lat, int leaveTime, String license, long lineId, String lineName, Double lng, int passengerCnt, Float speed, int stationCode, long stationId, String stationName, int stationNo, long vehicleId) {
        this.angle = angle;
        this.bizType = bizType;
        this.doorCnt = doorCnt;
        this.flag = flag;
        this.height = height;
        this.ioStation = ioStation;
        this.ioTime = ioTime;
        this.lat = lat;
        this.leaveTime = leaveTime;
        this.license = license;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lng = lng;
        this.passengerCnt = passengerCnt;
        this.speed = speed;
        this.stationCode = stationCode;
        this.stationId = stationId;
        this.stationName = stationName;
        this.stationNo = stationNo;
        this.vehicleId = vehicleId;
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

    public int getDoorCnt() {
        return doorCnt;
    }

    public void setDoorCnt(int doorCnt) {
        this.doorCnt = doorCnt;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getioStation() {
        return ioStation;
    }

    public void setioStation(int ioStation) {
        this.ioStation = ioStation;
    }

    public String getIoTime() {
        return ioTime;
    }

    public void setIoTime(String ioTime) {
        this.ioTime = ioTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getPassengerCnt() {
        return passengerCnt;
    }

    public void setPassengerCnt(int passengerCnt) {
        this.passengerCnt = passengerCnt;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public int getStationCode() {
        return stationCode;
    }

    public void setStationCode(int stationCode) {
        this.stationCode = stationCode;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStationNo() {
        return stationNo;
    }

    public void setStationNo(int stationNo) {
        this.stationNo = stationNo;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
