package ru.splashcourse.liubachka.logics.pages.model;

import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.splashcourse.liubachka.ObjectWithIdImpl;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PageElement extends ObjectWithIdImpl {

    public void setChildren(List<PageElement> children) {
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(child -> child.setParentElement(this));
        }
        this.children = children;
    }

    @ManyToOne
    private Page parentPage;

    @ManyToOne
    private PageElement parentElement;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private PageElementType type;

    private String name;

    private int elementOrder = 1;

    @ElementCollection
    private Map<String, String> params;

    private String style;

    @OneToMany
    private List<PageElement> children;

    private String content;
}
