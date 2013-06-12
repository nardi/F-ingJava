package f_ingjava;

public abstract class Procedure<ResultType> extends Function<Void, ResultType>
{
	public abstract ResultType call() throws Exception;

    @Override
    public final ResultType call(Void arg) throws Exception
    {
        return this.call();
    }

    public final Procedure<ResultType> callAsync(Callback<? super ResultType> cb)
    {
        this.callAsync(null, cb);
        return this;
    }
}
