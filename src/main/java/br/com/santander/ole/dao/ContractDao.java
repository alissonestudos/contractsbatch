package br.com.santander.ole.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.santander.ole.entity.Contract;


public interface ContractDao {
	
	void saveContracts(List<Contract> contracts) throws SQLException;
	void updateContracts(List<Contract> contracts) throws SQLException;
	Contract findByContraint(Contract contract);
	
	
}
