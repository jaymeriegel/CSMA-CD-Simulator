import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        TransmissionMedia transmissionMedia = new TransmissionMedia();
        int maxTimeSimulation = 1;
        int numberOfTransmitters = 10;
        List<Thread> transmitters = new ArrayList<>();
        List<AtomicInteger> countersOfSendFrames = new ArrayList<>();
        List<AtomicInteger> countersOfCollisions = new ArrayList<>();

        for (int i = 0; i < numberOfTransmitters; i++) {
            AtomicInteger frameCount = new AtomicInteger(0);
            AtomicInteger collisionCount = new AtomicInteger(0);
            countersOfSendFrames.add(frameCount);
            countersOfCollisions.add(collisionCount);
            Thread transmitter = new Thread(new Transmitter(transmissionMedia, i, frameCount, collisionCount));
            transmitters.add(transmitter);
            transmitter.start();
        }

        int seconds = 5;
        try {
            Thread.sleep(maxTimeSimulation * seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread t : transmitters) {
            t.stop();
        }


        System.out.println("Simulation summary:");
        for (int i = 0; i < numberOfTransmitters; i++) {
            AtomicInteger transmissionCount = countersOfSendFrames.get(i);
            AtomicInteger collisionCount = countersOfCollisions.get(i);
            System.out.println("Transmitter " + i + " sent: " + transmissionCount.get() + " frames");
            System.out.println("Transmitter " + i + " collision: " + collisionCount.get() + " times");
        }

        int totalSent = countersOfSendFrames.stream().mapToInt(AtomicInteger::get).sum();
        System.out.println("The total of frames sent: " + totalSent);
        double baudRate = (double) totalSent / seconds;
        System.out.println("The baud rate is: " + baudRate);
    }
}