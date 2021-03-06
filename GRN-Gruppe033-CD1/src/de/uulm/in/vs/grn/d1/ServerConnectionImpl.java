package de.uulm.in.vs.grn.d1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ServerConnectionImpl implements VoidRunnerBoard.ServerConnection {

	private VoidRunnerBoard voidRunnerBoard;
	private InetSocketAddress serverEndpoint;
	private DatagramSocket clientSocket;
	private UpdateHandler updateHandler;

	public ServerConnectionImpl(VoidRunnerBoard voidRunnerBoard, InetSocketAddress serverEndpoint) throws IOException {
		this.voidRunnerBoard = voidRunnerBoard;
		this.serverEndpoint = serverEndpoint;

		clientSocket = new DatagramSocket();

		updateHandler = new UpdateHandler(voidRunnerBoard, clientSocket);
	}

	@Override
	public boolean[][] initializeGame(UUID uuid) {

		// prepare first Packet
		long leastSig = uuid.getLeastSignificantBits();
		long mostSig = uuid.getMostSignificantBits();

		ByteBuffer outBuffer = ByteBuffer.allocate(24);

		outBuffer.putLong(mostSig);
		outBuffer.putLong(leastSig);
		for (int i = 0; i < 8; i++)
			outBuffer.put((byte) -1);

		byte[] payload = outBuffer.array();

		DatagramPacket outPacket = new DatagramPacket(payload, payload.length, serverEndpoint);

		byte[] receiveData = new byte[4096];

		try {
			// send and receive
			clientSocket.send(outPacket);
			DatagramPacket incomingPacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(incomingPacket);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteBuffer buffer = ByteBuffer.wrap(receiveData);

		int posX = buffer.getInt();
		int posY = buffer.getInt();
		int boardWidth = buffer.getInt();
		int boardHeight = buffer.getInt();

		boolean[] flatBoard = new boolean[boardHeight * boardWidth];

		// read Bitmap
		for (int a = 0; a < boardHeight * boardWidth; a++) {
			byte buf = buffer.get();
			boolean[] bits = new boolean[8];
			for (int b = 7; b >= 0; b--) {
				if (a >= boardHeight * boardWidth)
					break;
				bits[b] = ((1 & (buf >>> b)) == 1); // parse bit
			}
			for (int v = 0; v < 7; v++) {
				flatBoard[a++] = bits[v];
			}
		}

		boolean[][] board = new boolean[boardWidth][boardHeight];

		for (int x = 0; x < boardWidth; x++) {
			for (int y = 0; y < boardHeight; y++) {
				board[x][y] = flatBoard[x + (y * boardWidth)];
			}
		}

		voidRunnerBoard.setInitialPosition(posX, posY);
		updateHandler.start();
		return board;

	}

	@Override
	public void sendUpdate(UUID uuid, int x, int y) {

		// prepare packet
		long leastSig = uuid.getLeastSignificantBits();
		long mostSig = uuid.getMostSignificantBits();

		ByteBuffer outBuffer = ByteBuffer.allocate(24);

		outBuffer.putLong(mostSig);
		outBuffer.putLong(leastSig);
		outBuffer.putInt(x);
		outBuffer.putInt(y);

		byte[] payload = outBuffer.array();

		DatagramPacket outPacket = new DatagramPacket(payload, payload.length, serverEndpoint);
		try {
			clientSocket.send(outPacket);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}