package ru.volsu.coursefilestorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.volsu.coursefilestorage.model.DatabaseSequence;

import java.util.Objects;

@Service
@EnableAutoConfiguration
public class SequenceGenerator {

    private MongoOperations mongoOperations;

    @Autowired
    public SequenceGenerator(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Integer generateSequence(String seqName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(seqName));

        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        DatabaseSequence counter = mongoOperations.findAndModify(query,
                new Update().inc("seq", 1),
                options,
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSequence() : 1;

    }
}
