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

import br.com.easygame.dao.EventoDAO;
import br.com.easygame.entity.Evento;

/**
 * @author Alexandre
 *
 */
@Named
@Path(value = "agenda")
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AgendaService {

	private EventoDAO eventoDAO;

	public AgendaService() {
	}

	@Inject
	public AgendaService(EventoDAO eventoDAO) {
		this.eventoDAO = eventoDAO;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response cadastrarEvento(JsonObject json) throws Exception {
		Response response;
		Evento evento = Evento.toEvento(json);
		eventoDAO.salvar(evento);
		eventoDAO.flush();

		URI uri = UriBuilder.fromUri("evento/{id}").build(evento.getId());
		return response = Response.created(uri).build();
	}

	@GET
	@Path("{id}")
	public JsonObject retornaUsuario(@PathParam("id") Long id) {
		Evento evento = eventoDAO.pesquisarPorId(id);
		if (evento != null) {
			return evento.toJSON();
		}

		throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualizarEvento(@PathParam("id") Long id, JsonObject jsonObject) {
		Evento eventoBanco = eventoDAO.pesquisarPorId(id);
		eventoBanco = Evento.toEvento(jsonObject);
		eventoDAO.editar(eventoBanco);
		eventoDAO.flush();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void apagarEvento(@PathParam("id") Long id) {
		// TODO criar uma logica para avisar do cancelamento do evento

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listarUsuarios() {
		try {
			// aqui um exemplo de como retornar todos os usuarios com JSON
			List<Evento> eventos = eventoDAO.listar();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Evento evento : eventos) {
				arrayBuilder.add(evento.toJSON());

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
		InputStream is = AgendaService.class.getResourceAsStream("/" + nomeImagem + ".jpg");
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
		InputStream is = AgendaService.class.getResourceAsStream("/" + nomeImagem + ".jpg");
		if (is == null) {
			throw new WebApplicationException(javax.ws.rs.core.Response.Status.NOT_FOUND);
		}
		byte[] dados;
		dados = new byte[is.available()];
		is.read(dados);
		is.close();

		return Response.ok(dados).type("image/jpg").build();

	}

	public void salvarImagem(byte[] bytes) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");

		// ImageIO is a class containing static methods for locating
		// ImageReaders
		// and ImageWriters, and performing simple encoding and decoding.

		ImageReader reader = (ImageReader) readers.next();
		Object source = bis;
		ImageInputStream iis = ImageIO.createImageInputStream(source);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();

		BufferedImage image = reader.read(0, param);
		// got an image file

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		// bufferedImage is the RenderedImage to be written

		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, null, null);

		File imageFile = new File("/home/alexandre/teste.jpg");
		ImageIO.write(bufferedImage, "jpg", imageFile);

		System.out.println(imageFile.getPath());
	}

}
