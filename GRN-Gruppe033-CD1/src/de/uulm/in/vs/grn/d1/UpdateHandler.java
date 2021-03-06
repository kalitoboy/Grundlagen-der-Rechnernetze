package de.uulm.in.vs.grn.d1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UpdateHandler extends Thread {

    private VoidRunnerBoard voidRunnerBoard;
    private DatagramSocket clientSocket;

    public UpdateHandler(VoidRunnerBoard voidRunnerBoard, DatagramSocket clientSocket) {
        this.voidRunnerBoard = voidRunnerBoard;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        
    	//always receive packets
    	while(true){
    	byte[] receiveData = new byte[254];

		try {
			
			DatagramPacket incomingPacket = new DatagramPacket(receiveData, receiveData.length);

			clientSocket.receive(incomingPacket);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteBuffer buffer = ByteBuffer.wrap(receiveData);
    	
		int nrOfUpdates = buffer.getInt();
		for(int i=0; i<nrOfUpdates; i++){
			UUID uuid = new UUID(buffer.getLong(), buffer.getLong());
			boolean alive = buffer.get()!=0;
			int x=buffer.getInt();
			int y=buffer.getInt();
			voidRunnerBoard.handleUpdate(uuid, alive, x, y);
		}
    	}
    	
    }

}