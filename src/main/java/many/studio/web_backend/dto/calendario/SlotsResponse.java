package many.studio.web_backend.dto.calendario;

import java.time.LocalDate;
import java.util.List;

public class SlotsResponse {

    private LocalDate data;
    private List<Slot> slots;

    public SlotsResponse(LocalDate data, List<Slot> slots) {
        this.data = data;
        this.slots = slots;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
}
