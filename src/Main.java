import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger ai = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        RaceOne raceOne = new RaceOne();
        Thread thread1 = new Thread(raceOne);

        RaceTwo raceTwo = new RaceTwo();
        Thread thread2 = new Thread(raceTwo);

        raceOne.thread = thread2;
        raceTwo.thread = thread1;

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("i = " + ai.get());
        resultRace(ai);
    }

    public static class RaceOne implements Runnable{

        private Thread thread;

        @Override
        public void run(){
            boolean interrupted = false;
            System.out.println("Start for " + Thread.currentThread().getName());
            for (int i = 0; i < 100; i++) {
                ai.incrementAndGet();
                interrupted = Thread.interrupted() || sleepRandom(500);
                if (interrupted){
                    break;
                }
            }
            if (!interrupted){
                thread.interrupt();
            }
        }
    }

    public static class RaceTwo implements Runnable {

        private Thread thread;

        @Override
        public void run(){
            boolean interrupted = false;
            System.out.println("Start for " + Thread.currentThread().getName());
            for (int i = 0; i < 100; i++) {
                ai.decrementAndGet();
                interrupted = Thread.interrupted() || sleepRandom(500);
                if (interrupted){
                    break;
                }
            }
            if (!interrupted){
                thread.interrupt();
            }
        }
    }

    public static void resultRace(AtomicInteger ai){
        if (ai.intValue() > 0){
            System.out.println("Win one");
        }else if (ai.intValue() < 0){
            System.out.println("Win two");
        }else {
            System.out.println("Draw");
        }
    }

    public static boolean sleepRandom(int t){
        Random random = new Random();
        int time = random.nextInt(t);

        boolean b = false;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            b = true;
        }
        return b;
    }
}
