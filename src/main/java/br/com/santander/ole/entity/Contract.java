package br.com.santander.ole.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_CONTRACT", schema = "dbo")
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private String id;

	@Column(name = "COD_COVENANT")
	private String codCovenant;

	@Column(name = "WALLET")
	private String wallet;

	@Column(name = "CONTRACT_NUMBER")
	private String contractNumber;

	@Column(name = "ENTRY_NUMBER")
	private String entryNumber;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CNPJ_CPF")
	private String cnpjCpf;

	@Column(name = "TRANCHE_VALUE")
	private String trancheValue;

	@Column(name = "REGISTRATION")
	private String registration;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "CHANGED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date changedDate;
	
	@Transient
	private boolean firstExecution;
	

	public Contract() {
		super();
	}

}
