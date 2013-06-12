package f_ingjava;

public abstract class SafeFunction<ArgumentType, ResultType> extends Function<ArgumentType, ResultType>
{
	public abstract ResultType call(ArgumentType arg);

    public final SafeFunction<ArgumentType, ResultType> callAsync(ArgumentType arg, final SafeCallback<? super ResultType> cb)
    {
    	if (cb == null)
            throw new NullPointerException("Callback may not be null");
        this.executeAsyncCaller(arg, new Callback<ResultType>()
        {
            public void onResult(ResultType result)
            {
                cb.onResult(result);
            }
            public void onError(Exception error)
            {
            }
        });
        return this;
    }
}
