package many.studio.web_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NonAuthorizedException extends RuntimeException {
    public NonAuthorizedException(String message) {
        super(message);
    }
}
