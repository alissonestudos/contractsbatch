package br.com.santander.ole.datasource;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.altec.bsbr.fw.jdbc.datasource.PoolingBatchDataSource;

import br.com.santander.ba.credentialsclient.BACredentialsClient;
import br.com.santander.ba.credentialsclient.Credentials;
import br.com.santander.dlb.dlbies.CipherException;

public class CgnCredentialsDataSource extends PoolingBatchDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CgnCredentialsDataSource.class);

	public CgnCredentialsDataSource() {
		super();
	}

	public CgnCredentialsDataSource(String enviroment, String path, String sigla, String index) {
			this.setMBSConection(enviroment, path, sigla, index);
	}

	private void setMBSConection(String enviroment, String path, String sigla, String index) {
		try {
			BACredentialsClient client = new BACredentialsClient(enviroment, path);
			Credentials credentials;
			credentials = client.getCredentials(sigla, index);
			setUsername(credentials.getUser());
			setPassword(credentials.getPassword());
		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | SignatureException
				| ParseException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
				| IOException | org.json.simple.parser.ParseException | CipherException ex) {
			
			LOGGER.error("Falha ao obter usuario/senha do banco de dados", ex);
		}
	}

}
