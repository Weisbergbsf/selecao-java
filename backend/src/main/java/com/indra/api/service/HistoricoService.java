package com.indra.api.service;

import com.indra.api.model.dto.BandeiraDto;
import com.indra.api.model.dto.HistoricoCsvDto;
import com.indra.api.model.dto.MunicipioDto;
import com.indra.api.util.PageResultCriteria;

public interface HistoricoService {

	public MunicipioDto mediaPrecoDoMunicipio(String municipio);

	public PageResultCriteria<HistoricoCsvDto> obterTodosPorSiglaDaRegiao(String regiao, int currentPage, int pageSize);

	public PageResultCriteria<BandeiraDto> mediaPrecoCompraVendaPorBandeira(int currentPage, int pageSize);

	public PageResultCriteria<MunicipioDto> mediaPrecoCompraVendaPorMunicipio(int currentPage, int pageSize);
	
	public PageResultCriteria<HistoricoCsvDto> obterDadosPorRevendedora(int currentPage, int pageSize);

	public PageResultCriteria<HistoricoCsvDto> obterDadosPorDataColeta(int currentPage,int pageSize);
}
