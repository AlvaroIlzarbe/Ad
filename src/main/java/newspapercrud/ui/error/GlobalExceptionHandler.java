package newspapercrud.ui.error;


import newspapercrud.domain.error.AppError;
import newspapercrud.domain.error.DataBaseError;
import newspapercrud.domain.error.DuplicatedUsernameError;
import newspapercrud.domain.error.ForeignKeyError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DuplicatedUsernameError.class)
    public ResponseEntity<String> handleForeignKetException(DuplicatedUsernameError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(ForeignKeyError.class)
    public ResponseEntity<String> handleForeignKetException(ForeignKeyError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AppError.class)
    public ResponseEntity<String> handleForeignKetException(AppError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(DataBaseError.class)
    public ResponseEntity<String> handleForeignKetException(DataBaseError e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }


}
