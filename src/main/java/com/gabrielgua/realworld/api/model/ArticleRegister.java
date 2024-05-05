package com.gabrielgua.realworld.api.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonTypeName("article")
public class ArticleRegister extends BaseResponse {

    private String title;
    private String description;
    private String body;
    private Set<String> tagList;

}
