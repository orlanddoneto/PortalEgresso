package com.muxegresso.egresso.domain.dtos.reports;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DadosGraficoCursoDTO {
    private List<Integer> series;
    private List<String> labels;

    public DadosGraficoCursoDTO(List<Integer> series, List<String> labels) {
        this.series = series;
        this.labels = labels;
    }

}
