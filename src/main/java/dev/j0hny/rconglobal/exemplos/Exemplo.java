package dev.j0hny.rconglobal.exemplos;

import dev.j0hny.rconglobal.AuthenticationException;
import dev.j0hny.rconglobal.Rcon;

import java.io.IOException;

public class Exemplo {
    public static Rcon rcon;

    public static void setupRCON() {

        try {
            rcon = new Rcon("127.0.0.1", 25565, "passwd".getBytes());
        } catch (IOException | AuthenticationException e) {
            throw new RuntimeException(e);
        }



    }

}
