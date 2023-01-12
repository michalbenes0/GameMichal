package gameFunctions.Updates;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import renderEngine.skybox.SkyboxRenderer;

public class SkyboxTimeIncrease extends Thread {

    private SkyboxRenderer Skybox;

    public void setSkybox(SkyboxRenderer skybox) {
        Skybox = skybox;
    }

    public void run() {
        Time time = new Time();
        time.start();
        while (true) {

            //float realTime = getTime();
            float realTime = time.getTime();
            realTime /= 1000;
            realTime %= 86400;
            realTime /= 86400;
            realTime *= 24000;
//			System.out.println(realTime);
            Skybox.time = realTime;
            Skybox.getShader().rotation = realTime / 24000 * 180 * 1000;

        }
    }

    private long getTime() {
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //Timestamp timestamp = new Timestamp(java.time.Instant.now().getEpochSecond());
        //return timestamp.getTime();
        Date date = new Date();
        long timeMilli = date.getTime();
        Calendar calendar = Calendar.getInstance();
        long timeMilli2 = calendar.getTimeInMillis();
        return Instant.now().getEpochSecond();
    }
}
