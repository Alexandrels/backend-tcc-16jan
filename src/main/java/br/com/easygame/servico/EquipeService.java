/**
 * 
 */
package br.com.easygame.servico;

import java.net.URI;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.easygame.dao.EquipeDAO;
import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Equipe;
import br.com.easygame.entity.Usuario;
import br.com.easygame.enuns.TipoPosicao;

/**
 * @author Alexandre TEste de commit
 */
@Named
@Path(value = "equipe")
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
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

	// fez tudo OK HTTP CREATED 201
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response cadastrarEquipe(JsonObject jsonObject) throws Exception {
		Response response;
		Equipe equipe = Equipe.toEquipe(jsonObject);
		if (equipeDAO.existeEquipe(equipe)) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.CONFLICT);
		}
		equipeDAO.salvar(equipe);
		equipeDAO.flush();

		URI uri = UriBuilder.fromUri("equipe/{id}").build(equipe.getId());
		return response = Response.created(uri).entity(equipe).build();
	}

	@GET
	@Path("{id}")
	public JsonObject retornaEquipe(@PathParam("id") Long id) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		Equipe equipe = equipeDAO.pesquisarPorId(id);
		if (equipe != null) {
			builder.add("objeto", equipe.toJSON());
			return builder.build();
		}

		throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualizarEquipe(@PathParam("id") Long id, JsonObject jsonObject) {
		Equipe equipeBanco = equipeDAO.pesquisarPorId(id);
		Equipe equipe = Equipe.toEquipe(jsonObject);
		// TODO falta validar melhor se equipe ja nãoexiste mesmodepois de
		// editar
		if (!equipeBanco.naoAlterouNomeDaequipeDoUsuario(equipe)) {
			equipe = equipeBanco;
			if (equipeDAO.existeEquipe(equipeBanco)) {
				throw new WebApplicationException(javax.ws.rs.core.Response.Status.CONFLICT);
			}
		}
		equipeDAO.editar(equipe);
		equipeDAO.flush();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void apagarEquipe(@PathParam("id") Long id) {
		Equipe equipeBanco = equipeDAO.pesquisarPorId(id);
		// TODO falta criar o metodo de exclusão sócopiar do usuarioDAO

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject listarEquipes() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		try {
			List<Equipe> equipes = equipeDAO.listar();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Equipe equipe : equipes) {
				arrayBuilder.add(equipe.toJSON());

			}
			builder.add("objeto", Json.createObjectBuilder().add("array", arrayBuilder.build()));
			return builder.build();

		} catch (Exception e) {
			e.getCause();
		}
		return Json.createObjectBuilder().add("erro", "Não listou as equipes").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("convite")
	@Transactional
	public JsonObject cadastrarUsuarioConvidado(JsonObject jsonObject) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		try {

			Response response;
			Long idEquipe = Long.valueOf(jsonObject.getInt("equipe"));
			Long idUsuario = Long.valueOf(jsonObject.getInt("usuario"));
			int posicaoConvite = jsonObject.getInt("posicaoConvite");

			Equipe equipe = equipeDAO.pesquisarPorId(idEquipe);
			Usuario usuario = usuarioDAO.pesquisarPorId(idUsuario);
			TipoPosicao tipoPosicao = TipoPosicao.values()[posicaoConvite];

			equipe.adicionarUsuario(usuario, tipoPosicao);

			equipeDAO.editar(equipe);
			equipeDAO.flush();
			
			return builder.add("objeto", Json.createObjectBuilder().add("convite", "sucesso")).build();
		} catch (Exception e) {
			return builder.add("objeto", Json.createObjectBuilder().add("convite", "erro")).build();
		}
	}

}
