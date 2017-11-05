package org.brownmun.web.security;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@ConfigurationProperties("staff")
public class StaffProperties
{
    /**
     * Email addresses of ops staffers (not associated with a committee)
     */
    private List<String> ops;

    /**
     * Email addresses of committee staffers, grouped by committee shortname.
     */
    private Map<String, List<String>> committees;

    public List<String> getOps()
    {
        return ops;
    }

    public void setOps(List<String> ops)
    {
        this.ops = ops;
    }

    public Map<String, List<String>> getCommittees()
    {
        return committees;
    }

    public void setCommittees(Map<String, List<String>> committees)
    {
        this.committees = committees;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffProperties that = (StaffProperties) o;
        return Objects.equals(ops, that.ops) &&
                Objects.equals(committees, that.committees);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ops, committees);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("ops", ops)
                .add("committees", committees)
                .toString();
    }
}
