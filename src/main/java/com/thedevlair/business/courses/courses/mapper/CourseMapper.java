package com.thedevlair.business.courses.courses.mapper;

import com.thedevlair.business.courses.courses.model.business.Course;
import com.thedevlair.business.courses.courses.model.thirdparty.CourseDOC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course courseDOCTOCourse(CourseDOC courseDOC);

    CourseDOC courseTOCourseDOC(Course course);

    // Method for update course existing with values of Course
    @Mapping(target = "id", ignore = true)// Ignore id
    void updateCourseFromDTO(@MappingTarget CourseDOC existingCourse, Course course);

}
