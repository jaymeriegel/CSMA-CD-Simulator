import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransmissionMedia {
    private final AtomicBoolean busy;
    private final List<Long> transmittersTransmitting;

    public TransmissionMedia() {
        this.busy = new AtomicBoolean(false);
        this.transmittersTransmitting = new ArrayList<>();
    }

    public boolean isBusy() {
        return this.busy.get();
    }

    public void setBusy(boolean value) {
        this.busy.set(value);
    }

    public void addTransmittersTransmitting(long idTransmitter) {
        this.transmittersTransmitting.add(idTransmitter);
    }

    public boolean hasCollision() {
        return this.transmittersTransmitting.size() > 1;
    }

    public void cleanTransmittersTransmitting() {
        this.transmittersTransmitting.clear();
    }

    public List<Long> getTransmittersTransmitting() {
        return this.transmittersTransmitting;
    }
}