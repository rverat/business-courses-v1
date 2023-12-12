package com.thedevlair.business.courses.courses.service.impl;

import com.thedevlair.business.courses.courses.dao.CourseRepository;
import com.thedevlair.business.courses.courses.exception.types.NotFoundException;
import com.thedevlair.business.courses.courses.mapper.CourseMapper;
import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);


    private static final String COURSE_NOT_FOUND_MESSAGE = "Course with ID %s does not exist";

    final CourseRepository courseRepository;

    final CourseMapper courseMapper;

    final RedisServiceImpl redisTemplate;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper, RedisServiceImpl redisTemplate) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Flux<Course> findAll() {
        return courseRepository.findAll()
                .map(courseMapper::courseDOCTOCourse);
    }

    @Override
    public Mono<Course> findById(String id) {

        return redisTemplate.get(id)
                .flatMap(course -> {
                    logger.info("Get data from cache");
                    return Mono.just(course);
                })
                .switchIfEmpty(courseRepository.findById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException(COURSE_NOT_FOUND_MESSAGE, id)))
                        .flatMap(courseDOC -> {

                            Course course = courseMapper.courseDOCTOCourse(courseDOC);
                            // Save course in redis
                            return redisTemplate.put(course.getId(), course)
                                    .then(Mono.just(course));
                        })
                        .doOnNext(course -> logger.info("Get data from mongodb"))
                );
    }


    @Override
    public Mono<String> createCourse(Course course) {
        return courseRepository.save(courseMapper.courseTOCourseDOC(course))
                .map(savedCourse -> "Course created successfully. Access it at: " + savedCourse.getId())
                .onErrorResume(throwable -> Mono.just("Failed to create course: " + throwable.getMessage()));
    }

    @Override
    public Mono<String> updateCourse(Course course) {
        return courseRepository.findById(course.getId())
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(COURSE_NOT_FOUND_MESSAGE, course.getId()))
                )).flatMap(existingCourse -> {

                    //update existingCourse with news values
                    courseMapper.updateCourseFromDTO(existingCourse, course);

                    //execute update
                    return courseRepository.save(existingCourse)
                            .map(savedCourse -> "Course updated")
                            .onErrorResume(throwable -> Mono.just("Failed to update course: " + throwable.getMessage()));
                });

    }

    @Override
    public Mono<Void> deleteCourse(String id) {
        return courseRepository.existsById(id)
                .flatMap(exists -> {
                    boolean doesExist = exists;
                    return doesExist ?
                            courseRepository.deleteById(id).then(Mono.empty()) :
                            Mono.error(new NotFoundException(COURSE_NOT_FOUND_MESSAGE, id));
                });
    }
}
