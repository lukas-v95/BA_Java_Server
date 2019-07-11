package com.fhv.uebersetzer.model.general.ObjectMappers;

import com.fhv.uebersetzer.model.general.Dialect;
import com.fhv.uebersetzer.model.general.LinguisticUsage;
import com.fhv.uebersetzer.model.general.PartOfSpeech;

public class DialectMapper {


    public static Dialect performObjectCreationChecks(Dialect dialect){

        if (dialect.getDialectId() == null ){
            // it's ok
        }

        //(dialectEntry, partOfSpeech, linguisticUsage, refToGermanId);
        if (dialect.getDialectEntry() == null || dialect.getDialectEntry().equals("")){
            System.out.println("dialect entry must not be null!");
            return null;
        }

        if (dialect.getPartOfSpeech() == null){
            dialect.setPartOfSpeech(new PartOfSpeech());
        }

        if (dialect.getLinguisticUsage() == null){
            dialect.setLinguisticUsage(new LinguisticUsage());
        }

        return dialect;

    }


    public static Dialect performObjectUpdateChecks(Dialect dialect){

        if (dialect.getDialectId() == null ){
            System.err.println("dialect Id is missing!");
            return null;
        }

        //(dialectEntry, partOfSpeech, linguisticUsage, refToGermanId);
        if (dialect.getDialectEntry() == null || dialect.getDialectEntry().equals("")){
            System.out.println("dialect entry must not be null!");
            return null;
        }

        if (dialect.getPartOfSpeech() == null){
            dialect.setPartOfSpeech(new PartOfSpeech());
        }

        if (dialect.getLinguisticUsage() == null){
            dialect.setLinguisticUsage(new LinguisticUsage());
        }

        return dialect;
    }
}
