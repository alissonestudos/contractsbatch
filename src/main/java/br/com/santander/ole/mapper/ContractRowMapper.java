package br.com.santander.ole.mapper;

import br.com.santander.ole.dto.LinePositionsDTO;
import br.com.santander.ole.entity.Contract;

public class ContractRowMapper {
	
	public ContractRowMapper() {
		super();
	}

	/**
	 * Metodo que mapea a linha do arquivo.
	 */
	public Contract mapRow(String[] row, LinePositionsDTO linePositions) {
		Contract entity = new Contract();
		entity.setCodCovenant(row[linePositions.getCodCovenantPosition()]);
		entity.setContractNumber(row[linePositions.getContractNumberPosition()]);
		entity.setEntryNumber(row[linePositions.getEntryNumberPosition()]);
		entity.setStatus(row[linePositions.getStatusPosition()]);
		entity.setCnpjCpf(row[linePositions.getCnpjCpfPosition()]);
		entity.setTrancheValue(row[linePositions.getTrancheValuePosition()]);
		entity.setRegistration(row[linePositions.getRegistrationPosition()]);
		validateFields(entity);
		return entity;
	}
	 
	private void validateFields(Contract entity) {
		if(entity.getCnpjCpf() != null) {
			entity.setCnpjCpf(entity.getCnpjCpf().replaceAll("[^0-9]", ""));
		}
		if(entity.getTrancheValue() != null) {
			entity.setTrancheValue(entity.getTrancheValue().replaceAll("^0*", ""));
		}
		if(entity.getRegistration() != null) {
			entity.setRegistration(entity.getRegistration().trim());
		}
		if(entity.getEntryNumber() != null) {
			entity.setEntryNumber(entity.getEntryNumber().trim());
		}
	}
}