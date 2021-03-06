package de.uulm.in.vs.grn.chat.client.messages.responses;

/**
 * represents a byebye response
 * @author Marius
 *
 */
public class GRNCPByebye extends Response {
	public GRNCPByebye(String date) {
		super(date);
	}

	/**
	 * displays any Byebye response the client gets from the server
	 */
	@Override
	public void display() {
		System.out.println(date + " | You are logged out.");

	}

}
