package ru.volsu.coursefilestorage.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.coursefilestorage.model.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, Integer> {
    Optional<File> findByUuid(String uuid);
    List<File> findAllByUuidIn(List<String> uuidList);
}
