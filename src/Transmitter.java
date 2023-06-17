import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Transmitter implements Runnable {
    private final TransmissionMedia transmissionMedia;
    private final long idTransmitter;
    private AtomicInteger counterOfFramesSending;
    private AtomicInteger counterOfCollisions;

    public Transmitter(TransmissionMedia transmissionMedia, long idTransmitter,
                       AtomicInteger counterOfFramesSending, AtomicInteger counterOfCollisions) {
        this.transmissionMedia = transmissionMedia;
        this.idTransmitter = idTransmitter;
        this.counterOfFramesSending = counterOfFramesSending;
        this.counterOfCollisions = counterOfCollisions;
    }

    @Override
    public void run() {
        while (true) {
            while (this.transmissionMedia.isBusy()) {
                try {
                    Thread.sleep(this.calculateTruncatedExponentialTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.counterOfFramesSending.incrementAndGet();
            this.sendFrame();
            this.waitTransmission();
            if (this.transmissionMedia.hasCollision()) {
                this.counterOfCollisions.incrementAndGet();
                this.waitCollision();
            }
            this.transmissionMedia.cleanTransmittersTransmitting();
            this.transmissionMedia.setBusy(false);
        }
    }

    private void sendFrame() {
        System.out.println("Frame send for the transmitter " + this.idTransmitter);
        this.transmissionMedia.setBusy(true);
        this.transmissionMedia.addTransmittersTransmitting(this.idTransmitter);
    }

    private void waitTransmission() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitCollision() {
        List<Long> transmitters = new ArrayList<>(this.transmissionMedia.getTransmittersTransmitting());
        System.out.println("Collision detected. Backoff algorithm being applied by transmitters: " + transmitters);

        try {
            Thread.sleep(calculateTruncatedExponentialTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long calculateTruncatedExponentialTime() {
        long randomDelay = (long) (Math.random() * 100);
        randomDelay *= 2;
        return randomDelay;
    }
}