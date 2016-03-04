/**
 * 
 */
package br.com.easygame.servico;

import java.io.StringReader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.easygame.dao.UsuarioDAO;

/**
 * @author Alexandre
 *
 */
@Named
@Path(value = "login")
public class LoginService {

	
	private UsuarioDAO usuarioDAO;

	public LoginService() {
	}
	
	@Inject
	public LoginService(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonObject validarLogin(String json) throws Exception {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		try {
			boolean autenticar = false;
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject jsonObject = jsonReader.readObject();
			String login = jsonObject.getString("login");
			String senha = jsonObject.getString("senha");
			if (login != null && senha != null) {
				autenticar = usuarioDAO.autenticar(login, senha);
			}
			if (!autenticar) {
				throw new WebApplicationException(javax.ws.rs.core.Response.Status.FORBIDDEN);
			}
			
			return builder.add("objeto", "ok").build();

		} catch (Exception e) {
			e.getCause();
		}
		return builder.add("erro", "erro").build() ;
	}

}
