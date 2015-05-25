package persistence;

import log.Logger;
import run.main.App;

public class PersistedObject
{

    @Override
    public String toString()
    {
        return "PersistedObject [" + (id != null ? "id=" + id + ", " : "") + (hash != null ? "hash=" + hash : "") + "]";
    }

    public PersistedObject()
    {
    }

    public PersistedObject(String id, String hash)
    {
        super();
        this.id = id;

        if (hash.length() != 32 && hash.length() != 31)
        {
            Logger.traceERROR("This is not a correct hash : " + hash + ".");
            App.kill();
        }

        this.hash = hash;
    }

    // id of the ad
    public String id;

    // Hash of the ConfItem
    public String hash;
}
