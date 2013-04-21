package f_ingjava;

public abstract class SafeFunction<ArgumentType, ResultType> extends Function<ArgumentType, ResultType>
{
	public abstract ResultType call(ArgumentType arg);
	
	public final SafeFunction<ArgumentType, ResultType> callAsync(ArgumentType arg, final SafeCallback<ResultType> cb)
	{
		this.callAsync(arg, new Callback<ResultType>()
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
