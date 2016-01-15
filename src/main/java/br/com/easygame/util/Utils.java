package br.com.easygame.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Classe utilitária.
 *
 * @author Geraldo Vieira
 * @version 1.0
 */
public class Utils {

	/**
	 * @return
	 */
	public static Locale getLocale() {
		return new Locale("pt", "BR");
	}

	/**
	 * Compacta um diretório no formato <code>zip</code>.
	 *
	 * @param dir2zip
	 *            diretório
	 * @param zipFile
	 *            nome do arquivo compactado
	 * @throws IOException
	 */
	public static void zipDir(File dir2zip, File zipFile, String extensao, boolean zipSubPastas) throws IOException {
		ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));
		zipDir(dir2zip, outputStream, extensao, zipSubPastas);
		outputStream.close();
	}

	/**
	 * Monta o <code>Stream</code> do diretório compactado no formato
	 * <code>zip</code>.
	 *
	 * @param dir2zip
	 *            diretório
	 * @param zos
	 *            stream
	 * @throws IOException
	 */
	public static void zipDir(File dir2zip, ZipOutputStream zos, String extensao, boolean zipSubPastas)
			throws IOException {
		_zip(dir2zip, zos, dir2zip.getPath(), extensao, zipSubPastas);
	}

	public static void zipFile(File arquivo, File arquivoZip) throws IOException {
		ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(arquivoZip));
		FileInputStream fis = new FileInputStream(arquivo);
		ZipEntry anEntry = new ZipEntry(arquivo.getName());
		outputStream.putNextEntry(anEntry);
		byte[] readBuffer = new byte[512];
		int bytesIn = 0;
		while ((bytesIn = fis.read(readBuffer)) != -1) {
			outputStream.write(readBuffer, 0, bytesIn);
		}
		fis.close();
		outputStream.closeEntry();
		outputStream.close();
	}

	/** implementação */
	private static void _zip(File dir2zip, ZipOutputStream zos, String path, final String extensao,
			boolean zipSubPastas) throws IOException, FileNotFoundException {
		File[] dirList = null;
		if (extensao != null) {
			dirList = dir2zip.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return pathname.getName().toLowerCase().endsWith(extensao.toLowerCase());
				}
			});
		} else {
			dirList = dir2zip.listFiles();
		}
		byte[] readBuffer = new byte[512];
		int bytesIn = 0;
		for (File f : dirList) {
			if (f.isDirectory()) {
				if (zipSubPastas) {
					_zip(f, zos, path, extensao, zipSubPastas);
				}
				continue;
			}
			FileInputStream fis = new FileInputStream(f);
			ZipEntry anEntry = new ZipEntry(f.getPath().substring(path.length() + 1));
			zos.putNextEntry(anEntry);
			while ((bytesIn = fis.read(readBuffer)) != -1) {
				zos.write(readBuffer, 0, bytesIn);
			}
			fis.close();
		}
	}

	/**
	 * Extrai um arquivo zip.
	 *
	 * @param arquivoZip
	 *            O arquivo zip que será extraido.
	 * @param diretorio
	 *            O destino onde os arquivos serão extraidos.
	 * @throws ZipException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void extrairZip(File arquivoZip, File diretorio) throws ZipException, IOException {
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[2048];
		try {
			// cria diretório informado, caso não exista
			if (!diretorio.exists()) {
				if (!diretorio.mkdirs()) {
					throw new IOException("Não foi possível criar o diretório " + diretorio.getPath());
				}
			}

			if (!diretorio.isDirectory()) {
				throw new IOException(diretorio.getPath() + " não é um diretório");
			}
			zip = new ZipFile(arquivoZip);
			Enumeration e = zip.entries();
			while (e.hasMoreElements()) {
				ZipEntry entrada = (ZipEntry) e.nextElement();
				arquivo = new File(diretorio, entrada.getName());
				// se for diretório inexistente, cria a estrutura
				// e pula pra próxima entrada
				if (entrada.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
				}
				// se a estrutura de diretórios não existe, cria
				if (!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				try {
					// lê o arquivo do zip e grava em disco
					is = zip.getInputStream(entrada);
					os = new FileOutputStream(arquivo);
					int bytesLidos = 0;
					if (is == null) {
						throw new ZipException("Erro ao ler a entrada do zip: " + entrada.getName());
					}
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Copia um arquivo de um local para outro.
	 *
	 * @param srFile
	 *            arquivo de origem.
	 * @param dtFile
	 *            arquivo de destino.
	 * @throws IOException
	 */
	public static void copyfile(File srFile, File dtFile) throws IOException {
		InputStream in = new FileInputStream(srFile);

		// For Append the file.
		// OutputStream out = new FileOutputStream(f2,true);

		// For Overwrite the file.
		OutputStream out = new FileOutputStream(dtFile);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Obtem as coordenadas GPS de um determinado endereço.
	 *
	 * @param endereco
	 *            O endereço que será pesquisado a Coordenada.
	 * @return As coordenadas do endereço passado. O primeiro elemento da lista
	 *         é a Latitude e o segundo a Longitude. Se não for encotrada
	 *         coordenada do endereço retorna uma lista vazia.
	 */
	public static List<Double> getCoordenadas(String endereco) {
		endereco += ", Brasil";
		List<Double> coordenadas = getCoordenadasGoogle(endereco);
		if (coordenadas.isEmpty()) {
			coordenadas = getCoordenadasYahoo(endereco);
		}
		return coordenadas;
	}

	/**
	 * Obtem as coordenadas GPS de um determinado endereço acessando o
	 * WebService do google.
	 *
	 * @param endereco
	 *            O endereço que será pesquisado a Coordenada.
	 * @return As coordenadas do endereço passado. O primeiro elemento da lista
	 *         é a Latitude e o segundo a Longitude. Se não for encotrada
	 *         coordenada do endereço retorna uma lista vazia.
	 */
	private static List<Double> getCoordenadasGoogle(String endereco) {
		List<Double> coordenadas = new ArrayList<Double>();
		try {
			StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/geocode/xml?").append("address=")
					.append(URLEncoder.encode(endereco, "UTF-8")).append("&").append("sensor=")
					.append(URLEncoder.encode("false", "UTF-8"));

			URL siteUrl = new URL(url.toString());
			HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String local = "<location>";
			while ((line = in.readLine()) != null) {
				if (line.contains(local)) {
					line = in.readLine().trim();
					String latitude = line.substring(5, line.indexOf("</lat>"));
					line = in.readLine().trim();
					String longitude = line.substring(5, line.indexOf("</lng>"));
					coordenadas.add(new Double(latitude));
					coordenadas.add(new Double(longitude));
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			coordenadas = new ArrayList<Double>();
		}
		return coordenadas;
	}

	/**
	 * Obtem as coordenadas GPS de um determinado endereço acessando o
	 * WebService do Yahoo.
	 *
	 * @param endereco
	 *            O endereço que será pesquisado a Coordenada.
	 * @return As coordenadas do endereço passado. O primeiro elemento da lista
	 *         é a Latitude e o segundo a Longitude. Se não for encotrada
	 *         coordenada do endereço retorna uma lista vazia.
	 */
	public static List<Double> getCoordenadasYahoo(String endereco) {
		List<Double> coordenadas = new ArrayList<Double>();
		try {
			StringBuilder url = new StringBuilder("http://where.yahooapis.com/geocode?").append("q=")
					.append(URLEncoder.encode(endereco, "UTF-8")).append("&").append("appid=")
					.append(URLEncoder.encode("f1YzJj7k", "UTF-8"));

			URL siteUrl = new URL(url.toString());
			HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String local = "<quality>";
			while ((line = in.readLine()) != null) {
				if (line.contains(local)) {
					int indexLatitude = line.indexOf("<latitude>");
					int indexLongitude = line.indexOf("<longitude>");
					if (indexLatitude == -1 || indexLongitude == -1) {
						continue;
					}
					String latitude = line.substring(indexLatitude + 10, line.indexOf("</latitude>"));
					String longitude = line.substring(indexLongitude + 11, line.indexOf("</longitude>"));
					coordenadas.add(new Double(latitude));
					coordenadas.add(new Double(longitude));
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			coordenadas = new ArrayList<Double>();
		}
		return coordenadas;
	}

}