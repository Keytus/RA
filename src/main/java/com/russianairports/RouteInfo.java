package com.russianairports;

public class RouteInfo
{

    private Integer routeID;
    private String Start;
    private String End;
    private Integer tranzCount;

    public Integer getTranzCount() {
        return tranzCount;
    }

    public void setTranzCount(Integer tranzCount) {
        this.tranzCount = tranzCount;
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

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public RouteInfo(Integer routeID,String Start,String End, Integer tranzCount)
    {
        this.routeID = routeID;
        this.Start = Start;
        this.End = End;
        this.tranzCount = tranzCount;
    }
}
