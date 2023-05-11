package ru.volsu.coursefilestorage.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.coursefilestorage.model.File;

@Repository
public interface FileRepository extends MongoRepository<File, Integer> {
}
