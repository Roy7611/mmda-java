/**
 * 
 */
package cloud.mmda.core.utils;

import java.util.UUID;

/**
 * @author roshion
 *
 */
public abstract class TimeUuidUtil {
	static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
	public static long getTime(UUID uuid) {
		return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
	}
}
