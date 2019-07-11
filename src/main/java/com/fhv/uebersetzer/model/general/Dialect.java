package com.fhv.uebersetzer.model.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Dialect {

    //private String languageName;
    @Id
    private String dialectId;
    private String dialectEntry;
    private String dialectEntryLowerCase;

    private PartOfSpeech partOfSpeech;
    private LinguisticUsage linguisticUsage;
    private String refToGermanId;
    private String germanEntry;
    private List<Synonym> synonymList = new ArrayList<>();

    public Dialect(/*String languageName, */ /* String dialectId, */ String dialectEntry, PartOfSpeech partOfSpeech, LinguisticUsage linguisticUsage, String refToGermanId) {
        //this.languageName = languageName;
        //this.dialectId = dialectId;
        this.dialectEntry = dialectEntry;
        this.dialectEntryLowerCase = dialectEntry.toLowerCase();
        this.partOfSpeech = partOfSpeech;
        this.linguisticUsage = linguisticUsage;
        this.refToGermanId = refToGermanId;
    }

    public Dialect(/*String languageName, */ /*String dialectId, */ String dialectEntry, PartOfSpeech partOfSpeech, LinguisticUsage linguisticUsage) {
        //this.languageName = languageName;
        //this.dialectId = dialectId;
        this.dialectEntry = dialectEntry;
        this.dialectEntryLowerCase = dialectEntry.toLowerCase();
        this.partOfSpeech = partOfSpeech;
        this.linguisticUsage = linguisticUsage;
    }

    public Dialect(){} // empty constructor required for spring!!!
    // otherwise: java.lang.NoSuchMethodException: com.translator.uebersetzer.domain.Dialect.<init>()
/*
    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
*/

    public String getDialectId() {
        return dialectId;
    }

   /* public void setWortId(String dialectId) {
        this.dialectId = dialectId;
    }

    */

    public String getDialectEntry() {
        return dialectEntry;
    }

    public void setDialectEntry(String dialectEntry) {
        this.dialectEntry = dialectEntry;
        this.dialectEntryLowerCase = dialectEntry.toLowerCase();
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public LinguisticUsage getLinguisticUsage() {
        return linguisticUsage;
    }

    public void setLinguisticUsage(LinguisticUsage linguisticUsage) {
        this.linguisticUsage = linguisticUsage;
    }

    public String getRefToGermanId() {
        return refToGermanId;
    }

    public void setRefToGermanId(String refToGermanId) {
        this.refToGermanId = refToGermanId;
    }



    public void addSynonymId(String id, String name){

        if(dialectId.equals(id)){
            System.out.println("You cannot add a reference to yourself!");
            return;


        }

        for(Synonym oneSynonym : synonymList){
            if(oneSynonym.getEntryId().equals(id)){
                System.out.println("Eintrag bereits vorhanden!");
                return;
            }
        }
        System.out.println("wenn eintrag vorhanden wäre dürfte ich nicht printen!");
        synonymList.add(new Synonym(id, name));

    }

    @JsonIgnore
    public List<String> getSynonymIdList(){
        List<String> list = new ArrayList<>();
        for(Synonym s : synonymList){
            list.add(s.getEntryId());
        }
        return list;
    }

    public List<Synonym> getSynonymObjectList() {
        return synonymList;
    }


    public void removeSynonymObject(String id){
        for(int i = 0; i< synonymList.size(); i++){
            Synonym oneSynonym = synonymList.get(i);
            if(oneSynonym.getEntryId().equals(id)){
                synonymList.remove(i);
                return;
            }
        }
    }



    @Override
    public String toString(){
        System.out.println("----------------------------------------------");
        System.out.println("WortID: " + dialectId);
        System.out.println("WortEintrag: " + dialectEntry);
        System.out.println("WortArten (unwichtig): " + partOfSpeech);

        System.out.println("deutschReferenz: " + refToGermanId);
        //System.out.println("eigensynonyme " + Arrays.toString(synonymIds.toArray()));
        System.out.println("----------------------------------------------");
        return super.toString();


    }

    public String getDialectEntryLowerCase() {
        return dialectEntryLowerCase;
    }

    public void setDialectEntryLowerCase(String dialectEntryLowerCase) {
        this.dialectEntryLowerCase = dialectEntryLowerCase.toLowerCase();
    }

    public String getGermanEntry() {
        return germanEntry;
    }

    public void setGermanEntry(String germanEntry) {
        this.germanEntry = germanEntry;
    }
}
