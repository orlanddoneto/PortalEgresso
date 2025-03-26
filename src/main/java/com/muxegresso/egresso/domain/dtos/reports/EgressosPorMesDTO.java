package com.muxegresso.egresso.domain.dtos.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EgressosPorMesDTO {
    private List<Integer> series;

    public EgressosPorMesDTO(List<Integer> series) {
        this.series = series;
    }
}
