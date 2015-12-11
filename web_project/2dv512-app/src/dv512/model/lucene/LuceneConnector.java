package dv512.model.lucene;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.RestTemplate;
import org.ektorp.impl.StdObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is an extension of the ektorp 
 * library. Since ektorp did not have 
 * support for the lucene queries that
 * are supported by cloudant this had to be written.
 *
 * @author Daniel Nilsson
 */
public class LuceneConnector {
	
	CouchDbConnector coudbDbConnector;
	private HttpClient httpClient;
	private RestTemplate restTemplate;
	
	protected final ObjectMapper objectMapper;
	
	public LuceneConnector(CouchDbConnector con) {		
		coudbDbConnector = con;
		httpClient = con.getConnection();		
		restTemplate = new RestTemplate(httpClient);
		objectMapper = new StdObjectMapperFactory().createObjectMapper();		
	}
	
	public <T> List<T> queryIndex(final LuceneQuery query, final Class<T> type) {
		query.dbPath(coudbDbConnector.path());			
		LuceneDocResponseHandler<T> rh = 
				new LuceneDocResponseHandler<T>(type, objectMapper);
	
		return restTemplate.get(query.buildQuery(), rh);
	}
	
}
