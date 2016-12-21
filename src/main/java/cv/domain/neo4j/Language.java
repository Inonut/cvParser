package cv.domain.neo4j;

import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.springframework.data.neo4j.annotation.QueryResult;

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
