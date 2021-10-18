package br.com.santander.ole.dao.impl;

import br.com.santander.ole.entity.Contract;
import br.com.santander.ole.dao.ContractDao;

import com.altec.bsbr.fw.dao.jpa.GenericJpaDao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import javax.persistence.Query;

import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

@Repository
public class ContractDaoImpl extends GenericJpaDao<Contract, Long> implements ContractDao {

	private static final int DATE_CHANGE_INSERT_POSITION = 10;
	private static final int DATE_UPDATE_POSITION = 3;
	private static final int STATUS_UPDATE_POSITION = 2;
	private static final int COVENANT_ENTRY_NUMBER_UPDATE_INSERT_POSITION = 1;
	private static final int WALLET_INSERT_POSITION = 2;
	private static final int CONTRACT_NUMBER_INSERT_POSITION = 3;
	private static final int ENTRY_NUMBER_INSERT_POSITION = 4;
	private static final int STATUS_INSERT_POSITION = 5;
	private static final int CNPJ_CPF_INSERT_POSITION = 6;
	private static final int TRANCHE_VALUE_INSERT_POSITION = 7;
	private static final int REGISTRATION_INSERT_POSITION = 8;
	private static final int DATE_INSERT_POSITION = 9;
	private static final String INSERT_CONTRACT = "INSERT INTO dbo.TB_CONTRACT (COD_COVENANT, WALLET, CONTRACT_NUMBER, ENTRY_NUMBER, STATUS, CNPJ_CPF, TRANCHE_VALUE, REGISTRATION, CREATED_DATE, CHANGED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_CONTRACT = "UPDATE dbo.TB_CONTRACT (ENTRY_NUMBER, STATUS, CHANGED_DATE) VALUES (?, ?, ?)";

	private static final Logger LOGGER = LoggerFactory.getLogger(ContractDaoImpl.class);

	@Autowired
	public ContractDaoImpl() {
		super();
	}

	
	/**
	 * Salva contratos em carga completa.
	 * @throws SQLException 
	 *
	 */
	@Transactional(rollbackFor = {SQLException.class})
	public void saveContracts(List<Contract> contracts) throws SQLException   {
		PreparedStatement prepStmt = null;
		try {
			Session session = (Session) getEntityManager().getDelegate();
	        MyWork myWork = new MyWork();
	        session.doWork(myWork);
			Connection connection = myWork.getConnection();
			connection.setAutoCommit(false);
			prepStmt = connection.prepareStatement(INSERT_CONTRACT);
			Iterator<Contract> it = contracts.iterator();
			java.sql.Date dateNow =  new java.sql.Date(new Date().getTime());
			while (it.hasNext()) {
				Contract contract = it.next();
				prepStmt.setString(COVENANT_ENTRY_NUMBER_UPDATE_INSERT_POSITION,contract.getCodCovenant());  
				prepStmt.setString(WALLET_INSERT_POSITION,contract.getWallet());
				prepStmt.setString(CONTRACT_NUMBER_INSERT_POSITION,contract.getContractNumber());
				prepStmt.setString(ENTRY_NUMBER_INSERT_POSITION,contract.getEntryNumber());
				prepStmt.setString(STATUS_INSERT_POSITION,contract.getStatus());
				prepStmt.setString(CNPJ_CPF_INSERT_POSITION,contract.getCnpjCpf());
				prepStmt.setString(TRANCHE_VALUE_INSERT_POSITION,contract.getTrancheValue());
				prepStmt.setString(REGISTRATION_INSERT_POSITION,contract.getRegistration());
				prepStmt.setDate(DATE_INSERT_POSITION, dateNow);
				prepStmt.setDate(DATE_CHANGE_INSERT_POSITION, null);
			    prepStmt.addBatch();
			}
			prepStmt.executeBatch();
			connection.commit();
			LOGGER.info("Lote de registros gravado com sucesso quantidade: {}",  contracts.size());
		} catch (SQLException e) {
			LOGGER.error("Erro em gravação de registro.", e);
		}finally {
			if(Objects.nonNull(prepStmt)) {
				prepStmt.close();
			}
		}
	}
	
	
	@Transactional(rollbackFor = {SQLException.class})
	public void updateContracts(List<Contract> contracts) throws SQLException   {
		PreparedStatement updateStmt = null;
		try {
			Session session = (Session) getEntityManager().getDelegate();
	        MyWork myWork = new MyWork();
	        session.doWork(myWork);
			Connection connection = myWork.getConnection();
			connection.setAutoCommit(false);
			updateStmt = connection.prepareStatement(UPDATE_CONTRACT);
			java.sql.Date dateNow =  new java.sql.Date(new Date().getTime());
			Iterator<Contract> it = contracts.iterator();
			while (it.hasNext()) {
				Contract contract = it.next();
				updateStmt.setString(COVENANT_ENTRY_NUMBER_UPDATE_INSERT_POSITION,contract.getEntryNumber());
				updateStmt.setString(STATUS_UPDATE_POSITION,contract.getStatus());
				updateStmt.setDate(DATE_UPDATE_POSITION, dateNow);
				updateStmt.addBatch();
			}
			updateStmt.executeBatch();
			connection.commit();	
		} catch (SQLException e) {
			LOGGER.error("Erro em gravação de registro.", e);
		} finally {
			if(Objects.nonNull(updateStmt)) {
				updateStmt.close();
			}
		}
	}
	

	@SuppressWarnings("unchecked")
	public Contract findByContraint(Contract contract) {
		Query query = getEntityManager().createQuery("select o from Contract o where o.codCovenant = :codCovenant and o.wallet = :wallet and o.contractNumber = :contractNumber and o.cnpjCpf = :cnpjCpf and o.registration = :registration");
		query.setParameter("codCovenant", contract.getCodCovenant());
		query.setParameter("wallet", contract.getWallet());
		query.setParameter("contractNumber", contract.getContractNumber());
		query.setParameter("cnpjCpf", contract.getCnpjCpf());
		query.setParameter("registration", contract.getRegistration());
		List<Contract> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
	
	private static class MyWork implements Work {

	    private Connection conn;

	    @Override
	    public void execute(Connection arg0) throws SQLException {
	        this.conn = arg0;
	    }

	    public Connection getConnection() {
	        return conn;
	    }

		public MyWork() {
			super();
			
		}
	}
	
}
