package conf;

public class LocationConf
{
    @Override
    public String toString()
    {
        return "LocationConf [" + (regionId != null ? "regionId=" + regionId + ", " : "") + (departmentId != null ? "departmentId=" + departmentId + ", " : "") + (zipCode != null ? "zipCode=" + zipCode : "") + "]";
    }

    private String regionId;
    private String departmentId;
    private String zipCode;

    protected LocationConf(String regionId, String departmentId, String zipCode)
    {
        setRegionId(regionId);
        setDepartmentId(departmentId);
        setZipCode(zipCode);
    }

    public String getRegionId()
    {
        return regionId;
    }

    public void setRegionId(String regionId)
    {
        this.regionId = regionId;
    }

    public String getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }
}
