package com.thedevlair.business.courses.web;

import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Test
    void getCourses_shouldReturnAllCourses() {
        // Mock the behavior of your service
        when(courseService.findAll()).thenReturn(Flux.just(new Course("656f4f6e3998b15e4ff5c9cb", "Linux", "enable"),
                new Course("656f5f00b9827f724fd051aa", "java", "enable")));

        // Use WebTestClient to perform HTTP requests and verify the response
        WebTestClient
                .bindToController(courseController)
                .build()
                .get()
                .uri("/v1/api/courses")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Course.class)
                .hasSize(2);

        // Verify that the service method was called
        verify(courseService).findAll();
    }

    @Test
    void getCourseById_shouldReturnCourse() {
        // Mock the behavior of your service
        when(courseService.findById("1")).thenReturn(Mono.just(new Course("656f4f6e3998b15e4ff5c9cb", "Linux", "enable")));

        // Use WebTestClient to perform HTTP requests and verify the response
        WebTestClient
                .bindToController(courseController)
                .build()
                .get()
                .uri("/v1/api/courses/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Course.class)
                .isEqualTo(new Course("656f4f6e3998b15e4ff5c9cb", "Linux", "enable"));

        // Verify that the service method was called
        verify(courseService).findById("1");
    }

    @Test
    void createCourse_shouldReturnSuccessMessage() {
        Course courseToCreate = new Course(/* Initialize with required data */);
        when(courseService.createCourse(any(Course.class))).thenReturn(Mono.just("Course created successfully"));

        WebTestClient
                .bindToController(courseController)
                .build()
                .post()
                .uri("/v1/api/courses")
                .bodyValue(courseToCreate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Course created successfully");

        verify(courseService).createCourse(any(Course.class));
    }

    @Test
    void updateCourse_shouldReturnSuccessMessage() {
        Course courseToUpdate = new Course(/* Initialize with required data */);
        when(courseService.updateCourse(any(Course.class))).thenReturn(Mono.just("Course updated"));

        WebTestClient
                .bindToController(courseController)
                .build()
                .patch()
                .uri("/v1/api/courses")
                .bodyValue(courseToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Course updated");

        verify(courseService).updateCourse(any(Course.class));
    }

    @Test
    void deleteCourse_shouldReturnSuccess() {
        String courseIdToDelete = "someId";
        when(courseService.deleteCourse(courseIdToDelete)).thenReturn(Mono.empty());

        WebTestClient
                .bindToController(courseController)
                .build()
                .delete()
                .uri("/v1/api/courses/{id}", courseIdToDelete)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        verify(courseService).deleteCourse(courseIdToDelete);
    }
}
