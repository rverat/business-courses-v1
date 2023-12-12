package com.thedevlair.business.courses.courses.model.business;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String state;

}
