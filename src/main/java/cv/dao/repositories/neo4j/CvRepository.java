package cv.dao.repositories.neo4j;

import cv.domain.neo4j.Cv;
import cv.domain.neo4j.JobInfo;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
@Repository
public interface CvRepository extends GraphRepository<Object> {

    /*@Query("with {text} as text \n" +
            "with reduce(t=text, delim in [',','.','!','?','\"',':',';',\"'\",'-'] | replace(t,delim,' '+delim)) as normalized \n" +
            "with split(normalized,' ') as words \n" +
            "UNWIND range(0,size(words)-2) as idx \n" +
            "merge (w1:Word {name:words[idx], index: idx}) \n" +
            "merge (w2:Word {name:words[idx+1], index: idx+1}) \n" +
            "merge (w1)-[:Next]->(w2) \n" +
            "return w1, w2;")
    Object splitText(@Param("text") String text);

    @Query("MATCH (n) RETURN count(*);")
    Object countNodes();

    @Query("create (n: Test{name: {0}}) RETURN n as node;")
    Object createNode(Object label);

    @Query("with 'Ceva sa mearga.' as text \n" +
            "with reduce(t=text, delim in [',','.','!','?','\"',':',';',\"'\",'-'] | replace(t,delim,' '+delim)) as normalized \n" +
            "return normalized; ")
    Object normalized();

    @Query("with 'Ceva sa mearga.' as text \n" +
            "with reduce(t=text, delim in [',','.','!','?','\"',':',';',\"'\",'-'] | replace(t,delim,' '+delim)) as normalized \n" +
            "with split(normalized,' ') as words \n" +
            "return words;")
    Object words();

    @Query("with 'Ceva sa mearga.' as text \n" +
            "with reduce(t=text, delim in [',','.','!','?','\"',':',';',\"'\",'-'] | replace(t,delim,' '+delim)) as normalized \n" +
            "with split(normalized,' ') as words \n" +
            "UNWIND range(0,size(words)-2) as idx \n" +
            "merge (w1:Word {name:words[idx], index: idx})\n" +
            "return w1; ")
    Object merge1();

    @Query("with 'Ceva sa mearga.' as text \n" +
            "with reduce(t=text, delim in [',','.','!','?','\"',':',';',\"'\",'-'] | replace(t,delim,' '+delim)) as normalized \n" +
            "with split(normalized,' ') as words \n" +
            "UNWIND range(0,size(words)-2) as idx \n" +
            "merge (w1:Word {name:words[idx], index: idx}) \n" +
            "merge (w2:Word {name:words[idx+1], index: idx+1})")
    Object merge2();

    @Query("MATCH (n)\n" +
            "OPTIONAL MATCH (n)-[r]-()\n" +
            "DELETE n,r")
    Object clear();

    @Query("merge (w1:Word {name:'a', index: 1}) \n" +
            "merge (w2:Word {name:'b', index: 2}) \n" +
            "merge (w1)-[:Next]->(w2) \n")
    Object createRel();*/

   /* @Query("merge (w: {0}) return w;")
    Object test(String label);*/

    /*@Autowired
    @Qualifier("neo4jTemplate")
    private Neo4jOperations operations;

    public Object test(String label) {
        return operations.query("merge (w: " + label + ") return w;", null);
    }*/

    /*@QueryResult
    public class BookResult {
        public Long id;
        public Map<String,Object> node;
        public List<String> labels;
    }*/
/*
    @Query("MATCH (n:Test) where id(n)={0} return labels(n) as labels, ID(n) as id, {properties: n} as node")
    Book getBookLabels(Long bookId);*/

    //@Query("match (n:Cv)-[]->() return n;")
    @Query("start n=node(*) match path=(n)-[]->() return path;")
    List<Cv> loadCvs();


    @Query("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r;")
    void deleteAll();
}
