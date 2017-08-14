package ru.splashcourse.liubachka.logics.skilltest.results.model;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.splashcourse.liubachka.ObjectWithIdImpl;

@Entity
@ToString()
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public class TestResult extends ObjectWithIdImpl {

    private String personName;

    private String phone;

    private String email;

    private Integer age;

    private boolean isInterested = false;

    private boolean hasBeenCalled = false;

    private boolean willCome = false;

    @Type(type = "text")
    private String testSnapShotDto;

    @Type(type = "text")
    private String resultsSnapShotDto;
}