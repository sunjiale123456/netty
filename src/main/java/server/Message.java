package server;

/**
 * @Description: 消息实体
 * @Author: wq
 * @Date: 2020/3/5 16:05
 */
public class Message {

    /**
     * 消息主题
     */
    private String channel;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 消息类型 1:gps 2:到离站 3:客流 4:考勤 5:违规
     */
    private int type;

    public Message(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public Message(String channel, String message, int type) {
        this.channel = channel;
        this.message = message;
        this.type = type;
    }
}
