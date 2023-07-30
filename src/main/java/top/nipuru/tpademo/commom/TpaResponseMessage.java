package top.nipuru.tpademo.commom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Nipuru
 * @since 2023/07/30 16:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TpaResponseMessage implements Serializable {
    private static final long serialVersionUID = 6624638306571232561L;

    /** 发出 tpaccept 指令的玩家名字 */
    String sender;

    /** tpaccept 的目标玩家名字 */
    String receiver;

    /** 是否接受 */
    boolean isAccept;
}
