package cv.support;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by amitrus on 11/23/16.
 */
public class DateSupport {
    private static Long beginTime = Timestamp.valueOf("1990-01-01 00:00:00").getTime();
    private static Long endTime = Timestamp.valueOf("1994-12-31 00:58:00").getTime();

    public static Date getRandomDate() {
        long diff = endTime - beginTime + 1;
        return new Date(beginTime + (long) (Math.random() * diff));
    }
}
