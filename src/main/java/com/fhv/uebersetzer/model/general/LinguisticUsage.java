package com.fhv.uebersetzer.model.general;

import com.fhv.uebersetzer.model.general.enums.LinguisticEnum;

import java.util.ArrayList;
import java.util.List;

public class LinguisticUsage {


    List<LinguisticEnum> linguisticUsages = new ArrayList<>();



    public LinguisticUsage() { }


    public void addOnelinguisticUsage(LinguisticEnum e){
        if(!linguisticUsages.contains(e)){
            linguisticUsages.add(e);
        }
    }

    public void removelinguisticUsages(LinguisticEnum e){
        linguisticUsages.remove(e);

    }


    public void addMultipleLinguisticUsages(List<LinguisticEnum> list){
        for(int i = 0; i < list.size(); i++){
            if(!linguisticUsages.contains(list.get(i))){
                linguisticUsages.add(list.get(i));
            }
        }
    }

    public List<LinguisticEnum> getLinguisticUsages(){
        return linguisticUsages;
    }

    public void setLinguisticUsages(List<LinguisticEnum> linguisticUsages){
        this.linguisticUsages = linguisticUsages;
    }

}
