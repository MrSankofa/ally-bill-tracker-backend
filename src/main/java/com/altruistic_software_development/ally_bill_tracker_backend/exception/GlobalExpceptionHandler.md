The @ExceptionHandler(MethodArgumentNotValidException.class) is automatically triggered by Spring when a controller method receives a request body that fails validation due to @Valid.

When It Gets Used
If you annotate your controller like this:

```java
@PostMapping("/register")
public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
// ...
}

```
And your RegisterRequest has constraints like:

```java
public class RegisterRequest {
@NotBlank @Email
private String email;

    @NotBlank @Size(min = 8)
    private String password;
}

```
Then Spring will automatically throw a MethodArgumentNotValidException when:

email is missing or not an email

password is missing or too short

That exception is caught by this global handler:

```java

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
```
Example Request Triggering It
Sending this invalid payload:

```json
{
"email": "not-an-email",
"password": "123"
}

```
Would result in a response like:

```json
{
"email": "must be a well-formed email address",
"password": "Password must be at least 8 characters"
}

```
Summary
Triggered by: @Valid failures in controller methods.

Thrown by: Spring, not manually.

Handled in: @ControllerAdvice with @ExceptionHandler.

Result: Clean error response instead of a 500 crash or stack trace.