package f_ingjava;

public interface Callback<ResultType> extends SafeCallback<ResultType>
{
    public void onResult(ResultType result);

    public void onError(Exception error);
}
