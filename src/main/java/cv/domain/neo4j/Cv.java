package cv.domain.neo4j;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Dragos on 12/16/2016.
 */
public class Cv extends DomainObject{

    //TODO: de schimbat in int[]
    @Property
    private byte[] inputData;

    @Relationship(type="HAS", direction=Relationship.OUTGOING)
    private PersonalInfo personalInfo;

    @Relationship(type="HAS", direction=Relationship.OUTGOING)
    private List<Education> educations;

    @Relationship(type="HAS", direction=Relationship.OUTGOING)
    private List<JobInfo> jobInfos;

    @Relationship(type="HAS", direction=Relationship.OUTGOING)
    private List<Language> languages;

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<JobInfo> getJobInfos() {
        return jobInfos;
    }

    public void setJobInfos(List<JobInfo> jobInfos) {
        this.jobInfos = jobInfos;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public String getInputData() {
        try {
            return new String(inputData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(inputData);
    }

    public void setInputData(String inputData) {
        try {
            this.inputData = inputData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
