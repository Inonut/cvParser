package cv.support.section;

/**
 * Created by Dragos on 12/15/2016.
 */
public interface SectionContent {

    <T> T concat(SectionContent other);

    <T> T getContent();
}
