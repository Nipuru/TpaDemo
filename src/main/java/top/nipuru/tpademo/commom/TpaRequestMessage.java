package top.nipuru.tpademo.commom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author Nipuru
 * @since 2023/07/30 16:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TpaRequestMessage implements Serializable {
    private static final long serialVersionUID = -5450022522711432485L;

    /** 表示请求成功 */
    public static final int SUCCESS = 0;
    /** 表示玩家不在线败 */
    public static final int NOT_ONLINE = -1;
    /** 表示已经请求等候处理 */
    public static final int ALREADY_REQUEST = 1;

    /** 发出tpa指令的玩家名字 */
    String sender;
    /** tpa 的目标玩家名字 */
    String receiver;

    /** true:tpa false:tpahere */
    boolean isTpa;

    /** 消息发送的时间戳 */
    long time;

}
