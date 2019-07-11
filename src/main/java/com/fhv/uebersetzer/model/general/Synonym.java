package com.fhv.uebersetzer.model.general;

public class Synonym {
    private String entryId;
    private String entryName;

    public Synonym(){}


    public Synonym(String entryId, String entryName) {
        this.entryId = entryId;
        this.entryName = entryName;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
