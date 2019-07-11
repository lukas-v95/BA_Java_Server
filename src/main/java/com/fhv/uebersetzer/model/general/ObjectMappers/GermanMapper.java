package com.fhv.uebersetzer.model.general.ObjectMappers;

import com.fhv.uebersetzer.model.general.German;

public class GermanMapper {

    public static German performObjectCreationChecks(German german){
        boolean isValid = true;

        if (german.getGermanId() == null ){
            // it's ok
        }

        //(dialectEntry, partOfSpeech, linguisticUsage, refToGermanId);
        if (german.getGermanEntry() == null || german.getGermanEntry().equals("")){
            System.out.println("german entry must not be null!");
            return null;
        }

        return german;

    }



    public static German performObjectUpdateChecks(German german){
        if (german.getGermanId() == null || german.getGermanId().equals("")){
            return null;
        }
        return german;
    }
}
