package top.nipuru.tpademo.commom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * 用于通知某个服务器对玩家位置进行缓存 或直接传送
 *
 * @author Nipuru
 * @since 2023/07/30 16:55
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CacheOrTeleportMessage implements Serializable {
    private static final long serialVersionUID = -466546980837287335L;

    /** 传送目标玩家名字 */
    String to;

    /** 被传送的玩家名字 */
    String from;
}
