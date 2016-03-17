/**
 * 
 */
package br.com.easygame.servico;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.dao.UsuarioEquipeDAO;
import br.com.easygame.entity.Usuario;
import br.com.easygame.entity.UsuarioEquipe;
import br.com.easygame.enuns.TipoUsuario;

/**
 * @author Alexandre
 *
 */
@Named
@Path(value = "usuarioEquipe")
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UsuarioEquipeService {

	private UsuarioDAO usuarioDAO;
	private UsuarioEquipeDAO usuarioEquipeDAO;

	public UsuarioEquipeService() {
	}

	@Inject
	public UsuarioEquipeService(UsuarioDAO usuarioDAO,UsuarioEquipeDAO usuarioEquipeDAO) {
		this.usuarioDAO = usuarioDAO;
		this.usuarioEquipeDAO = usuarioEquipeDAO;
	}


	@GET
	@Path("{id}")
	public Usuario retornaUsuario(@PathParam("id") Long id) {
		Usuario usuario = usuarioDAO.pesquisarPorId(id);
		if (usuario != null) {
			return usuario;
		}

		throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject listarConvitesPendentes(@PathParam("id") Long id) {
		try {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			Usuario usuario = retornaUsuario(id);
			// aqui um exemplo de como retornar todos os usuarios com JSON
			List<UsuarioEquipe> pendentes = usuarioEquipeDAO.listarUsuariosConvitesPendentes(usuario);
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (UsuarioEquipe pendente : pendentes) {
				arrayBuilder.add(pendente.toJSON());

			}
			builder.add("objeto", Json.createObjectBuilder().add("array", arrayBuilder.build()));
			return builder.build();

		} catch (Exception e) {
			e.getCause();
		}
		return Json.createObjectBuilder().add("erro", "Não listou as convites pendentes").build();
	}

	@GET
	@Path("coordenadas/{tipo}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject listarCoordenadasUsuarios(@PathParam("tipo") int tipo) {
		try {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			JsonObjectBuilder coordenadasJson = Json.createObjectBuilder();
			// aqui um exemplo de como retornar todos os usuarios com JSON
			List<Usuario> usuarios = usuarioDAO.listar(TipoUsuario.values()[tipo]);
			JsonArrayBuilder arrayUsuarios = Json.createArrayBuilder();
			for (Usuario usuario : usuarios) {
				arrayUsuarios.add(usuario.toUsuarioCoordenadasJSON());

			}
			coordenadasJson.add("coordenadas", arrayUsuarios);
			return builder.add("objeto", coordenadasJson).build();

		} catch (Exception e) {
			e.getCause();
		}
		return null;
	}
	
}
