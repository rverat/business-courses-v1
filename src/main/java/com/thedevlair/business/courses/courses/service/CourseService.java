package com.thedevlair.business.courses.courses.service;

import com.thedevlair.business.courses.courses.model.business.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {

    Flux<Course> findAll();

    Mono<Course> findById(String id);

    Mono<String> createCourse(Course course);

    Mono<String> updateCourse(Course product);

    Mono<Void> deleteCourse(String id);

}
