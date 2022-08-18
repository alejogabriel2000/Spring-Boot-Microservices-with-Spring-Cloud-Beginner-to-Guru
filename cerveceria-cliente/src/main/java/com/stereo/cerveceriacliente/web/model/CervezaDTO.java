package com.stereo.cerveceriacliente.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CervezaDTO {

    private UUID id;
    private String nombreCerveza;
    private String estiloCerveza;
    private Long upc;
}
