package pl.edu.uj.ii.webapp.ui;

import java.util.concurrent.TimeUnit;

/**
 * Created by gauee on 5/14/16.
 */
public class TimeDuration {

    public String parse(long duration) {
        has(duration, TimeUnit.MINUTES);
        StringBuilder sb = new StringBuilder();
        if (has(duration, TimeUnit.MINUTES)) {
            sb.append(TimeUnit.MILLISECONDS.toMinutes(duration))
                    .append("min ");
        }
        duration = roundToSeconds(duration);
        if (has(duration, TimeUnit.SECONDS)) {
            sb.append(TimeUnit.MILLISECONDS.toSeconds(duration))
                    .append("sec ");
        }
        duration = roundToMilliseconds(duration);
        if (has(duration, TimeUnit.MILLISECONDS)) {
            sb.append(duration)
                    .append("millis");
        }
        return sb.toString();
    }

    private long roundToMilliseconds(long duration) {
        return duration % TimeUnit.SECONDS.toMillis(1);
    }

    private long roundToSeconds(long duration) {
        return duration % TimeUnit.MINUTES.toMillis(1);

    }


    private boolean has(long duration, TimeUnit timeUnit) {
        return duration > timeUnit.toMillis(1);
    }
}
