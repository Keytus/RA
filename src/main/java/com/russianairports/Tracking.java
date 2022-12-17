package com.russianairports;

public class Tracking
{
    private String track;
    private Boolean available;

    public Tracking(String track, Boolean available)
    {
        this.track = track;
        this.available = available;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void ChangeAvailable(String selectedTrack)
    {
        if (selectedTrack.equals(track))
        {
            if (available)
            {
                available = false;
            }
            else available = true;
        }
    }
}
