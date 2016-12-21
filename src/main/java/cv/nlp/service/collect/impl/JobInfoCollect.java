package cv.nlp.service.collect.impl;

import cv.domain.neo4j.JobInfo;
import cv.nlp.service.collect.FindSimilarSequence;
import cv.nlp.service.collect.PeriodePrepare;
import cv.nlp.service.collect.StreamWrapper;
import cv.nlp.service.collect.Worker;
import cv.support.Util;
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

        StreamWrapper streamData = new StreamWrapper(data);
        FindSimilarSequence sequence = new FindSimilarSequence(streamData.collect(Section.WORK_EXPERIENCE)).find();

        List<JobInfo> result = new ArrayList<>();
        sequence.getResult().forEach(seq -> {
            StreamWrapper seqWrapper = new StreamWrapper(seq);
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
