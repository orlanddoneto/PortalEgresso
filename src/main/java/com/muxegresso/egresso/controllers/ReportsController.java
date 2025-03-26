package com.muxegresso.egresso.controllers;

import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCargoDTO;
import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCursoDTO;
import com.muxegresso.egresso.domain.dtos.reports.EgressosPorMesDTO;
import com.muxegresso.egresso.services.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;


    @GetMapping("/grafico-top-salarios")
    public ResponseEntity<DadosGraficoCargoDTO> topSalarios() {
        DadosGraficoCargoDTO dados = reportsService.gerarTopCargosPorSalario(6);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/grafico-egressos-por-curso")
    public ResponseEntity<DadosGraficoCursoDTO> graficoEgressosCurso() {
        DadosGraficoCursoDTO dados = reportsService.gerarGraficoEgressosPorCurso();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/egressos-por-mes")
    public ResponseEntity<EgressosPorMesDTO> egressosPorMes() {
        return ResponseEntity.ok(reportsService.contarEgressosPorMes());
    }


}
