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

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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

import br.com.easygame.dao.UsuarioDAO;
import br.com.easygame.entity.Usuario;

/**
 * @author Alexandre
 *
 */
@Named
@Path(value = "usuario")
public class UsuarioService {

	private UsuarioDAO usuarioDAO;

	public UsuarioService() {
	}

	@Inject
	public UsuarioService(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cadastrarUsuario(JsonObject json) throws Exception {
		Response response;
		Usuario usuario = new Usuario();
		usuario = usuario.toUsuario(json);
		if (usuarioDAO.existeLogin(usuario.getLogin())) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.CONFLICT);
		}
		usuarioDAO.salvar(usuario);
		usuarioDAO.flush();

		URI uri = UriBuilder.fromUri("usuario/{id}").build(usuario.getId());
		return response = Response.created(uri).entity(usuario).build();
	}

	@GET
	@Path("{id}")
	public Usuario retornaUsuario(@PathParam("id") String id) {
		Usuario usuario = usuarioDAO.pesquisarPorId(Long.valueOf(id));
		if (usuario != null) {
			return usuario;
		}

		throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
	}

	@PUT
	@Path("{id}")
	public void atualizarUsuario(@PathParam("id") String id, JsonObject jsonObject) {
		if (usuarioDAO.existeLogin(jsonObject.getString("login"))) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.CONFLICT);
		}
		Usuario usuarioBanco = retornaUsuario(id);
		usuarioBanco.toUsuario(jsonObject);
		usuarioDAO.editar(usuarioBanco);
		usuarioDAO.flush();

	}

	@DELETE
	@Path("{id}")
	public void apagarUsuario(@PathParam("id") String id) {
		Usuario usuarioBanco = retornaUsuario(id);
		usuarioDAO.excluir(usuarioBanco);
		usuarioDAO.flush();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listarUsuarios() {
		try {
			// aqui um exemplo de como retornar todos os usuarios com JSON
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

	@GET
	@Path("resources/{nome}")
	@Produces("image/*")
	public Response recuperaImagem(@PathParam("nome") String nomeImagem) throws IOException {
		InputStream is = UsuarioService.class.getResourceAsStream("/" + nomeImagem + ".jpg");
		if (is == null) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
		}
		byte[] dados;
		dados = new byte[is.available()];
		is.read(dados);
		salvarImagem(dados);
		is.close();
		return Response.ok(dados).type("image/jpg").build();

	}
	
	@POST
	@Path("resources/{nome}")
	@Produces("image/*")
	public Response criarImagem(@PathParam("nome") String nomeImagem) throws IOException {
		InputStream is = UsuarioService.class.getResourceAsStream("/" + nomeImagem + ".jpg");
		if (is == null) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
		}
		byte[] dados;
		dados = new byte[is.available()];
		is.read(dados);
		is.close();
		
		return Response.ok(dados).type("image/jpg").build();

	}

	
	public void salvarImagem(byte[]bytes) throws IOException{
		 ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
	 
	        //ImageIO is a class containing static methods for locating ImageReaders
	        //and ImageWriters, and performing simple encoding and decoding. 
	 
	        ImageReader reader = (ImageReader) readers.next();
	        Object source = bis; 
	        ImageInputStream iis = ImageIO.createImageInputStream(source); 
	        reader.setInput(iis, true);
	        ImageReadParam param = reader.getDefaultReadParam();
	 
	        BufferedImage image = reader.read(0, param);
	        //got an image file
	 
	        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        //bufferedImage is the RenderedImage to be written
	 
	        Graphics2D g2 = bufferedImage.createGraphics();
	        g2.drawImage(image, null, null);
	 
	        File imageFile = new File("/home/alexandre/teste.jpg");
	        ImageIO.write(bufferedImage, "jpg", imageFile);
	 
	        System.out.println(imageFile.getPath());
	    }


}
