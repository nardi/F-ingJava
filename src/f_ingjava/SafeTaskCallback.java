package f_ingjava;

public abstract class SafeTaskCallback implements SafeCallback<Void>
{
	public abstract void onResult();
	
	@Override
	public final void onResult(Void result)
	{
		this.onResult();
	}	
}
