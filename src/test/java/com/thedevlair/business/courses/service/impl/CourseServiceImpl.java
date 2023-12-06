package com.thedevlair.business.courses.service.impl;

import com.thedevlair.business.courses.courses.dao.CourseRepository;
import com.thedevlair.business.courses.courses.exception.types.NotFoundException;
import com.thedevlair.business.courses.courses.mapper.CourseMapper;
import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.model.thirdparty.CourseDOC;
import com.thedevlair.business.courses.courses.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;


    @Test
    void findAll_shouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(Flux.just(new CourseDOC("656f4f6e3998b15e4ff5c9cb", "Linux", "enable")));
        when(courseMapper.courseDOCTOCurse(any())).thenReturn(new Course("656f4f6e3998b15e4ff5c9cb", "Linux", "enable"));

        Flux<Course> result = courseService.findAll();

        StepVerifier.create(result)
                .expectNextCount(1) // Adjust based on the number of CourseDOC instances you're mocking
                .verifyComplete();

        verify(courseRepository).findAll();
        verify(courseMapper, times(1)).courseDOCTOCurse(any());
    }

    @Test
    void findById_existingId_shouldReturnCourse() {
        String courseId = "656f4f6e3998b15e4ff5c9cb";
        when(courseRepository.findById(courseId)).thenReturn(Mono.just(new CourseDOC("656f4f6e3998b15e4ff5c9cb", "Linux", "enable")));
        when(courseMapper.courseDOCTOCurse(any())).thenReturn(new Course("656f4f6e3998b15e4ff5c9cb", "Linux", "enable"));

        Mono<Course> result = courseService.findById(courseId);

        StepVerifier.create(result)
                .expectNextMatches(course -> course.getId().equals("656f4f6e3998b15e4ff5c9cb"))
                .verifyComplete();

        verify(courseRepository).findById(courseId);
        verify(courseMapper, times(1)).courseDOCTOCurse(any());
    }

    @Test
    void findById_nonExistingId_shouldThrowNotFoundException() {
        String nonExistingId = "656f4f6e3998b15e4ff5c9ca";
        when(courseRepository.findById(nonExistingId)).thenReturn(Mono.empty());

        Mono<Course> result = courseService.findById(nonExistingId);

        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        verify(courseRepository).findById(nonExistingId);
        verify(courseMapper, never()).courseDOCTOCurse(any());
    }

    @Test
    void createCourse_validCourse_shouldReturnSuccessMessage() {
        Course courseToCreate = new Course("656f4f6e3998b15e4ff5c9ca", "linux", "enable");
        CourseDOC savedCourseDOC = new CourseDOC("656f4f6e3998b15e4ff5c9ca", "linux", "enable");

        when(courseMapper.courseTOCurseDOC(courseToCreate)).thenReturn(savedCourseDOC);
        when(courseRepository.save(savedCourseDOC)).thenReturn(Mono.just(savedCourseDOC));

        Mono<String> result = courseService.createCourse(courseToCreate);

        StepVerifier.create(result)
                .expectNext("Course created successfully. Access it at: 656f4f6e3998b15e4ff5c9ca")
                .verifyComplete();

        verify(courseRepository).save(savedCourseDOC);
        verify(courseMapper, times(1)).courseTOCurseDOC(courseToCreate);
        //verify(courseMapper, times(1)).courseDOCTOCurse(savedCourseDOC);
    }

    @Test
    void updateCourse_existingCourse_shouldReturnSuccessMessage() {
        String existingCourseId = "existingId";
        CourseDOC existingCourse = new CourseDOC(existingCourseId, "Existing Course", "enable");
        Course updatedCourse = new Course(existingCourseId, "Updated Course", "disable");

        when(courseRepository.findById(existingCourseId)).thenReturn(Mono.just(existingCourse));

        doAnswer(invocation -> {
            CourseDOC existing = invocation.getArgument(0);
            Course update = invocation.getArgument(1);
            existing.setName(update.getName());
            existing.setState(update.getState());
            return null; // The method is void, so we return null
        }).when(courseMapper).updateCourseFromDTO(any(), any());

        when(courseRepository.save(existingCourse)).thenReturn(Mono.just(existingCourse));

        Mono<String> result = courseService.updateCourse(updatedCourse);

        StepVerifier.create(result)
                .expectNext("Course updated")
                .verifyComplete();

        verify(courseRepository).findById(existingCourseId);
        verify(courseRepository).save(existingCourse);
        verify(courseMapper).updateCourseFromDTO(existingCourse, updatedCourse);
    }

    @Test
    void updateCourse_nonExistingCourse_shouldReturnFailureMessage() {
        // Assuming a non-existing course ID for the test
        String nonExistingCourseId = "nonExistingId";
        Course updatedCourse = new Course(nonExistingCourseId, "Updated Course", "disable");

        // Mock the behavior of findById to return an empty Mono, indicating non-existence
        when(courseRepository.findById(nonExistingCourseId)).thenReturn(Mono.empty());

        Mono<String> result = courseService.updateCourse(updatedCourse);

        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        // Verify that the repository's findById method was called
        verify(courseRepository).findById(nonExistingCourseId);
        // Ensure that the save method is not called for a non-existing course
        verify(courseRepository, never()).save(any());
        // Ensure that the updateCourseFromDTO method is not called for a non-existing course
        verify(courseMapper, never()).updateCourseFromDTO(any(), any());
    }


    @Test
    void deleteCourse_existingCourseId_shouldDeleteCourse() {
        String existingCourseId = "existingId";

        when(courseRepository.existsById(existingCourseId)).thenReturn(Mono.just(true));
        when(courseRepository.deleteById(existingCourseId)).thenReturn(Mono.empty());

        Mono<Void> result = courseService.deleteCourse(existingCourseId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(courseRepository).existsById(existingCourseId);
        verify(courseRepository).deleteById(existingCourseId);
    }

    @Test
    void deleteCourse_nonExistingCourseId_shouldThrowNotFoundException() {
        String nonExistingCourseId = "nonExistingId";

        when(courseRepository.existsById(nonExistingCourseId)).thenReturn(Mono.just(false));

        Mono<Void> result = courseService.deleteCourse(nonExistingCourseId);

        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        verify(courseRepository).existsById(nonExistingCourseId);
        verify(courseRepository, never()).deleteById(anyString());
    }
}
