package com.cerveza.cervezaservice.web.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CervezaPagedList extends PageImpl<CervezaDTO> {

   public CervezaPagedList(List<CervezaDTO> content, Pageable pageable, long total) {
      super(content, pageable, total);
   }

   public CervezaPagedList(List<CervezaDTO> content) {
      super(content);
   }
}
