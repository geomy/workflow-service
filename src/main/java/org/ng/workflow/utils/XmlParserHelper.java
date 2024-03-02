/**
 * 
 */
package org.ng.workflow.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author masoya
 * 
 *  XmlParserHelper class named attributes.
 *
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@Component
public class XmlParserHelper {

	private String xmlString;

	public XmlParserHelper(String xmlString) {		
		this.xmlString = xmlString;
	}	

	public XmlParserHelper parse(String xmlString) {
		return new XmlParserHelper(xmlString);
	}

	public String extractXmlStringNamed(final String parameter) {
		String value = null;

		String openTag = "<" + parameter + ">";
		String closeTag = "</" + parameter + ">";
		try {
			value = StringUtils.substringBetween(this.getXmlString(), openTag, closeTag);
		} catch (Exception e) {
			log.error("Error occurred while extracting {}", e.getMessage());
		}		

		return value;
	}
	public String extractInfoStringNamed(final String parameter) {
		String value = null;

		String openTag = parameter+"/";
		String closeTag = "/";
		try {
			value = StringUtils.substringBetween(this.getXmlString(), openTag, closeTag);
		} catch (Exception e) {
			log.error("Error occurred while extracting {}", e.getMessage());
		}		

		return value;
	}

	public Long extractXmlLongNamed(final String parameter) {
		Long longValue = null;
		try {
			String value = this.extractXmlStringNamed(parameter);
			longValue = Long.valueOf(value);
		} catch (Exception e) {
			log.info("Error occurred while converting string {}", e.getMessage());
		}		

		return longValue;
	}

	public Integer extractXmlIntegerNamed(final String parameter) {
		String value = null;
		Integer integerValue = null;
		try {
			 value = this.extractXmlStringNamed(parameter);
			  integerValue = Integer.valueOf(value);
		} catch (Exception e) {
			log.info("Error occurred while converting  string {}", e.getMessage());
		}		

		return integerValue;
	}

	public String getXmlParameter(String parameter) {
		return "<" + parameter + "></" + parameter + ">";
	}

	public boolean parameterExists(String parameter) {
		String openTag = "<" + parameter + ">";
		String closeTag = "</" + parameter + ">";
		try {
			if (StringUtils.contains(this.getXmlString(), openTag) && StringUtils.contains(closeTag, this.getXmlString())) {
				return true;
			}
		} catch (Exception e) {
			log.error("Error occurred while extracting {}", e.getMessage());
		}
		
		return false;
	}

	public String getXmlString() {
		return this.xmlString;
	}
	
	public List<String> getDataArray(String xmlString, String parameter){
		
		List<String> response = new ArrayList<String>();
		String[] arrays = xmlString.split("<" + parameter + ">");
		
		for(String array : arrays) {
			response.add(array);
		}
		
		return response;
	}
}
