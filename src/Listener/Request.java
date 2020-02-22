package Listener;

public class Request {
    protected String id;
    protected String devicenumber;
    protected String actioncommand;
    protected String triggeredby;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getDeviceNumber() {
        return devicenumber;
    }

    public void setDeviceNumber(String devicenumber) {
        this.devicenumber = devicenumber;
    } 
    
    public String getActionCommand() {
        return actioncommand;
    }

    public void setActionCommand(String actioncommand) {
        this.actioncommand = actioncommand;
    } 
    
    public String getTriggeredBy() {
        return triggeredby;
    }

    public void setTriggeredBy(String triggeredby) {
        this.triggeredby = triggeredby;
    }
}