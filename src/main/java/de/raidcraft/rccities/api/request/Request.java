package de.raidcraft.rccities.api.request;

/**
 * @author Philip Urban
 */
public interface Request {

    public boolean isRejected();

    public String getRejectReason();

    public void accept();

    public void reject(String reason);
}
