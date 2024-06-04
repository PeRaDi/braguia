package pt.uminho.braguia.util;

public class Result<T> {

    private T value;
    private String error;
    private Throwable throwable;

    public static <V> Result<V> ok(V value) {
        return new Result(value, null, null);
    }

    public static <V> Result<V> error(String error, Throwable throwable) {
        return new Result(null, error, throwable);
    }

    public static <V> Result<V> error(String error) {
        return error(error, null);
    }

    public static <V> Result<V> error(Throwable throwable) {
        return error(null, throwable);
    }

    private Result(T value, String error, Throwable throwable) {
        this.value = value;
        this.error = error;
        this.throwable = throwable;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean isOk() {
        return value != null && error == null && throwable == null;
    }

    public boolean isError() {
        return !isOk();
    }

    @Override
    public String toString() {
        return "Result{" +
                "value=" + value +
                ", error='" + error + '\'' +
                ", throwable=" + throwable +
                '}';
    }
}
