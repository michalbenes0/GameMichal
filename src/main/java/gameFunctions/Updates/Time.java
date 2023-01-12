package gameFunctions.Updates;

public class Time extends Thread {

    private long time;
    private long systemTime;
    private long NMB;

    public void run() {
        while (true) {
            time += 1;
            long systemTimeTmp = System.currentTimeMillis() / 60_000;
            if (systemTime != systemTimeTmp) {
                time = System.currentTimeMillis();
                systemTime = systemTimeTmp;
                NMB += 1;
                System.out.println("System time changed" + NMB);
            }
            try {
                this.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public long getTime() {
        return time;
    }
}
