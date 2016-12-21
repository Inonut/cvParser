package cv.dao.repositories.neo4j;

import cv.domain.neo4j.Cv;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dragos on 12/6/2016.
 */
@Repository
public interface CvRepository extends GraphRepository<Object> {

    //@Query("match (n:Cv)-[]->() return n;")
    @Query("start n=node(*) match path=(n)-[]->() return path;")
    List<Cv> loadCvs();


    @Query("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r;")
    void deleteAll();
}
