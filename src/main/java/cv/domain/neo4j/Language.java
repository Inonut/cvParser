package cv.domain.neo4j;

/**
 * Created by Dragos on 12/15/2016.
 */

public class Language extends DomainObject{

    private String name;
    private String level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
