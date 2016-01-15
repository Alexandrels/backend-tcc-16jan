package br.com.easygame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * @author Alexandre Leite - 16/12/2014
 * 
 */
public final class DataUtils {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Utils.getLocale());

	/**
	 * Converte o parâmetro <code>data</code> em uma data.
	 * 
	 * @param data
	 *            A {@link String} que será convertida para uma data.
	 * @param pattern
	 *            O padrão que será aplicado na conversão.
	 * @return A data que foi convertida ou null se ocorrer algum erro na
	 *         conversão.
	 */
	public static synchronized Date parseDate(String data, String pattern) {
		if (data == null) {
			return null;
		}
		try {
			dateFormat.applyPattern(pattern);
			return dateFormat.parse(data);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Converte o parâmetro <code>data</code> em uma data.
	 * 
	 * @param data
	 *            A {@link String} que será convertida para uma data.
	 * @param pattern
	 *            O padrão que será aplicado na conversão.
	 * @return A data que foi convertida ou null se ocorrer algum erro na
	 *         conversão.
	 */
	public static synchronized LocalDate parseLocalDate(String data, String pattern) {
		return DateTimeFormat.forPattern(pattern).withLocale(Utils.getLocale()).parseLocalDate(data);
	}

	/**
	 * Formata uma {@link Date} de acordo com o padrão informado.
	 * 
	 * @param data
	 *            A {@link Date} que será formatada.
	 * @param pattern
	 *            O padrão que será aplicado para formatar a data.
	 * @return A {@link Date} formatada.
	 */
	public static synchronized String formatarDate(Date data, String pattern) {
		if (data == null) {
			return null;
		}
		dateFormat.applyPattern(pattern);
		return dateFormat.format(data);
	}

	/**
	 * Formata uma {@link LocalDate} de acordo com o padrão informado.
	 * 
	 * @param data
	 *            A {@link LocalDate} que será formatada.
	 * @param pattern
	 *            O padrão que será aplicado para formatar a data.
	 * @return A {@link LocalDate} formatada.
	 */
	public static synchronized String formatarDate(LocalDate data, String pattern) {
		if (data == null) {
			return null;
		}
		return DateTimeFormat.forPattern(pattern).withLocale(Utils.getLocale()).print(data);
	}

	/**
	 * Altera o horário de uma data para a primeira hora minuto segundo do dia.
	 * 
	 * @param date
	 *            A data que será convertida.
	 * @return A data midificada.
	 */
	public static Date dataPrimeiraHora(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Altera o horário de uma data para a última hora minuto segundo do dia.
	 * 
	 * @param date
	 *            A data que será convertida.
	 * @return A data midificada.
	 */
	public static Date dataUltimaHora(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
}
