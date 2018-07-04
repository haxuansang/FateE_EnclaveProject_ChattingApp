package application.android.com.fatee.models;

public interface Callback<T> {
    void onSuccess(T result);

    void onError(Throwable what);
}
