package dev.j0hny.rconglobal;

import dev.j0hny.rconglobal.exception.AuthenticationException;
import dev.j0hny.rconglobal.logger.LoggerModule;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
* @author uJ0hny_
 * Api para conexão com o RCon
 */
public class Rcon {

    private final Object sync = new Object();
    private final Random rand = new Random();
    private LoggerModule logger=new LoggerModule();

    private int requestId;
    private Socket socket;

    private Charset charset;

    /**
     * Crie, conecte e autentique um novo objeto Rcon
     *
     * @param host Endereço do servidor rcon
     * @param port Porta do servidor rcon
     * @param password Senha do servidor rcon
     *
     */
    public Rcon(String host, int port, byte[] password) throws IOException, AuthenticationException {
        // Default charset is utf8
        this.charset = StandardCharsets.UTF_8;

        // Connect to host
        this.connect(host, port, password);
    }

    /**
     * Conecte-se a um servidor rcon
     *
     * @param host Endereço do servidor Rcon
     * @param porta Porta do servidor Rcon
     * @param senha Senha do servidor Rcon
     *
     */
    public void connect(String host, int porta, byte[] senha) throws IOException, AuthenticationException {
        if(host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço do servidor não pode ser nulo");
        }

        if(porta < 1 || porta > 65535) {
            throw new IllegalArgumentException("A porta é maior que 65535");
        }

        // Conecte-se a um servidor rcon
        synchronized(sync) {
            // Novo ID de solicitação aleatória
            this.requestId = rand.nextInt();

            // Não podemos reutilizar um socket, então precisamos de um novo
            this.socket = new Socket(host, porta);
        }

        // Envie o pacote de autenticação
        RconPacket res = this.send(RconPacket.SERVERDATA_AUTH, senha);

        // Falha na autenticaçao
        if(res.getRequestId() == -1) {
            throw new AuthenticationException("A senha foi rejeitada pelo servidor.");
        }
    }

    /**
     * Desconecte-se do servidor atual
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        synchronized(sync) {
            this.socket.close();
        }
        if (getSocket().isConnected()) {
            logger.sendMessage("A conexão com o rcon foi encerrada com sucesso!");
        }
    }

    /**
     * Envie um comando para o servidor
     *
     * @param comando O comando que irá ser enviado
     * @return A carga útil da resposta
     *
     * @throws IOException
     */
    public String comando(String comando) throws IOException {
        if(comando == null || comando.trim().isEmpty()) {
            throw new IllegalArgumentException("O comando não pode ser nulo");
        }

        RconPacket response = this.send(RconPacket.SERVERDATA_EXECCOMMAND, comando.getBytes());

        if (getSocket().isConnected()) {
            logger.sendMessageModule("COMANDO", "O comando " + comando + " foi executado no Rcon.");
        }

        return new String(response.getPayload(), this.getCharset());

    }

    private RconPacket send(int type, byte[] payload) throws IOException {
        synchronized(sync) {
            return RconPacket.send(this, type, payload);
        }
    }

    public int getRequestId() {
        return requestId;
    }

    public Socket getSocket() {
        return socket;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

}
