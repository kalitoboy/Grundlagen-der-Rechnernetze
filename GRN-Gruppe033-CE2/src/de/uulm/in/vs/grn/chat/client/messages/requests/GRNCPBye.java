package de.uulm.in.vs.grn.chat.client.messages.requests;

import java.io.IOException;
import java.io.Writer;

import de.uulm.in.vs.grn.chat.client.GRNCP;

/**
 * represents a bye request
 * @author Marius
 *
 */
public class GRNCPBye extends Request {
	
	private final String byeMessage = "BYE " + GRNCP.PROTOCOL_VERSION + "\r\n\r\n";
	
	/**
	 * sends a disconnect request to the server
	 */
	public GRNCPBye() {
		super();
	}

	
	@Override
	public void send(Writer writer) throws IOException {
		writer.write(byeMessage);
		writer.flush();
	}
}
