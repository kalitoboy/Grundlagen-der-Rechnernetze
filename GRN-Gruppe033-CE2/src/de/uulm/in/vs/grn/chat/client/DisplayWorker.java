package de.uulm.in.vs.grn.chat.client;

import java.util.LinkedList;
import java.util.Queue;

import de.uulm.in.vs.grn.chat.client.messages.Displayable;
import de.uulm.in.vs.grn.chat.client.messages.events.Event;

public class DisplayWorker extends Thread {

	private Queue<Displayable> messages;
	private boolean active = false;

	public DisplayWorker() {
		super();
		messages = new LinkedList<Displayable>();
		active = true;
	}

	@Override
	public void run() {
		while (active) {
			workOfQueue();
		}
	}

	public synchronized void addEvent(Event event) {
		messages.add(event);
		notify();

	}

	public synchronized void workOfQueue() {
		try {
			wait();
		} catch (InterruptedException e) {
			// nothing
		}
		while (!messages.isEmpty()) {
			Displayable message = messages.poll();
			message.display();
		}
	}
	
	public void disable(){
		active = false;
	}

}
