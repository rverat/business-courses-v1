package com.thedevlair.business.courses.courses.dao;

import com.thedevlair.business.courses.courses.model.thirdparty.CourseDOC;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ReactiveMongoRepository<CourseDOC, String> {

}
