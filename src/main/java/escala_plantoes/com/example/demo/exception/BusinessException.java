package escala_plantoes.com.example.demo.exception;

public abstract class BusinessException extends RuntimeException {

    protected BusinessException(String message) {
        super(message);
    }
}
