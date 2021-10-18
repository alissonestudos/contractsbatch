package br.com.santander.ole.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.altec.bsbr.fw.batch.Parameter;
import com.altec.bsbr.fw.batch.RecordConsumer;

import br.com.santander.ole.dto.LinesDTO;
import br.com.santander.ole.service.ContractService;

public class WalletConsumer implements RecordConsumer<LinesDTO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WalletConsumer.class);
	
	@Autowired(required = true)
	private ContractService service;

	public WalletConsumer() {
		super();
	}

	/**
	 * Método de carregamento para a base de contratos unificada.
	 */
	@Override
	public void consume(LinesDTO lines) {
		LOGGER.info("Processando lote de registros .." );
		this.service.process(lines);
	}

	/**
	 * Método de carregamento para a base de contratos unificada.
	 */
	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Método que seta paramentros da aplicação.
	 */
	@Override
	public void setParameters(List<Parameter> parameters) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Método para recuperar parametros da aplicação.
	 */
	@Override
	public List<Parameter> getParameters() {
		throw new UnsupportedOperationException();
	}
}
