package dv512.model.lucene;

import org.ektorp.http.URI;

public class LuceneQuery {

	private final static int NOT_SET = -1;
	
	private String dbPath;
	private String designDocId;
    private String indexName;
    private String query;
    
    private String sort;
	private int limit = NOT_SET;
	private boolean includeDocs = false;
	
	
  
	public LuceneQuery dbPath(String s) {
		dbPath = s;
		return this;
	}
	
    public LuceneQuery designDocId(String s) {
    	designDocId = s;
    	return this;
    }
    
    public LuceneQuery indexName(String s) {
    	indexName = s;
    	return this;
    }
    
    public LuceneQuery query(String s) {
    	query = s;
    	return this;
    }
    
    public LuceneQuery limit(int i) {
    	limit = i;
    	return this;
    }
    
    public LuceneQuery sort(String s) {
    	sort = s;
    	return this;
    }
    
    public LuceneQuery includeDocs(boolean b) {
    	includeDocs = b;
    	return this;
    }
	
    
    private URI buildIndexPath() {
		URI uri = URI.of(dbPath);
		
		uri.append(designDocId);
		uri.append(indexName);

		return uri;
	}


    public String buildQuery() {
    	return buildQueryURI().toString();
    }

    private URI buildQueryURI() {
		URI q = buildIndexPath();

		if(query != null) {
			q.param("q", query);
		}
		
		if(sort != null) {
			q.param("sort", sort);
		}
				
		if(limit != NOT_SET) {
			q.param("limit", limit);
		}
	
		if(includeDocs) {
			q.param("include_docs", "true");
		}
		
	
		return q;
	}

}
