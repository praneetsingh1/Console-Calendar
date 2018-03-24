
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

enum DAYS
{
	Su, Mo, Tu, We, Th, Fr, Sa;
}

public class MyCalendar {
	 DAYS[] arrayOfDays = DAYS.values();
	private TreeMap<Date, ArrayList<Event>> events = new TreeMap<>();
	
	public DAYS[] arrayOFDays() {
		return arrayOfDays;
	}
	/**
	 * gets the next day event
	 * @param c
	 * @throws ParseException 
	 */
	public void getNext(Calendar c) throws ParseException { //return the next day
		c.add(Calendar.DATE, 1);
		DateFormat day = new SimpleDateFormat("EEEE, MMMM d, yyyy");
		System.out.println(day.format(c.getTime()));
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String today = df.format(c.getTime());
		Date d = df.parse(today);
		getCurrentEvent(d);
	}
	
	/**
	 * Gets the previous day event
	 * @param c
	 * @throws ParseException 
	 */
	public void getPrev(Calendar c) throws ParseException { //return the next day
		c.add(Calendar.DATE, -1);
		DateFormat day = new SimpleDateFormat("EEEE, MMMM d, yyyy");
		System.out.println(day.format(c.getTime()));
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String today = df.format(c.getTime());
		Date d = df.parse(today);
		getCurrentEvent(d);
	}
	/**
	 * Prints the previous month
	 * @param c
	 * @throws ParseException
	 */
	public void getPrevMonth(Calendar c) throws ParseException {
		c.add(Calendar.MONTH, -1);
		monthEvents(c);
	}
	/**
	 * Prints the next month 
	 * @param c
	 * @throws ParseException
	 */
	public void getNextMonth(Calendar c) throws ParseException {
		c.add(Calendar.MONTH, 1);
		monthEvents(c);
	}
	
	/**
	 * Prints month given a Calendar
	 * @param c
	 * @throws ParseException
	 */
	public void monthEvents(Calendar c) throws ParseException {
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		DateFormat dfs = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat month = new SimpleDateFormat("MMMM yyyy");
		System.out.println("     " + month.format(c.getTime()));
		for(DAYS days: arrayOfDays) {
			System.out.print(days + " ");
		}
		int firstDayOFMonths = temp.get(Calendar.DAY_OF_WEEK)-1;
		
		System.out.println();
		
		for(int i = 0; i < firstDayOFMonths; i++) {
			System.out.print("   ");
		}
		for(int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			
			String dated = dfs.format(temp.getTime());
			Date dates = dfs.parse(dated);
			if((i+firstDayOFMonths) % 7 == 1) {
				System.out.println();
			}
			if(events.containsKey(dates)) {
				System.out.printf("{%2d} ", i);
			}else {
				System.out.printf("%2d ", i);
			}
			temp.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	/**
	 * Adds an event to the treeMap
	 * @param e
	 * @throws ParseException 
	 */
	public void add(Event e) throws ParseException {
		DateFormat dap = new SimpleDateFormat("MM/dd/yyyy");
		Date dat = dap.parse(e.getEventDate());
		
		ArrayList<Event> even = events.get(dat);
		if(events.containsKey(dat)) {
			even.add(e);
		}else {
			even = new ArrayList<>();
			even.add(e);
			DateFormat da = new SimpleDateFormat("MM/dd/yyyy");
			Date d = da.parse(e.getEventDate());
			events.put(d, even);
		}
		Collections.sort(even, sortTime());
	}
	
	/**
	 * Removes selected event based on
	 * @param date
	 * @param eventName
	 */
	public void removeSelected(Date date, String eventName){
		
		if(events.containsKey(date)) {
			ArrayList<Event> even = events.get(date);
			for(Event o : even) {
				if(o.getEventName().equals(eventName)) {
					even.remove(o);
					if(events.get(date).isEmpty()) {
						events.remove(date);
						System.out.printf("Event %s removed!", eventName);
					}
					break;
				}
				
			}
		}
	}
	
	/**
	 * Removes all events from a given date
	 * @param date
	 */
	public void removeAll(Date date){
		if(events.containsKey(date)) {
			events.remove(date);
			System.out.printf("All events deleted!");
		}
	}
	
	/**
	 * check if there is a time conflict	
	 * @param e
	 * @return
	 * @throws ParseException
	 */
	public boolean timeConflict(Event e) throws ParseException {
		DateFormat df = new SimpleDateFormat("K:mm");
		Date startTime = df.parse(e.getStartingTime());
		Date endTime = df.parse(e.getEndingTime());	
		DateFormat da = new SimpleDateFormat("MM/dd/yyyy");
		Date d = da.parse(e.getEventDate());
		if(events.get(d) != null) {
			for(Event p : events.get(d)) {
				Date tempStart = df.parse(p.getStartingTime());
				Date tempEnd = df.parse(p.getEndingTime());
					if( (startTime.after(tempStart) && startTime.before(tempEnd)) || 
							(endTime.after(tempStart) && endTime.before(tempEnd)) ||
								(startTime.equals(tempStart) && endTime.equals(tempEnd))) { //then there is conflict
						return true;
					}
			}	
		
		}
		return false;
	}
	
	/**
	 * print events in a specific date
	 * @param Date date
	 */
	public void printEvents(Date date){
			for(Event e : events.get(date)) {
				System.out.println("  " + e.getEventName() + " " + e.getStartingTime() + " - " + e.getEndingTime());
			}
	}
	
	/**
	 * print out all the events
	 * @throws ParseException 
	 */
	public void printAllEvents() throws ParseException {
		DateFormat dayAll = new SimpleDateFormat("yyyy");
		Date year = null;
		Date year1 = null;
		for(Date date : events.keySet()) {
			String yearString1 = dayAll.format(date);
			year1 = dayAll.parse(yearString1);
			//System.out.println(dayAll.format(date));
			if((year == null) || (!year.equals(year1))) {
				System.out.println(dayAll.format(date));
			}
			for(Event e : events.get(date)) {
				DateFormat day = new SimpleDateFormat("EEEE MMMM d ");
				System.out.println("  " + day.format(date) + e.getEventName() + " " + e.getStartingTime() + " - " + e.getEndingTime());
			}
			String yearString = dayAll.format(date);
			year = dayAll.parse(yearString);
		}
	}
	
	/**
	 * Returns an array of all the events
	 * @return ArrayList of events
	 */
	public ArrayList<Event> arrayOfAllEvents() {
		ArrayList<Event> allEvents = new ArrayList<>();
		for(Date date : events.keySet()) {
			for(Event e : events.get(date)) {
				allEvents.add(e);
			}
		}
		return allEvents;
	}
	
	/**
	 * Gets the current event based on given date
	 * Gets an event based on date and print it
	 * @param Date date 
	 */
	public void getCurrentEvent(Date date) {
		if(events.containsKey(date)) {
			printEvents(date);
		}
	}
	
	/**
	 * Creates a new comparator to sort by starting time using anonymous class.
	 * @return
	 */
	private  Comparator<Event> sortTime() {
		return new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				return o1.getStartingTime().compareTo(o2.getStartingTime());
			}
		};
	}	
}
