package br.com.santander.ole.producer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.altec.bsbr.fw.batch.BatchErrorException;
import com.altec.bsbr.fw.batch.Parameter;
import com.altec.bsbr.fw.batch.RecordProducer;
import com.google.common.collect.Lists;

import br.com.santander.ole.dto.LinesDTO;

public class WalletProducer implements RecordProducer<LinesDTO> {

	private static final int ERROR_CODE = -1;

	private static final Logger LOGGER = LoggerFactory.getLogger(WalletProducer.class);
	private static final String OLE_WALLET = "169";
	private static final String STD_WALLET = "033";
	private static final String STD_PATH = "std";

	@Value("${work.dir}")
	private String workdir;
	
	@Value("${app.chunk}")
	private Integer chunk;
	
	private Iterator<LinesDTO> contractsIterator;
	private List<LinesDTO> contractsList;
	

	public WalletProducer() {
		super();
	}

	/**
	 * Metodo de inicalização apos o contrutor.
	 * @throws IOException 
	 * @throws CsvException 
	 */
	@PostConstruct
	public void init() {
		contractsList = new ArrayList<>();
		this.getFile(this.workdir);
	}

	/**
	 * Recupera o proximo arquivo.
	 */
	@Override
	public boolean hasNext() {
		return contractsIterator.hasNext();
	}

	/**
	 * Recupera o proximo arquivo.
	 */
	@Override
	public LinesDTO next() {
		LinesDTO lines = contractsIterator.next();
		LOGGER.info("Enviando lote de registros, wallet: {}", lines.getWallet());
		return lines;
	}

	private void getFile(String path){
		File[] files;
		File in = null;
		in = new File(path);
		if (in.exists()) {
			files = in.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					File[] dirs = file.listFiles();
					if (dirs.length > 0) {
						for (File walletFile : dirs) {
							LOGGER.info("Carregando arquivo {} ...", walletFile.getName());
							this.readFile(walletFile);
							LOGGER.info("Arquivo {} carregado com sucesso.", walletFile.getName());
							boolean fileRemoved = walletFile.delete();
							if(fileRemoved) {
								LOGGER.info("Arquivo {} removido com sucesso.", walletFile.getName());
							}
						}
					}
				}
			}
		}
	}
	
	
	private void readFile(File file)  { 
		String wallet = null;
		try {
			if (file.getAbsolutePath().contains(STD_PATH)) {
				wallet = STD_WALLET;
			} else {
				wallet = OLE_WALLET;
			}
			List<String> lines = Files.readAllLines(file.toPath());
			List<List<String>> chunckedList = Lists.partition(lines , this.chunk);
			LinesDTO dto = null;
			for (List<String> list : chunckedList) {
				dto = new LinesDTO();
				dto.setLines(list);
				dto.setWallet(wallet);
				contractsList.add(dto);
			}
			this.contractsIterator = contractsList.iterator();
		}catch (IOException e) {
			int erroCode = ERROR_CODE;
			String msg = "Erro em carramento de arquivo {}";
			LOGGER.error(msg, file.getName(), e);
			throw new BatchErrorException(erroCode, msg, e);
		}
	}


	/**
	 * Seta parametros no job.
	 */
	@Override
	public void setParameters(List<Parameter> parameters) {
		throw new UnsupportedOperationException();
	}

	/**
	 * recupera parametros no job.
	 */
	@Override
	public List<Parameter> getParameters() {
		throw new UnsupportedOperationException();
	}
}
