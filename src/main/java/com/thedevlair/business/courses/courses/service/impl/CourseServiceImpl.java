package com.thedevlair.business.courses.courses.service.impl;

import com.thedevlair.business.courses.courses.dao.CourseRepository;
import com.thedevlair.business.courses.courses.exception.types.NotFoundException;
import com.thedevlair.business.courses.courses.mapper.CourseMapper;
import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.service.CourseService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String COURSE_NOT_FOUND_MESSAGE = "Course with ID %s does not exist";

    final CourseRepository courseRepository;

    final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Flux<Course> findAll() {
        return courseRepository.findAll()
                .map(courseMapper::courseDOCTOCurse);
    }

    @Override
    public Mono<Course> findById(String id) {
        return courseRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(COURSE_NOT_FOUND_MESSAGE, id)))
                .map(courseMapper::courseDOCTOCurse);

    }

    @Override
    public Mono<String> createCourse(Course course) {
        return courseRepository.save(courseMapper.courseTOCurseDOC(course))
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
