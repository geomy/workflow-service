package org.ng.workflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class CustomGeneratedData {

	private static final Logger logger = LoggerFactory.getLogger(CustomGeneratedData.class);
	public static String GenerateUniqueID() {
		UUID uuid = UUID.randomUUID();
        return uuid.toString();
	}

	public static long getTimeStamp() {
		return new Date().getTime();
	}

	public static LocalDate stringToLocalDate(String formDate) {
		LocalDate convertedDate = LocalDate.parse(formDate);
		return convertedDate;
	}

	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
		return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime getLocalDate(long valueToConvert) {
		Date currentDate = new Date(valueToConvert);
		return currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDate stringToDate(String dateToConvert) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return LocalDate.parse(dateToConvert, formatter);
	}

	public static LocalDate stringToLocalDateFormat(String dateToConvert) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(dateToConvert, formatter);
	}

	public static LocalDate stringToDateFormat(String dateToConvert) throws ParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd-MM-yyyy][yyyy-MM-dd]");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate receivedDate = LocalDate.parse(dateToConvert, formatter);
		String returnDateStr = receivedDate.format(formatter2);

		return LocalDate.parse(returnDateStr, formatter2);
	}

	public static  <T> T convertJSONToObject(Class clazz, String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return (T)  mapper.readValue(json, clazz);
		} catch (Exception je) {
			throw new RuntimeException("Error interpreting JSON response", je);
		}
	}

	public static  String convertObjectToJson(Object input) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(input);
		} catch ( JsonProcessingException jpe) {
			throw new RuntimeException("Error converting Object to json", jpe);
		}
	}
	public static String convertJson2Xml(String json){
		JSONObject jsonObject = new JSONObject(json);
		String xmlString = XML.toString(jsonObject);
		return xmlString;
	}


}
