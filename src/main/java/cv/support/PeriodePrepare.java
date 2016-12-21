package cv.support;

import cv.support.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dragos on 12/12/2016.
 */
public class PeriodePrepare {

    private List<String> dates;

    public PeriodePrepare() {
    }

    public void split(String periode) {
        if(periode == null){
            dates = new ArrayList<>();
        } else {
            this.dates = Arrays.stream(periode.split(Arrays.stream(Util.separators).collect(Collectors.joining("|")))).collect(Collectors.toList());
        }
    }

    public String next(){
        String date = null;
        if(hasNext()){
            date = dates.get(0);
            dates.remove(0);
        }
        return date;
    }

    public boolean hasNext(){
        return !dates.isEmpty();
    }
}
