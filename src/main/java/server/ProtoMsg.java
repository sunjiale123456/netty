package server;

/**
 * @ClassName：ProtoMsg
 * @Description：消息原型
 */
public class ProtoMsg {

    /**
     * 消息类型 1:gps 2:到离站 3:客流 4:考勤 5:违规
     */
    private short msgType;
    /**
     * 消息长度
     */
    private int msgLength;
    /**
     * 消息内容json
     */
    private String msg;

    public short getMsgType() {
        return msgType;
    }

    public void setMsgType(short msgType) {
        this.msgType = msgType;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
