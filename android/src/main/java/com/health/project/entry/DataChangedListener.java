package com.health.project.entry;

public interface DataChangedListener  {



    public void onNotificationsChanged();
    public void onAttachedProcedureChanged();
    public void onCondtionsChanged();
    public void onProceduresChanged();
    public void onBillsChanged();
    public void onPersonalDataChanged();



    public void onNotificationInit();
    public void onAttachedProcedureInit();
    public void onCondtionsInit();
    public void onProceduresInit();
    public void onBillsInit();
    public void onPersonalDataInit();


}
