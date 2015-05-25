package recovery;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import log.Logger;
import utils.MonthsMap;
import utils.NameFactory;
import utils.StringUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import connectivity.Client;
import connectivity.LinkFactory;

public class AdRetrieval
{
	private static AdRetrievalCache cache = new AdRetrievalCache();
	
    public static Ad retrieveAd(String id) throws UnknownAdException
    {
        try
        {
        	Ad ad = new Ad();
        	String content = cache.getById(id);
        	String formPageXmlStr = null;
        	if(content == null)
        	{
                WebClient client = Client.get();
                String pageName = LinkFactory.AD_WINE_LINK + id + ".htm";
                Logger.traceINFO_NoNewLine("GET: " + pageName);
                final HtmlPage formPage = client.getPage(pageName);
                Logger.traceINFO_NoBaseLine(" [DONE]");
                formPageXmlStr = formPage.asXml();
                cache.updateId(id, formPageXmlStr);
        	}
        	else
        	{
        		formPageXmlStr = content;
        	}

            if (formPageXmlStr.contains("Annonce introuvable") && formPageXmlStr.contains("Cette annonce est d"))
            {
                throw new UnknownAdException();
            }

            formPageXmlStr = StringUtils.truncBefore(formPageXmlStr, "upload_by");
            String name = formPageXmlStr.substring(formPageXmlStr.indexOf("'utilisateur_v2','N')\">") + "'utilisateur_v2','N')\">".length(), formPageXmlStr.indexOf("</a>")).trim();
            name = name.trim();
            formPageXmlStr = StringUtils.truncBefore(formPageXmlStr, "Mise en ligne");
            formPageXmlStr = StringUtils.truncAfter(formPageXmlStr, ".");
            String dateStr = formPageXmlStr;

            ad.dateOfPost = extractDate(dateStr);
            ad.name = name;
            return ad;
        }
        catch (FailingHttpStatusCodeException | IOException e)
        {
            throw new UnknownAdException();
        }
    }

    public static Date extractDate(String dateStr)
    {
        // le 9 février à 23:11.
        int dayOfMonth = StringUtils.extractInt(dateStr);
        dateStr = StringUtils.truncBeforeAndOverSymbol(dateStr, String.valueOf(dayOfMonth));

        // février à 23:11.
        dateStr = dateStr.trim();
        String month = StringUtils.truncAfter(dateStr, " ").trim();
        Integer monthId = MonthsMap.getMonthId(month);

        // à 23:11.
        int hourTime = StringUtils.extractInt(dateStr);
        dateStr = StringUtils.truncBeforeAndOverSymbol(dateStr, String.valueOf(hourTime));

        // 11.
        int minTime = StringUtils.extractInt(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, NameFactory.ORIG_YEAR);
        calendar.set(Calendar.MONTH, monthId);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourTime);
        calendar.set(Calendar.MINUTE, minTime);

		return calendar.getTime();
	}
}
