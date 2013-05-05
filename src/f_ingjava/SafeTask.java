package f_ingjava;

public abstract class SafeTask extends Task
{
    public abstract void call();

    public final SafeTask callAsync()
    {
        this.executeAsyncCaller(null, null);
        return this;
    }
}
