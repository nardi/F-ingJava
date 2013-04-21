package f_ingjava;

public abstract class Task extends Function<Void, Void>
{
	public abstract void call() throws Exception;
	
	@Override
	public final Void call(Void arg) throws Exception
	{
		this.call();
		return null;
	}
	
	public final Task callAsync(Callback<Void> cb)
	{
		this.callAsync(null, cb);
		return this;
	}
}
