package cv.nlp.service.collect.impl;

import cv.domain.neo4j.JobInfo;
import cv.support.FindSimilarSequence;
import cv.support.PeriodePrepare;
import cv.support.DataWrapper;
import cv.nlp.service.collect.Worker;
import cv.support.section.Section;
import cv.support.section.SectionContent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12/12/2016.
 */
public class JobInfoCollect implements Worker<List<JobInfo>> {
    @Override
    public List<JobInfo> work(List<Pair<Section, SectionContent>> data) {

        DataWrapper streamData = new DataWrapper(data);
        FindSimilarSequence sequence = new FindSimilarSequence(streamData.collect(Section.WORK_EXPERIENCE)).find();

        List<JobInfo> result = new ArrayList<>();
        sequence.getResult().forEach(seq -> {
            DataWrapper seqWrapper = new DataWrapper(seq);
            PeriodePrepare periodePrepare = new PeriodePrepare();
            periodePrepare.split(seqWrapper.collect(Section.PERIODE));

            JobInfo jobInfo = new JobInfo();
            jobInfo.setDateStart(periodePrepare.next());
            jobInfo.setDateEnd(periodePrepare.next());
            jobInfo.setAdress(seqWrapper.collect(Section.ADRESS));
            jobInfo.setPeriode(seqWrapper.collect(Section.PERIODE));
            jobInfo.setDescription(seqWrapper.collect(Section.DESCRIPTION));
            jobInfo.setEmployer(seqWrapper.collect(Section.ANGAJATOR));
            jobInfo.setJobRole(seqWrapper.collect(Section.JOB_ROLE));

            result.add(jobInfo);

        });

        return result;
    }
}
