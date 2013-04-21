package f_ingjava;

import android.os.AsyncTask;
import android.os.Build;

public abstract class Function<ArgumentType, ResultType>
{	
	public abstract ResultType call(ArgumentType arg) throws Exception;
	
	public final Function<ArgumentType, ResultType> callAsync(ArgumentType arg, Callback<ResultType> cb)
	{
		if (cb == null) throw new NullPointerException("Callback may not be null");
		this.executeAsyncCaller(arg, cb);
		return this;
	}
	
	protected final void executeAsyncCaller(ArgumentType arg, Callback<ResultType> cb)
	{
		AsyncCaller ac = new AsyncCaller(this, arg, cb);
		if (Build.VERSION.SDK_INT >= 11)
			ac.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			ac.execute();
	}
	
	private class AsyncCaller extends AsyncTask<Void, Void, ResultType>
	{
		private final Function<ArgumentType, ResultType> function;
		private final ArgumentType arg;
		private final Callback<ResultType> cb;
		public AsyncCaller(Function<ArgumentType, ResultType> function, ArgumentType arg, Callback<ResultType> cb)
		{
			this.function = function;
			this.arg = arg;
			this.cb = cb;
		}
		
		private Exception exception = null;
		protected ResultType doInBackground(Void... nothings)
		{
			try
			{
				ResultType result = function.call(arg);
				return result;
			}
			catch (Exception e)
			{
				this.exception = e;
				return null;
			}
		}
		
		protected void onPostExecute(ResultType result)
	    {
			if (this.cb != null)
			{
		    	if (this.exception != null)
		    		this.cb.onError(this.exception);
		    	else
		    		this.cb.onResult(result);
			}
	    }
	}
}
