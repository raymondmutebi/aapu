package org.pahappa.systems.core.utils.sendgrid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Mail {

	/** The email's from field. */
	@JsonProperty("from")
	public Email from;

	/**
	 * The email's subject line. This is the global, or message level, subject of
	 * your email. This may be overridden by personalizations[x].subject.
	 */
	@JsonProperty("subject")
	public String subject;

	/**
	 * The email's personalization. Each object within personalizations can be
	 * thought of as an envelope - it defines who should receive an individual
	 * message and how that message should be handled.
	 */
	@JsonProperty("personalizations")
	public List<Personalization> personalization;

	/** The email's content. */
	@JsonProperty("content")
	public List<Content> content;

	/** The email's reply to address. */
	@JsonProperty("reply_to")
	public Email replyTo;

	private static final ObjectMapper SORTED_MAPPER = new ObjectMapper();

	static {
		SORTED_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
	}

	private <T> List<T> addToList(T element, List<T> defaultList) {
		if (defaultList != null) {
			defaultList.add(element);
			return defaultList;
		} else {
			List<T> list = new ArrayList<T>();
			list.add(element);
			return list;
		}
	}

	/** Construct a new Mail object. */
	public Mail() {
		return;
	}

	/**
	 * Construct a new Mail object.
	 *
	 * @param from    the email's from address.
	 * @param subject the email's subject line.
	 * @param to      the email's recipient.
	 * @param content the email's content.
	 */
	public Mail(Email from, String subject, Email to, Content content) {
		this.setFrom(from);
		this.setSubject(subject);
		Personalization personalization = new Personalization();
		personalization.addTo(to);
		this.addPersonalization(personalization);
		this.addContent(content);
	}

	/**
	 * Get the email's from address.
	 *
	 * @return the email's from address.
	 */
	@JsonProperty("from")
	public Email getFrom() {
		return this.from;
	}

	/**
	 * Set the email's from address.
	 *
	 * @param from the email's from address.
	 */
	public void setFrom(Email from) {
		this.from = from;
	}

	/**
	 * Get the email's subject line.
	 *
	 * @return the email's subject line.
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * Set the email's subject line.
	 *
	 * @param subject the email's subject line.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Get the email's personalizations. Content added to the returned list will be
	 * included when sent.
	 *
	 * @return the email's personalizations.
	 */
	@JsonProperty("personalizations")
	public List<Personalization> getPersonalization() {
		return personalization;
	}

	/**
	 * Add a personalization to the email.
	 *
	 * @param personalization a personalization.
	 */
	public void addPersonalization(Personalization personalization) {
		this.personalization = addToList(personalization, this.personalization);
	}

	/**
	 * Get the email's content. Content added to the returned list will be included
	 * when sent.
	 *
	 * @return the email's content.
	 */
	@JsonProperty("content")
	public List<Content> getContent() {
		return content;
	}

	/**
	 * Add content to this email.
	 *
	 * @param content content to add to this email.
	 */
	public void addContent(Content content) {
		Content newContent = new Content();
		newContent.setType(content.getType());
		newContent.setValue(content.getValue());
		this.content = addToList(newContent, this.content);
	}

	/**
	 * Get the email's reply to address.
	 *
	 * @return the reply to address.
	 */
	@JsonProperty("reply_to")
	public Email getReplyto() {
		return replyTo;
	}

	/**
	 * Set the email's reply to address.
	 *
	 * @param replyTo the reply to address.
	 */
	public void setReplyTo(Email replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * Create a string representation of the Mail object JSON.
	 *
	 * @return a JSON string.
	 * @throws IOException in case of a JSON marshal error.
	 */
	public String build() throws IOException {
		try {
			return SORTED_MAPPER.writeValueAsString(this);
		} catch (IOException ex) {
			throw ex;
		}
	}

	/**
	 * Create a string representation of the Mail object JSON and pretty print it.
	 *
	 * @return a pretty JSON string.
	 * @throws IOException in case of a JSON marshal error.
	 */
	public String buildPretty() throws IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (IOException ex) {
			throw ex;
		}
	}
}
