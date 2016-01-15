/**
 * 
 */
package br.com.easygame.servico;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Usuario;

/**
 * @author Alexandre
 * TEste de commit
 */
@Named
@Path(value = "equipe")
	public class EquipeService {
	private EquipeDAO equipeDAO;
	private UsuarioDAO usuarioDAO;
	
	public EquipeService() {
	}
	
	@Inject
	public EquipeService(EquipeDAO equipeDAO, UsuarioDAO usuarioDAO) {
		this.equipeDAO = equipeDAO;
		this.usuarioDAO = usuarioDAO; 
	}

	//fez tudo OK HTTP CREATED 201
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cadastrarEquipe(String json) throws Exception {
		try {
//			Equipe equipeJson = Equipe.toEquipe(json);
//			equipeDAO.salvar(equipeJson);
//
//			Response response = Response.status(Response.Status.CREATED)
//					.entity("sadjgdagsdjh")
//					.location(UriBuilder.fromUri("dsds").build(equipeJson.getId())).build();
			//localhost:8080/easy-game/equipe/19
//			return response;
		} catch (Exception e) {
			e.getCause();
			Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("json").build();
		}
		//return Json.createObjectBuilder().add("erro", "Não salvou o equipe").build().toString();
		return null;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listarEquipes() {
		try {
			List<Usuario> usuarios = usuarioDAO.listarTodos();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Usuario usuario : usuarios) {
				arrayBuilder.add(Json.createObjectBuilder().add("id", usuario.getId().toString())
						.add("login", usuario.getLogin()).add("senha", usuario.getSenha())
						.add("latitude", usuario.getLatitude()).add("longitude", usuario.getLongitude()));

			}
			return arrayBuilder.build().toString();

		} catch (Exception e) {
			e.getCause();
		}
		return "não listou";
	}

}
