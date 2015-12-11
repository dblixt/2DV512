package dv512.model;

import java.io.IOException;

import org.ektorp.impl.StdObjectMapperFactory;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@TypeDiscriminator("doc.doc_type == 'user'")
@JsonSerialize(using = User.Serializer.class)
@JsonDeserialize(using = User.Deserializer.class)
public class User extends CouchDbDocument {	
	private static final long serialVersionUID = 1L;
	
	private static final String DOC_TYPE = "user";
		
	private String email;	
	private String password;
	
	private Profile profile;

	public User() {
		profile = new Profile();
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	
	
	/**
	 * Custom serialize class.
	 * 
	 * This is needed to avoid ugly structure since
	 * documents have to be store in geojson format in
	 * the database to take advantage of the geospartial
	 * functionality in cloudant.
	 * 
	 * We loose some flexibility in the User class though.
	 * But the Profile class will still be flexible.
	 * 
	 * @author Daniel Nilsson
	 *
	 */
	public static class Serializer extends JsonSerializer<User> {

		@Override
		public void serialize(User value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			
			jgen.writeStartObject();
			
			if(value.getId() != null) {
				jgen.writeStringField("_id", value.getId());
			}
			
			if(value.getRevision() != null) {
				jgen.writeStringField("_rev", value.getRevision());
			}
			
			jgen.writeStringField("doc_type", DOC_TYPE);
			         
            // write geometry object.
            jgen.writeObjectFieldStart("geometry"); // {            
            jgen.writeArrayFieldStart("coordinates"); //  [
            jgen.writeNumber(value.getProfile().getLongitude());
            jgen.writeNumber(value.getProfile().getLatitude()); 
            jgen.writeEndArray(); // ]
            jgen.writeStringField("type", "Point"); // geosjon cord type, always same.
            jgen.writeEndObject(); // }
            // end geometry object.
            
            // write properties object holding our data
            jgen.writeObjectFieldStart("properties");
            jgen.writeStringField("email", value.getEmail());
            jgen.writeStringField("password", value.getPassword());
            jgen.writeObjectField("profile", value.getProfile());
            jgen.writeEndObject();
            // end properties object.
                       
            jgen.writeStringField("type", "Feature"); // geojson specific.         
			jgen.writeEndObject();
		}		
	}
		
	/**
	 * Custom deserialize class.
	 * @author Daniel Nilsson
	 *
	 */
	public static class Deserializer extends JsonDeserializer<User> {
		
		@Override
		public User deserialize(JsonParser jp, DeserializationContext ctx)
				throws IOException, JsonProcessingException {
		
			if(jp.getCodec() == null) {
				// codec is sometimes null for some reason, not
				// sure if it is a bug, this is workaround.
				jp.setCodec(new StdObjectMapperFactory().createObjectMapper());
				System.err.println("setting object mapper.");
			}
			
			User u = new User();
			JsonNode root = jp.readValueAsTree();
			
			u.setId(root.get("_id").asText());
			u.setRevision(root.get("_rev").asText());
						
			JsonNode geometry = root.get("geometry");
			JsonNode coords = geometry.get("coordinates");
			if(coords != null && coords.isArray()) {
				u.getProfile().setLongitude(coords.get(0).asDouble(0));
				u.getProfile().setLatitude(coords.get(1).asDouble(0));
			}
			
			JsonNode props = root.get("properties");
			u.setEmail(props.get("email").asText());
			u.setPassword(props.get("password").asText());
						
			JsonNode profile = props.get("profile");	
			u.setProfile(jp.getCodec().treeToValue(profile, Profile.class));
			
			return u;
		}
		
	}
	
}
