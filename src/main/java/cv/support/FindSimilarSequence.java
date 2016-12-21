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

}
