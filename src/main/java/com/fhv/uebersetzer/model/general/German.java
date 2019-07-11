package com.fhv.uebersetzer.model.general;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "germancollection")
public class German{

    @Id
    private String germanId;
    private String germanEntry;
    private String germanEntryLowerCase;

    //private PartOfSpeech wordClass;
    //private LinguisticUsage linguisticUsage;
    private List<ReverseTranslation> reverseTranslations = new ArrayList<>();

    public German(/*String germanId, */String germanEntry) {
       // this.germanId = germanId;
        this.germanEntry = germanEntry;
        this.germanEntryLowerCase = germanEntry.toLowerCase();
       // this.reverseTranslations = reverseTranslations;
    }

    public German(String germanEntry, List<ReverseTranslation> reverseTranslations) {
        //this.germanId = germanId;
        this.germanEntry = germanEntry;
        this.germanEntryLowerCase = germanEntry.toLowerCase();
        this.reverseTranslations = reverseTranslations;
    }

    public German (){ }



    public String getGermanId() {
        return germanId;
    }

    //public void setDeutschesWortId(String germanId) {
    //    this.germanId = germanId;
    //}

    public String getGermanEntry() {
        return germanEntry;
    }

    public void setGermanEntry(String germanEntry) {
        this.germanEntry = germanEntry;
        this.germanEntryLowerCase = germanEntry.toLowerCase();
    }

    public void setOneLanguageRetourUebersetzung(ReverseTranslation r){
        reverseTranslations.add(r);
    }


    public void setReverseTranslation(String otherCollection, String otherId){
        boolean collectionExists = false;
        int indexOfCollection = -1;

        System.out.println("checking for existing dialectCollection...");
        for( int i = 0; i < reverseTranslations.size(); i++ ){
            if(reverseTranslations.get(i).getReverseGerman2DialectLanguage().equalsIgnoreCase(otherCollection)){
                collectionExists = true;
                indexOfCollection = i;
                System.out.println("dialect collection exists.");

                //List<String> existingEntries = reverseTranslations.get(i).getReverseGerman2DialectIdList();
                //existingEntries.contains(otherId);


               /*
                if(existingEntries.contains(otherId)){
                    System.out.println("gleicher ID-eintrag bereits vorhanden. 2x derselbe eintrag auf dieselbe dialectcollection ist nicht erlaubt!" +
                            " Nothing to do here.");
                    return;
                } else {

                */

                    //System.out.println("collection existiert bereits. NEU: Es wird ein bestehender eintrag nicht überschrieben, sondern es wird ein neuer Id-Eintrag einfach hinzugefügt!");;


                    // füge noch fehlende Id der deutschen collection hinzu!
                    reverseTranslations.get(indexOfCollection).addReverseGerman2DialectId(otherId);
                //}
            }
        }

        // erzeuge collection und füge eintrag hinzu!
        if (!collectionExists) {
            System.out.println("collection existiert nicht, deshalb wird neue erzeugt.");
            reverseTranslations.add(new ReverseTranslation(otherCollection, otherId));
        }

    }
/*
    // TODO DELETE completely!
    public String getOneRetourUebersetzungId(String language){
        for (int i = 0; i < reverseTranslations.size(); i++ ){
            if (reverseTranslations.get(i).getReverseGerman2DialectLanguage().equalsIgnoreCase(language)){
                return reverseTranslations.get(i).getReverseGerman2DialectId();
            }
        }
        return null;
    }

 */

    public List<String> getAllRetourUebersetzungIdsPerDialect(String language){
        for(int i = 0; i < reverseTranslations.size(); i++ ){
            if (reverseTranslations.get(i).getReverseGerman2DialectLanguage().equalsIgnoreCase(language)){

                return reverseTranslations.get(i).getReverseGerman2DialectIdList();
            }
        }
        return null; // besser als leere liste?
    }



    public void removeOneRetourUebersetzung(String language){
        for (int i = 0; i < reverseTranslations.size(); i++ ){
            ReverseTranslation reverseTranslation = reverseTranslations.get(i);
            if ( reverseTranslation.getReverseGerman2DialectLanguage().equalsIgnoreCase(language)){
                reverseTranslations.remove(i);
            }
        }
    }

    public List<ReverseTranslation> getReverseTranslations(){
        return reverseTranslations;
    }

    public String getGermanEntryLowerCase() {
        return germanEntryLowerCase;
    }

    public void setGermanEntryLowerCase(String germanEntryLowerCase) {
        this.germanEntryLowerCase = germanEntryLowerCase.toLowerCase();
    }
}
