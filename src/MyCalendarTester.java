/**
 *Praneet Singh
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class MyCalendarTester {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ParseException {
	    MyCalendar cal = new MyCalendar();             //Create a new instance of Caland class
	    Calendar c = GregorianCalendar.getInstance(); //Creates new calender with current info
		
	    GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		
	    DateFormat month1 = new SimpleDateFormat("MMMM yyyy");
		System.out.println("     " + month1.format(c.getTime()));
		
		for(DAYS day: cal.arrayOfDays) {
			System.out.print(day + " ");
		}
		
		int firstDayOFMonth = temp.get(Calendar.DAY_OF_WEEK)-1;
		
		System.out.println();
		
		for(int i = 0; i < firstDayOFMonth; i++) {
			System.out.print("   ");
		}
        //print out the calander
		for(int i = 1; i <= temp.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			if((i+firstDayOFMonth) % 7 == 1) {
				System.out.println();
			}
			if(i == c.get(Calendar.DAY_OF_MONTH)) {
				System.out.printf("[%2d] ", i);
			}else {
			System.out.printf("%2d ", i);
			}
		}
		
		Boolean isTrue= true;
		Scanner in = new Scanner(System.in);
		while(isTrue) {
			System.out.print("\nSelect one of the following options: "
					+ "\n[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit : ");
			String input = in.nextLine();
			switch (input) {
				case "L":
					//load events.txt to populate the calendar. 
					//If there is no such file because it is the first run, 
					//the load function prompts a message to the user indicating 
					//this is the first run. You may use Java serialization this function.
					ArrayList<Event> inputEvents = new ArrayList<>();
					try {
			            FileInputStream inputFile = new FileInputStream("Events.txt");
			            ObjectInputStream inputObject = new ObjectInputStream(inputFile);
			            inputEvents = (ArrayList<Event>) inputObject.readObject();
			            inputObject.close();
			            inputFile.close();
			            
			        } catch (NotSerializableException e) {
			        	  // Output expected NotSerializeableExceptions.
			        		e.printStackTrace();
			        } catch (IOException | ClassNotFoundException e) {
			            // Output unexpected IOExceptions and ClassNotFoundExceptions.
			        	  System.out.println("Event.txt is not created yet.");
			        }
					for(Event inputE : inputEvents) {	//add the events to the dataStructure
						cal.add(inputE);
					}
					System.out.println("Events loaded!");
					break;
					
				case "V":
					//User can choose a Day or a Month view. 
						System.out.print("[D]ay view or [M]view ? ");
						String t = in.nextLine();
						switch(t) {
							case "D":
								DateFormat day = new SimpleDateFormat("EEEE, MMMM d, yyyy");
								System.out.println(day.format(c.getTime()));
								
								DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
								String today = df.format(c.getTime());
								Date d = df.parse(today);
								cal.getCurrentEvent(d);	//get the events for today
								
								boolean loop = true;
								while(loop) {
								System.out.print("[P]revious or [N]ext or [M]ain menu ? ");
								String next = in.nextLine();
									switch(next) {
									case "P":
										cal.getPrev(c);
										break;
									case "N":
										cal.getNext(c);	//get the next day
										break;
									case "M":
										loop = false;
										break;
									default:
											System.out.println("Invalid choice.");
									}
								}
								
								//if date then view current date and if there 
								//are any events then display the events
								break;
							
							case "M":
								cal.monthEvents(c);
								
								boolean loop2 = true;
								while(loop2) {
								System.out.print("\n[P]revious or [N]ext or [M]ain menu ? ");
								String nextMonth = in.nextLine();
									switch(nextMonth) {
									case "P":
										cal.getPrevMonth(c);
										break;
									case "N":
										cal.getNextMonth(c);	//get the next month
										break;
									case "M":
										loop2 = false;
										c = GregorianCalendar.getInstance();
										break;
									default:
										System.out.println("Invalid choice.");
									}
								//if month
								
								
								}
							break;
									
							}
					break;
					
				case "C":
					//This option allows the user to schedule an event. The calendar asks the 
					//user to enter the title, date, starting time, and ending time of an event.
					System.out.print("Enter event name: ");
					String eventName = in.nextLine();
					System.out.print("Enter event date(MM/dd/yyyy): ");
					String eventDate = in.nextLine();
					System.out.print("Enter event starting time(hh:mm): ");
					String startTime = in.nextLine();
					System.out.print("Enter event ending time(hh:mm): ");
					String endingTime = in.nextLine();
					if(endingTime.equals("")) {
						endingTime = startTime;
					}
					Event e = new Event(eventName, startTime, endingTime, eventDate); 
					
					if(!cal.timeConflict(e)) {
						cal.add(e);
					}else {
						System.out.println("Time Conflict!");
					}
					
					
					break;
					
				case "G":
					//With this option, the user is asked to enter a date in the form of
					//MM/DD/YYYY and then the calendar displays the Day view of the 
					//requested date including any event scheduled on that day in the order of 
					//starting time.
					System.out.print("Enter date (MM/dd/yyyy): ");
					String sDate = in.nextLine();
					DateFormat da = new SimpleDateFormat("MM/dd/yyyy");
					Date d = da.parse(sDate);
					DateFormat day = new SimpleDateFormat("EEEE, MMMM d, yyyy");
					System.out.println(day.format(d));
					cal.printEvents(d);
					break;
					
				case "E":
					//The user can browse scheduled events. The calendar displays all the 
					//events scheduled in the calendar in the order of starting date and starting time
					cal.printAllEvents();
					break;
					
				case "D":
					//User can delete an event from the Calendar. User will be first asked 
					//to enter a specific date, e.g. 06/03/2018. And then will be asked to 
					//choose from two options: [S]elected and [A]ll.
					System.out.print("Enter date (MM/dd/yyyy): ");
					String deleteDate = in.nextLine();
					System.out.printf("Current events for the date: %s\n", deleteDate);
					DateFormat del = new SimpleDateFormat("MM/dd/yyyy");
					Date delete = del.parse(deleteDate);
					cal.getCurrentEvent(delete);
					System.out.print("[S]elected and [A]ll ? ");
					String deleteChoice = in.nextLine();
					switch(deleteChoice) {
						case"S":
							System.out.print("Enter a event name to delete: ");
							String deleteName = in.nextLine();
							
							cal.removeSelected(delete, deleteName);
							break;
						case "A":
							cal.removeAll(delete);
							break;	
					}
					break;
					
				case "Q":
					//saves all the events scheduled in a text file called "events.txt"
					//in the order of starting date and starting time.
					
					 try {
				            FileOutputStream fileOutputStream = new FileOutputStream("Events.txt");
				            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(cal.arrayOfAllEvents()); //save the events
				            objectOutputStream.close();
				            fileOutputStream.close();
				            
				        } catch (NotSerializableException exception) {
				            // Output expected NotSerializeableExceptions.
				           exception.printStackTrace();
				        } catch (IOException exception) {
				            // Output unexpected IOException.
				           exception.printStackTrace();
				        }
					in.close();
					isTrue = false;
					break;
					
				default:
					System.out.println("Invalid input. Try again!");
					
			
			}
		}
	}
}

