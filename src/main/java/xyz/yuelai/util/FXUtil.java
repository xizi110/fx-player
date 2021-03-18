package xyz.yuelai.util;

import javafx.util.Duration;

/**
 * @author zhong
 * @date 2021-03-16 11:49:08 周二
 */
public class FXUtil {

    private static final String HOUR_MINUTE_SECOND = "%02d:%02d:%02d";
    private static final String MINUTE_SECOND = "%02d:%02d";

    /**
     * 格式化时间，03:34 或者 01:03:34
     *
     * @param duration 待格式化的时间
     * @return 格式化后的时间
     */
    public static String formatMusicTime(Duration duration) {
        double totalSeconds = duration.toSeconds();
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);
        return hours == 0 ? String.format(MINUTE_SECOND, minutes, seconds) : String.format(HOUR_MINUTE_SECOND, hours, minutes, seconds);
    }

    public static void main(String[] args) {
        System.out.println(formatMusicTime(new Duration(1000)));
    }


}
