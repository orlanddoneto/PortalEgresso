package com.muxegresso.egresso.services.impl;

import com.muxegresso.egresso.domain.Cargo;
import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCargoDTO;
import com.muxegresso.egresso.domain.dtos.reports.DadosGraficoCursoDTO;
import com.muxegresso.egresso.domain.dtos.reports.EgressosPorMesDTO;
import com.muxegresso.egresso.repositories.CargoRepository;
import com.muxegresso.egresso.repositories.CursoRepository;
import com.muxegresso.egresso.repositories.EgressoRepository;
import com.muxegresso.egresso.services.CargoService;
import com.muxegresso.egresso.services.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    public DadosGraficoCargoDTO gerarTopCargosPorSalario(Integer quantity) {
        Pageable top6 = PageRequest.of(0, quantity);
        List<Cargo> cargos = cargoRepository.findTopSalarios(top6);

        List<Double> series = cargos.stream()
                .map(Cargo::getSalario)
                .collect(Collectors.toList());

        List<String> categories = cargos.stream()
                .map(Cargo::getDescricao) // ou outro info tipo: getDescricao() + " (" + getLocal() + ")"
                .collect(Collectors.toList());

        return new DadosGraficoCargoDTO(series, categories);
    }

    public DadosGraficoCursoDTO gerarGraficoEgressosPorCurso() {
        List<Object[]> resultados = cursoRepository.contarEgressosPorCurso();

        List<String> labels = new ArrayList<>();
        List<Integer> series = new ArrayList<>();

        for (Object[] linha : resultados) {
            labels.add((String) linha[0]);
            series.add(((Long) linha[1]).intValue());
        }

        return new DadosGraficoCursoDTO(series, labels);
    }

    @Override
    public EgressosPorMesDTO contarEgressosPorMes() {
        int anoAtual = Year.now().getValue();
        List<Object[]> resultados = egressoRepository.contarEgressosPorMes(anoAtual);

        Map<Integer, Integer> mapa = new HashMap<>();
        for (Object[] linha : resultados) {
            Integer mes = ((Number) linha[0]).intValue();
            Integer total = ((Number) linha[1]).intValue();
            mapa.put(mes, total);
        }

        List<Integer> series = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            series.add(mapa.getOrDefault(i, 0));
        }

        return new EgressosPorMesDTO(series);
    }

}
