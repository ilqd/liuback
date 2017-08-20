package ru.splashcourse.liubachka.logics.pages.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.splashcourse.liubachka.ObjectWithIdImpl;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PageToUrlMapping extends ObjectWithIdImpl {

    @NotBlank
    @Column(unique = true)
    private String url;

    @ManyToOne
    private Page page;

}
