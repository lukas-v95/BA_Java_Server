package com.fhv.uebersetzer.model.general;

import com.fhv.uebersetzer.model.general.enums.PartOfSpeechEnum;

import java.util.ArrayList;
import java.util.List;

public class PartOfSpeech {


    List<PartOfSpeechEnum> partOfSpeechs = new ArrayList<>();



    public PartOfSpeech() { }


    public void addOnePartOfSpeech(PartOfSpeechEnum e){
        if(!partOfSpeechs.contains(e)){
            partOfSpeechs.add(e);
        }

    }

    public void removePartOfSpeech(PartOfSpeechEnum e){
        partOfSpeechs.remove(e);

    }




    public void addMultiplePartOfSpeechs(List<PartOfSpeechEnum> list){
        for(int i = 0; i < list.size(); i++){
            if(!partOfSpeechs.contains(list.get(i))){
                partOfSpeechs.add(list.get(i));
            }
        }
    }

    public List<PartOfSpeechEnum> getPartOfSpeechs(){
        return partOfSpeechs;
    }

    public void setPartOfSpeechs(List<PartOfSpeechEnum> partOfSpeechs) {
        this.partOfSpeechs = partOfSpeechs;
    }
}
