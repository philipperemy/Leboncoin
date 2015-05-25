package conf;

public class ConfItem
{
    public String       adtitle;
    public LocationConf location;
    public String       description;
    public Integer      limitPrice;
    public String       linkPhoto1;
    public String       linkPhoto2;
    public String       linkPhoto3;
    public Integer      daysCountBeforeRefresh;
    
    @Override
    public String toString()
    {
        return "ConfItem [" + (adtitle != null ? "adtitle=" + adtitle + ", " : "") + (location != null ? "location=" + location + ", " : "") + (description != null ? "description=" + description + ", " : "")
            + (limitPrice != null ? "limitPrice=" + limitPrice + ", " : "") + (linkPhoto1 != null ? "linkPhoto1=" + linkPhoto1 + ", " : "") + (linkPhoto2 != null ? "linkPhoto2=" + linkPhoto2 + ", " : "")
            + (linkPhoto3 != null ? "linkPhoto3=" + linkPhoto3 + ", " : "") + (daysCountBeforeRefresh != null ? "daysCountBeforeRefresh=" + daysCountBeforeRefresh : "") + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adtitle == null) ? 0 : adtitle.hashCode());
        result = prime * result + ((daysCountBeforeRefresh == null) ? 0 : daysCountBeforeRefresh.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((limitPrice == null) ? 0 : limitPrice.hashCode());
        result = prime * result + ((linkPhoto1 == null) ? 0 : linkPhoto1.hashCode());
        result = prime * result + ((linkPhoto2 == null) ? 0 : linkPhoto2.hashCode());
        result = prime * result + ((linkPhoto3 == null) ? 0 : linkPhoto3.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfItem other = (ConfItem) obj;
        if (adtitle == null)
        {
            if (other.adtitle != null)
                return false;
        }
        else if (!adtitle.equals(other.adtitle))
            return false;
        if (daysCountBeforeRefresh == null)
        {
            if (other.daysCountBeforeRefresh != null)
                return false;
        }
        else if (!daysCountBeforeRefresh.equals(other.daysCountBeforeRefresh))
            return false;
        if (description == null)
        {
            if (other.description != null)
                return false;
        }
        else if (!description.equals(other.description))
            return false;
        if (limitPrice == null)
        {
            if (other.limitPrice != null)
                return false;
        }
        else if (!limitPrice.equals(other.limitPrice))
            return false;
        if (linkPhoto1 == null)
        {
            if (other.linkPhoto1 != null)
                return false;
        }
        else if (!linkPhoto1.equals(other.linkPhoto1))
            return false;
        if (linkPhoto2 == null)
        {
            if (other.linkPhoto2 != null)
                return false;
        }
        else if (!linkPhoto2.equals(other.linkPhoto2))
            return false;
        if (linkPhoto3 == null)
        {
            if (other.linkPhoto3 != null)
                return false;
        }
        else if (!linkPhoto3.equals(other.linkPhoto3))
            return false;
        if (location == null)
        {
            if (other.location != null)
                return false;
        }
        else if (!location.equals(other.location))
            return false;
        return true;
    }
}
