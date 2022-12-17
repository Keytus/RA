package com.russianairports;

public class FlightInfo
{
    private int flightID;
    private String Start;
    private String End;
    private Integer Cost;
    private String StartTime;
    private String EndTime;
    private Integer EconomFree;
    private Integer FirstClassFree;
    private Integer LuxuryFree;

    public FlightInfo(int flightID,String Start, String End,Integer Cost,String StartTime,String EndTime,Integer EconomFree,Integer FirstClassFree,Integer LuxuryFree)
    {
        this.flightID = flightID;
        this.Start = Start;
        this.End =  End;
        this.Cost = Cost;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.EconomFree = EconomFree;
        this.FirstClassFree = FirstClassFree;
        this.LuxuryFree = LuxuryFree;
    }

    public Integer getCost() {
        return Cost;
    }

    public void setCost(Integer cost) {
        Cost = cost;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Integer getEconomFree() {
        return EconomFree;
    }

    public void setEconomFree(Integer economFree) {
        EconomFree = economFree;
    }

    public Integer getFirstClassFree() {
        return FirstClassFree;
    }

    public void setFirstClassFree(Integer firstClassFree) {
        FirstClassFree = firstClassFree;
    }

    public Integer getLuxuryFree() {
        return LuxuryFree;
    }

    public void setLuxuryFree(Integer luxuryFree) {
        LuxuryFree = luxuryFree;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }
    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }


}
