package com.thedevlair.business.courses.courses.model.thirdparty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "courses")
public class CourseDOC {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String state;

}
