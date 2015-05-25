package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import log.Logger;

public class StringUtils
{
    public static String truncBefore(String input, String symbol)
    {
        return input.substring(input.indexOf(symbol));
    }

    public static String truncBeforeAndOverSymbol(String input, String symbol)
    {
        return input.substring(input.indexOf(symbol) + symbol.length());
    }

    public static String truncAfter(String input, String symbol)
    {
        return input.substring(0, input.indexOf(symbol));
    }

    public static int extractInt(String str)
    {
        Matcher matcher = Pattern.compile("-{0,1}\\d+\\.{0,1}\\d{0,}").matcher(str);
        if (!matcher.find())
        {
            throw new NumberFormatException("For input string [" + str + "]");
        }

        String matchStr = matcher.group();
        matchStr = matchStr.replaceAll("\\.", "");
        return Integer.parseInt(matchStr);
    }

    public static String md5(String input)
    {
        String md5 = null;

        if (input == null)
        {
            return null;
        }

        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.traceERROR(e);
        }
        return md5;
    }
}
