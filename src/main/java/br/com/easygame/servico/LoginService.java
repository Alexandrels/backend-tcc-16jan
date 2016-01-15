/**
 * 
 */
package br.com.easygame.servico;

import java.io.StringReader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

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
	public String validarLogin(String json) throws Exception {
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject jsonObject = jsonReader.readObject();
			String login = jsonObject.getString("login");
			String senha = jsonObject.getString("senha");
			if (login != null && senha != null) {
				boolean autenticar = usuarioDAO.autenticar(login, senha);
				return Json.createObjectBuilder().add("retorno", autenticar).build().toString();
			}

		} catch (Exception e) {
			e.getCause();
		}
		return Json.createObjectBuilder().add("erro", "Não conseguiu autenticar no banco").build().toString();
	}

}
