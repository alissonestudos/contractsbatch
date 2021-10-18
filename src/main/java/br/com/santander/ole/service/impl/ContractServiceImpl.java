package br.com.santander.ole.service.impl;


import br.com.santander.ole.service.ContractService;
import br.com.santander.ole.mapper.ContractRowMapper;
import br.com.santander.ole.dto.LinesDTO;
import br.com.santander.ole.dto.LinePositionsDTO;
import br.com.santander.ole.dao.ContractDao;
import br.com.santander.ole.entity.Contract;

import com.altec.bsbr.fw.batch.BatchErrorException;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ContractServiceImpl implements ContractService {
	
	private static final int ERROR_CODE = -1;
	private static final int SIZE_FIRST_EXECUTION = 8;
	private static final String FIRST_EXECUTION_POSITION = "{\"codCovenantPosition\":0,\"contractNumberPosition\":2,\"entryNumberPosition\":3,\"statusPosition\":4,\"cnpjCpfPosition\":5,\"trancheValuePosition\":6,\"registrationPosition\":7}";
	private static final String INCREMENTAL_EXECUTION_POSITION = "{\"codCovenantPosition\":0,\"contractNumberPosition\":1,\"entryNumberPosition\":3,\"statusPosition\":4,\"cnpjCpfPosition\":5,\"trancheValuePosition\":6,\"registrationPosition\":2}";
	private static final String DELIMITER = ";";
	
	@Autowired
	private ContractDao dao;
	
	private Gson gson;
	
	private ContractRowMapper mapper;

	@Autowired
	public ContractServiceImpl() {
		super();
	}
	/**
	 * Metodo que executar apos o construtor.
	 */
	@PostConstruct
	public void init() {
		this.gson = new Gson();
		this.mapper = new ContractRowMapper();
	}
	
	/**
	 * Processa o arquivo de contratos da carteira.
	 */
	public void process(LinesDTO linesContracts) {
		try {
			List<Contract> contracts = new ArrayList<>(linesContracts.getLines().size());
			LinePositionsDTO linePositions = null;
			Contract entity = null;
			boolean firstExecution = false;
			String[] lineArray = null;
			for (String line : linesContracts.getLines()) {
				lineArray = line.split(DELIMITER);
				if(lineArray.length < SIZE_FIRST_EXECUTION) {
					linePositions = this.gson.fromJson(INCREMENTAL_EXECUTION_POSITION, LinePositionsDTO.class);
					firstExecution = false;
				} else {
					linePositions = this.gson.fromJson(FIRST_EXECUTION_POSITION, LinePositionsDTO.class);
					firstExecution = true;
				}
				entity  = this.mapper.mapRow(lineArray, linePositions);
				entity.setFirstExecution(firstExecution);
				entity.setWallet(linesContracts.getWallet());
				contracts.add(entity);
			}
			if(firstExecution) {
				dao.saveContracts(contracts);
			}else {
				saveOrUpdate(contracts);
			}
		}catch (SQLException e) {
			String msg = "ERRO EM BANCO DE DADOS {}.";
			throw new BatchErrorException(ERROR_CODE, msg, e);
		}
		
	}
	
	private void saveOrUpdate(List<Contract> contracts)  throws SQLException {
		
		List<Contract> contractsInserts = new ArrayList<>();
		List<Contract> contractsUpdates = new ArrayList<>();
		
		Iterator<Contract> it = contracts.iterator();
		while (it.hasNext()) {
			Contract contract = it.next();
			Contract data = dao.findByContraint(contract);
			if (Objects.nonNull(data)) {
				if (!data.getStatus().equals(contract.getStatus()) || !data.getEntryNumber().equals(contract.getEntryNumber())) {
					contractsUpdates.add(data);
				}
			} else {
				contractsInserts.add(contract);
			}
		}
		dao.saveContracts(contractsInserts);
		dao.updateContracts(contractsUpdates);
	}
	
}
