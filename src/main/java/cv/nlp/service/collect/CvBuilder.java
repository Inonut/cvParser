package cv.nlp.service.collect;

import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Dragos on 12/12/2016.
 */
public class CvBuilder {

    private List<Pair<Section, SectionContent>> data;
    private List<Worker> workers;

    public CvBuilder(List<Pair<Section, SectionContent>> data){
        this.data = data;
        this.workers = new ArrayList<>();
    }

    public static CvBuilder groupData(List<Pair<Section, SectionContent>> data, Section... forSections){

        List<Section> sections = new ArrayList<>();

        if(forSections != null){
            sections.addAll(Arrays.asList(forSections));
        }

        List newData = new ArrayList();
        for (int i=0; i<data.size(); i++){
            Pair<Section, SectionContent> pair = data.get(i);
            if(sections.isEmpty() || sections.contains(pair.getKey())){
                int j;
                for(j=i+1; j<data.size() && pair.getKey().equals(data.get(j).getKey()); j++){
                    pair = new Pair<>(pair.getKey(), pair.getValue().concat(data.get(j).getValue()));
                }
                i = j - 1;
            }
            newData.add(pair);
        }

        return new CvBuilder(newData);
    }

    public List<Pair<Section, SectionContent>> getData(){
        return this.data;
    }

    public <T> CvBuilder withWorker(Worker<T> worker) {

        this.workers.clear();
        this.workers.add(worker);

        return this;
    }

    public <T> CvBuilder addWorker(Worker<T> worker) {

        this.workers.add(worker);

        return this;
    }

    public <T> CvBuilder execute(Consumer<T> fct) {
        this.workers.forEach(w -> fct.accept((T) w.work(this.data)));
        return this;
    }
}
