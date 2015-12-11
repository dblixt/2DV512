package dv512.model.lucene;

import java.util.List;

import org.ektorp.http.HttpResponse;
import org.ektorp.http.StdResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LuceneDocResponseHandler<T> extends StdResponseHandler<List<T>> {
	
	private LuceneQueryResultParser<T> parser;

	public LuceneDocResponseHandler(Class<T> docType, ObjectMapper om) {
		parser = new LuceneQueryResultParser<T>(docType, om);
	}

	
	@Override
	public List<T> success(HttpResponse hr) throws Exception {
		parser.parseResult(hr.getContent());
		return parser.getRows();
	}
	
}
