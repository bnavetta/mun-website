package org.brownmun.web.admin.printing;

import com.google.common.base.MoreObjects;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * Models the print request submission form.
 */
public class PrintSubmission
{
    private int numCopies;
    private String deliveryLocation;
    private String requester;
    private MultipartFile file;

    public int getNumCopies()
    {
        return numCopies;
    }

    public void setNumCopies(int numCopies)
    {
        this.numCopies = numCopies;
    }

    public String getDeliveryLocation()
    {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation)
    {
        this.deliveryLocation = deliveryLocation;
    }

    public String getRequester()
    {
        return requester;
    }

    public void setRequester(String requester)
    {
        this.requester = requester;
    }

    public MultipartFile getFile()
    {
        return file;
    }

    public void setFile(MultipartFile file)
    {
        this.file = file;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintSubmission that = (PrintSubmission) o;
        return numCopies == that.numCopies &&
                Objects.equals(deliveryLocation, that.deliveryLocation) &&
                Objects.equals(requester, that.requester) &&
                Objects.equals(file, that.file);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(numCopies, deliveryLocation, requester, file);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("numCopies", numCopies)
                .add("deliveryLocation", deliveryLocation)
                .add("requester", requester)
                .add("file", file)
                .toString();
    }
}
