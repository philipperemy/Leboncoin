package conf;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import log.Logger;
import run.main.App;
import utils.NameFactory;
import utils.StringUtils;
import au.com.bytecode.opencsv.CSVReader;

public class ConfReader
{
    private static volatile ConfReader confReader = new ConfReader();

    private static boolean             firstRead  = true;

    private List<ConfItem>             confItems  = null;

    private ConfReader()
    {

    }

    public static ConfReader getInstance()
    {
        return confReader;
    }

    public List<ConfItem> getConfItems()
    {
        if (confItems == null)
        {
            try
            {
                read();
            }
            catch (Exception e)
            {
                App.kill(e);
            }
        }

        return confItems;
    }

    // ad title, location, description, price, link photo 1, link photo 2, link photo 3, refresh
    private void read() throws IOException
    {
        if (firstRead)
        {
            CSVReader reader = new CSVReader(new FileReader(NameFactory.CONF_PATH_FILE));
            List<String[]> items = reader.readAll();
            confItems = new ArrayList<>();
            for (String[] item : items)
            {
                ConfItem confItem = createConfItem(item);
                Logger.traceINFO(confItem.toString());
                confItems.add(confItem);
            }

            reader.close();
            firstRead = false;
        }
    }

    private ConfItem createConfItem(String[] item)
    {
        assert item.length == 9;
        ConfItem confItem = new ConfItem();
        confItem.adtitle = item[0];

        String locationStr = item[1];
        LocationConf locationConf = null;
        switch (locationStr.trim())
        {
            case "gradignan":
                locationConf = new GradignanLocationConf();
                break;

            case "paris":
                locationConf = new ParisLocationConf();
                break;

            default:
                throw new RuntimeException("Invalid location : " + locationStr);
        }

        confItem.location = locationConf;
        confItem.description = item[2];
        confItem.limitPrice = Integer.parseInt(item[3].trim());
        confItem.linkPhoto1 = item[4];
        confItem.linkPhoto2 = item[5];
        confItem.linkPhoto3 = item[6];
        confItem.daysCountBeforeRefresh = Integer.parseInt(item[7].trim());
        return confItem;
    }
    
    public static ConfItem getConfItemFromHash(ConfReader confReader, String md5hash)
    {
        for (ConfItem confItem : confReader.getConfItems())
        {
            if (StringUtils.md5(confItem.adtitle).equals(md5hash))
            {
                return confItem;
            }
        }

        return null;
    }

}
