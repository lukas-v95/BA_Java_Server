package com.fhv.uebersetzer.model.general;

public class SearchResult {

    private Dialect dialectA;
    private German german;
    private Dialect dialectB;
    //private List<Dialect> synonyms;

    public SearchResult( ) { }

    public SearchResult(Dialect dialectA, German german, Dialect dialectB/*, List<Dialect> synonyms*/) {
        this.dialectA = dialectA;
        this.german = german;
        this.dialectB = dialectB;
        //this.synonyms = synonyms;
    }

    public Dialect getDialectA() {
        return dialectA;
    }

    public void setDialectA(Dialect dialectA) {
        this.dialectA = dialectA;
    }

    public German getGerman() {
        return german;
    }

    public void setGerman(German german) {
        this.german = german;
    }

    public Dialect getDialectB() {
        return dialectB;
    }

    public void setDialectB(Dialect dialectB) {
        this.dialectB = dialectB;
    }

    //public List<Dialect> getSynonyms() {
      //  return synonyms;
    //}

    //public void setSynonyms(List<Dialect> synonyms) {
      //  this.synonyms = synonyms;
    //}
}
