/**
 * 
 */
package br.com.easygame.entity.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author alexandre
 *
 */
public class ConverterData {

	private String pattern;
	private String data;

	public ConverterData() {
	}

	public ConverterData(String pattern, String data) {
		this.pattern = pattern;
		this.data = data;
	}

	public Timestamp dataStringToTimestamp() {
//		String str = "1986-04-08 12:30:25";
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		LocalDateTime dateTime = LocalDateTime.parse(data, formatter);
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		return timestamp;
	}

}
