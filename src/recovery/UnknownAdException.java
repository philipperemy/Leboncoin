package recovery;

public class UnknownAdException extends Exception
{
    public UnknownAdException()
    {
        super();
    }

    public UnknownAdException(Exception e)
    {
        super(e);
    }

    private static final long serialVersionUID = 1L;
}
