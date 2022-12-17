package com.russianairports;

public class Ticket
{
    private int flightID;
    private String ServiceClass;
    private int Cost;
    private String Start;
    private String End;
    private String Login;
    private String StartTime;

    public Ticket(int flightID,
                  String ServiceClass,
                  int Cost,
                  String Start,
                  String End,
                  String Login,
                  String StartTime)
    {
        this.flightID = flightID;
        this.ServiceClass = ServiceClass;
        this.Cost = Cost;
        this.Start = Start;
        this.End = End;
        this.Login = Login;
        this.StartTime = StartTime;
    }

    public String getServiceClass() {
        return ServiceClass;
    }

    public void setServiceClass(String serviceClass) {
        ServiceClass = serviceClass;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        this.Cost = cost;
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

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }


}
