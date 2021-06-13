package tr.yildiz.wardrobe;

public class Event {
    private int eventId;
    private String eventName;
    private String eventType;
    private String eventDate;
    private String eventLocation;

    public Event(String eventName, String eventType, String eventDate, String eventLocation) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
