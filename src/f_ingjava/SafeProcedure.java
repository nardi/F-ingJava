package f_ingjava;

public abstract class SafeProcedure<ResultType> extends SafeFunction<Void, ResultType>
{
	public abstract ResultType call();

	@Override
	public final ResultType call(Void arg)
	{
		return this.call();
	}
	
	public final SafeProcedure<ResultType> callAsync(SafeCallback<ResultType> cb)
	{
		this.callAsync(null, cb);
		return this;
	}
}
