
import java.io.Serializable;


public class Event implements Serializable{
	/**
	 * @param String eventName,startingTime, endingTime, eventDate
	 */
	private static final long serialVersionUID = 1L;
	private String eventName, startingTime, endingTime, eventDate;

	public Event(String eventName, String startingTime, String endingTime, String date) {
		this.eventName = eventName;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.eventDate = date;
	}
    
    /**
     * @return eventDate
     */
	public String getEventDate() {
		return eventDate;
	}
    
    /**
     * @return eventName
     */
	public String getEventName() {
		return eventName;
	}
    
    /**
     * @return startingTime
     */
	public String getStartingTime() {
		return startingTime;
	}
    
    /**
     * @return endingTime
     */
	public String getEndingTime() {
		return endingTime;
	}
}
