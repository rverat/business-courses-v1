package com.thedevlair.business.courses.web;

import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/courses")
public class CourseController {

    final CourseService courseService;


    /**
     * Constructs a new CourseController with the given services.
     *
     * @param courseService The service for managing courses.
     */
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Retrieves all courses.
     *
     * @return A Flux of Course objects representing all courses.
     */
    @GetMapping
    @Operation(summary = "Get all courses")
    public Flux<Course> getCourses() {
        return courseService.findAll();
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param id The ID of the course to retrieve.
     * @return A Mono of Course representing the course with the given ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a course by ID")
    public Mono<Course> getCourseById(@PathVariable String id) {
        return courseService.findById(id);
    }

    /**
     * Creates a new course.
     *
     * @param course The Course object representing the new course to create.
     * @return A Mono of String representing a success message.
     */
    @PostMapping
    @Operation(summary = "Create a new course")
    public Mono<String> createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    /**
     * Updates an existing course.
     *
     * @param course The Course object representing the updated course.
     * @return A Mono of String representing a success message.
     */
    @PatchMapping
    @Operation(summary = "Update an existing course")
    public Mono<String> updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    /**
     * Deletes a course by its ID.
     *
     * @param id The ID of the course to delete.
     * @return A Mono of Void representing a successful deletion.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course by ID")
    public Mono<Void> deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id);
    }

}
