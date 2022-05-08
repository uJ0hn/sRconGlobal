package dev.j0hny.rconglobal;



import java.io.*;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RconPacket {

    public static final int SERVERDATA_EXECCOMMAND = 2;
    public static final int SERVERDATA_AUTH = 3;

    private int requestId;
    private int type;
    private byte[] payload;

    private RconPacket(int requestId, int type, byte[] payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    /**
     * Envie um pacote Rcon e busque a resposta
     *
     * @param rcon Instância Rcon
     * @param type O tipo de pacote
     * @param payload útil A carga útil (senha, comando, etc.)
     * @return Um objeto RconPacket contendo a resposta
     *
     */
    protected static RconPacket send(Rcon rcon, int type, byte[] payload) throws IOException {
        try {
            RconPacket.write(rcon.getSocket().getOutputStream(), rcon.getRequestId(), type, payload);
        }
        catch(SocketException se) {
            // Fecha o socket se algo acontecer
            rcon.getSocket().close();

            // Relançar a exceção
            throw se;
        }

        return RconPacket.read(rcon.getSocket().getInputStream());
    }

    /**
     * Escreva um pacote rcon em um fluxo de saída
     *
     * @param out O OutputStream para escrever
     * @param requestId O id da solicitação
     * @param type O tipo de pacote
     * @param payload A carga útil
     *
     */
    private static void write(OutputStream out, int requestId, int type, byte[] payload) throws IOException {
        int bodyLength = RconPacket.getBodyLength(payload.length);
        int packetLength = RconPacket.getPacketLength(bodyLength);

        ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(bodyLength);
        buffer.putInt(requestId);
        buffer.putInt(type);
        buffer.put(payload);

        // Terminadores de bytes nulos
        buffer.put((byte)0);
        buffer.put((byte)0);

        // Uau!
        out.write(buffer.array());
        out.flush();
    }

    /**
     * Leia um pacote rcon de entrada
     *
     * @param in InputStream para ler
     * @return O RconPacket lido
     *
     */
    private static RconPacket read(InputStream in) throws IOException {
        // O cabeçalho tem 3 inteiros de 4 bytes
        byte[] header = new byte[4 * 3];

        // Leia os 3 ints
        in.read(header);

        try {
            // Use um bytebuffer em little endian para ler os 3 primeiros ints
            ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            int length = buffer.getInt();
            int requestId = buffer.getInt();
            int type = buffer.getInt();

            // O tamanho da carga útil pode ser calculado agora que temos seu comprimento
            byte[] payload = new byte[length - 4 - 4 - 2];

            DataInputStream dis = new DataInputStream(in);

            // Leia a carga completa
            dis.readFully(payload);

            // Leia os bytes nulos
            dis.read(new byte[2]);

            return new RconPacket(requestId, type, payload);
        }
        catch(BufferUnderflowException | EOFException e) {
            throw new MalformedPacketException("Não é possível ler o pacote inteiro");
        }
    }

    private static int getPacketLength(int bodyLength) {
        // 4 bytes para comprimento + x bytes para comprimento do corpo
        return 4 + bodyLength;
    }

    private static int getBodyLength(int payloadLength) {
        // 4 bytes para requestId, 4 bytes para tipo, x bytes para carga útil, 2 bytes para dois bytes nulos
        return 4 + 4 + payloadLength + 2;
    }

}
class MalformedPacketException extends IOException {

    public MalformedPacketException(String message) {
        super(message);
    }

}
