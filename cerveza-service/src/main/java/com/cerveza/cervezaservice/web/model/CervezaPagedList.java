package com.cerveza.cervezaservice.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CervezaPagedList extends PageImpl<CervezaDTO> {

   @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
   public CervezaPagedList(@JsonProperty("content") List<CervezaDTO> content,
                           @JsonProperty("number") int number,
                           @JsonProperty("size") int size,
                           @JsonProperty("totalElements") Long totalElements,
                           @JsonProperty("pageable")JsonNode pageable,
                           @JsonProperty("last") boolean last,
                           @JsonProperty("totalPages") int totalPages,
                           @JsonProperty("sort") JsonNode sort,
                           @JsonProperty("first") boolean first,
                           @JsonProperty("numberOfElements") int numberOfelements) {
      super(content, PageRequest.of(number, size), totalElements);
   }

   public CervezaPagedList(List<CervezaDTO> content, Pageable pageable, long total) {
      super(content, pageable, total);
   }

   public CervezaPagedList(List<CervezaDTO> content) {
      super(content);
   }
}