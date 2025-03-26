package com.muxegresso.egresso.domain.dtos.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DadosGraficoCargoDTO implements Serializable {
    private List<Double> series;
    private List<String> categories;

    public DadosGraficoCargoDTO(List<Double> series, List<String> categories) {
        this.series = series;
        this.categories = categories;
    }
}
