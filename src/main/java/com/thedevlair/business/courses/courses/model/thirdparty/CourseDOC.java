package com.thedevlair.business.courses.courses.model.thirdparty;

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

    private String name;

    private String state;

}
