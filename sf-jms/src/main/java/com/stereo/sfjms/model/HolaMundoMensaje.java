package com.stereo.sfjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HolaMundoMensaje implements Serializable {

   private static final long serialVersionUID = -2769927884602189842L;

   private UUID id;

   private String mensaje;
}
