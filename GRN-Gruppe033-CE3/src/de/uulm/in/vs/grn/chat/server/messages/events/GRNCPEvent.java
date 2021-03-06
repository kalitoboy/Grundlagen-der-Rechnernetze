package de.uulm.in.vs.grn.chat.server.messages.events;

import java.io.IOException;
import java.io.Writer;

/**
 * represents a chat event
 * @author Marius
 *
 */
public class GRNCPEvent extends Event {

	private final String description;

	/**
	 * extends the event object with an description
	 * @param date
	 * @param description
	 */
	public GRNCPEvent(String date, String description) {
		super(date);
		this.description = description;
	}


	/**
	 * used to show all events in the command line
	 */
	@Override
	public void display() {
		System.out.println(date + " | " + description);
		
	}

	/**
	 * used to determine what to use
	 * @return
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * writes an Event
	 * @param writer
	 * @throws IOException
	 */
	@Override
	public void send(Writer writer) throws IOException {
		
			writer.write("GRNCP/0.1 EVENT\r\nDate: " + date + "\r\nDescription: " + description + "\r\n\r\n");
			writer.flush();
	}
	
}
