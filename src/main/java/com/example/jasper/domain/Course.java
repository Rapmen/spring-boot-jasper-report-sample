package com.example.jasper.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "course_generator", sequenceName = "course_sequence", initialValue = 100)
    @GeneratedValue(generator = "course_generator")
    private Long id;

    private String name;
    private String details;
    private Date startDate;
}
