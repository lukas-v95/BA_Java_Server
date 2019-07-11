package com.fhv.uebersetzer.model.general;

import java.util.ArrayList;
import java.util.List;

public class ReverseTranslation {
    private String reverseGerman2DialectLanguage =  "";
    // use set instead!
    private List<String> reverseGerman2DialectIdList = new ArrayList<>();

    public ReverseTranslation() {}

    public ReverseTranslation(String reverseGerman2DialectLanguage, String reverseGerman2DialectId) {
        this.reverseGerman2DialectLanguage = reverseGerman2DialectLanguage;
        reverseGerman2DialectIdList.add(reverseGerman2DialectId);
    }

    public String getReverseGerman2DialectLanguage() {
        return reverseGerman2DialectLanguage;
    }

    public void setReverseGerman2DialectLanguage(String reverseGerman2DialectLanguage) {
        this.reverseGerman2DialectLanguage = reverseGerman2DialectLanguage;
    }

    public List<String> getReverseGerman2DialectIdList() {
        return reverseGerman2DialectIdList;
    }

    public void addReverseGerman2DialectId(String reverseGerman2DialectId) {
        if (!reverseGerman2DialectIdList.contains(reverseGerman2DialectId)){
            reverseGerman2DialectIdList.add(reverseGerman2DialectId);
        }

    }

    public boolean existsId(String someId){
        if(reverseGerman2DialectIdList.contains(someId)){
            return true;
        }

        return false;
    }
}
