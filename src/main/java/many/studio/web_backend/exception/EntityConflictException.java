package many.studio.web_backend.exception;

public class EntityConflictException extends RuntimeException {
  public EntityConflictException(String message) {
    super(message);
  }
}
