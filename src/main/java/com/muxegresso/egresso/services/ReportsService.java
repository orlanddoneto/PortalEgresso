package com.muxegresso.egresso.services;

import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCargoDTO;
import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCursoDTO;
import com.muxegresso.egresso.domain.dtos.reports.EgressosPorMesDTO;

public interface ReportsService{

    DadosGraficoCargoDTO gerarTopCargosPorSalario(Integer quantity);

    DadosGraficoCursoDTO gerarGraficoEgressosPorCurso();

    EgressosPorMesDTO contarEgressosPorMes();
}
