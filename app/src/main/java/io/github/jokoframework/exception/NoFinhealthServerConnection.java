package io.github.jokoframework.exception;

import java.io.IOException;

/**
 * Created by joaquin on 26/10/17.
 */

public class NoFinhealthServerConnection extends IOException {

    public NoFinhealthServerConnection(String format) {
        super(format);
    }

    public NoFinhealthServerConnection(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFinhealthServerConnection(Throwable cause) {
        super(cause);
    }

    public NoFinhealthServerConnection() {
    }
}
