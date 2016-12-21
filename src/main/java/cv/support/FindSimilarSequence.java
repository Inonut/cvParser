package cv.support;

import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/13/2016.
 */
public class FindSimilarSequence {

    private List<Pair<Section, SectionContent>> data;
    private List<List<Pair<Section, SectionContent>>> result;

    public FindSimilarSequence(List<Pair<Section, SectionContent>> data){
        this.data = data;
        this.result = new ArrayList<>();
    }

    public FindSimilarSequence find(){

        if(this.data == null){
            return this;
        }

        List<Pair<Section, SectionContent>> seq = new ArrayList<>();

        for (Pair<Section, SectionContent> pair: this.data){
            if(seq.stream().anyMatch(p -> p.getKey().equals(pair.getKey()))){
                this.result.add(seq);
                seq = new ArrayList<>();
            }
            seq.add(pair);
        }
        result.add(seq);

        return this;
    }

    public List<List<Pair<Section, SectionContent>>> getResult(){
        return this.result;
    }

    /*//O(n^2)... :(
    public FindSimilarSequence find(){

        if(this.data == null){
            return this;
        }

        List<Pair<String, String>> seq = new ArrayList<>();

        for (int i = 0;i<this.data.size();i++){
            seq.add(this.data.get(i));
            if(!existSequence(data, seq)){
                if(seq.size() > 1){
                    seq.remove(this.data.get(i));
                    i--;
                }
                result.add(seq);
                seq = new ArrayList<>();
            }
        }
        result.add(seq);

        return this;
    }

    //O(n)
    private boolean existSequence(List<Pair<String, String>> data, List<Pair<String, String>> seq) {
        for(int i=0; i<data.size(); i++){
            int j;
            for(j=0; j<seq.size() && i<data.size(); j++, i++){
                if(!(data.get(i) != seq.get(j) && data.get(i).getKey().equals(seq.get(j).getKey()))){
                    break;
                }
            }
            if(j==seq.size()){
                return true;
            } *//*else {
                i--;
            }*//*
        }

        return false;
    }*/
}
