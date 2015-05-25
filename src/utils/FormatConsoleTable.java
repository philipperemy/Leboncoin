package utils;

import java.util.ArrayList;
import java.util.List;
import log.Logger;

public class FormatConsoleTable
{
    private List<Row> rows = new ArrayList<>();

    private class Row
    {
        String adTitle;
        String adPrice;
        String status;

        public Row(String adTitle, String adPrice, String status)
        {
            this.adTitle = adTitle;
            this.adPrice = adPrice;
            this.status = status;
        }
    }

    public void print(String adTitle, String adPrice, String status)
    {
        rows.add(new Row(adTitle, adPrice, status));
    }

    public void flush()
    {
        String formatString = "| %55s | %9s | %18s |";
        // Mind that all rows have the same sizes.
        for (int i = 0; i < rows.size(); i++)
        {
            Row row = rows.get(i);
            String strOut = String.format(formatString, row.adTitle, row.adPrice, row.status);

            // First or last row
            if (i == 0)
            {
                Logger.traceINFO(org.apache.commons.lang3.StringUtils.repeat("_", strOut.length()));
            }

            Logger.traceINFO(strOut);

            if (i == rows.size() - 1)
            {
                Logger.traceINFO(org.apache.commons.lang3.StringUtils.repeat("_", strOut.length()));
            }
        }

    }
}
