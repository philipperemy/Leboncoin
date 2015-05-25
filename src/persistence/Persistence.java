package persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import log.Logger;
import run.main.App;
import utils.NameFactory;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public final class Persistence
{
    // 0 : hash
    // 1 : web id

    private static Persistence persistence = new Persistence();

    public static Persistence getInstance()
    {
        return persistence;
    }

    private Persistence()
    {
    }

    public List<PersistedObject> getPersistedObjects()
    {
        List<PersistedObject> persistedObjects = new ArrayList<>();
        try
        {
            CSVReader reader = new CSVReader(new FileReader(NameFactory.PERSISTENCE_CSV));
            List<String[]> allEntries = reader.readAll();
            reader.close();
            for (String[] entry : allEntries)
            {
                persistedObjects.add(new PersistedObject(entry[1], entry[0]));
            }
        }
        catch (Exception e)
        {
            App.kill(e);
        }

        return persistedObjects;
    }

    private synchronized boolean persist(String id, String hash, boolean append) throws IOException
    {
        // Append
        CSVReader reader = new CSVReader(new FileReader(NameFactory.PERSISTENCE_CSV));
        List<String[]> allEntries = reader.readAll();
        for (String[] entry : allEntries)
        {
            if (entry[0].equals(hash))
            {
                reader.close();
                return false;
            }
        }
        reader.close();

        CSVWriter writer = new CSVWriter(new FileWriter(NameFactory.PERSISTENCE_CSV, append));
        String[] entries = new String[2];
        entries[0] = hash;
        entries[1] = id;
        writer.writeNext(entries);
        writer.close();
        return true;
    }

    public synchronized boolean persist(PersistedObject persistedObject)
    {
        boolean result = false;
        try
        {
            result = persist(persistedObject.id, persistedObject.hash, true);
        }
        catch (IOException e)
        {
            App.kill(e);
        }

        return result;
    }

    public synchronized void flush()
    {
        try
        {
            CSVWriter writer = new CSVWriter(new FileWriter(NameFactory.PERSISTENCE_CSV, false));
            writer.close();
        }
        catch (IOException e)
        {
            Logger.traceERROR(e);
        }

        if (getSize() != 0)
        {
            Logger.traceERROR("PERSISTENCE - Persistence flush failed. Program will exit.");
            App.kill();
        }
    }

    public synchronized int getSize()
    {
        try
        {
            CSVReader reader = new CSVReader(new FileReader(NameFactory.PERSISTENCE_CSV));
            List<String[]> entries = reader.readAll();
            reader.close();
            return entries.size();
        }
        catch (IOException e)
        {
            Logger.traceERROR(e);
        }
        return -1;
    }

    public synchronized void update(PersistedObject persistedObject)
    {
        int oldSize = getSize();
        remove(persistedObject);
        persist(persistedObject);
        int newSize = getSize();

        if (oldSize != newSize)
        {
            Logger.traceERROR("PERSISTENCE - Error updating the object : " + persistedObject + ". It does not exist in the persistence. Program will exit.");
            App.kill();
        }
    }

    public synchronized void remove(PersistedObject persistedObject)
    {
        try
        {
            CSVReader reader = new CSVReader(new FileReader(NameFactory.PERSISTENCE_CSV));
            List<String[]> entries = reader.readAll();
            Map<String, String> entriesMap = new HashMap<String, String>();
            for (String[] entry : entries)
            {
                entriesMap.put(entry[0], entry[1]);
            }

            entriesMap.keySet().remove(persistedObject.hash);
            flush();
            for (Entry<String, String> entry : entriesMap.entrySet())
            {
                persist(entry.getValue(), entry.getKey(), true);
            }
            reader.close();
        }
        catch (Exception e)
        {
            Logger.traceERROR(e);
        }
    }

}
