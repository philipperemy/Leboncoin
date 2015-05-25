package recovery;

import java.util.Date;
import log.Logger;

public class Ad
{
    @Override
    public String toString()
    {
        return "Ad [" + (dateOfPost != null ? "dateOfPost=" + Logger.sdf.format(dateOfPost) + ", " : "") + (name != null ? "name=" + name : "") + "]";
    }

    public Date   dateOfPost;
    public String name;
}
