package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // so inclua na representação Json se o valor da propriedade não estiver nulo.
public class Problem {

	private Integer status;
	private OffsetDateTime timestamp;
	private String type;
	private String title;
	private String detail;
	private String userMensage;

	private List<Object> objects;

	public Problem() {

	}

	public Problem(Integer status, OffsetDateTime timestamp, String type, String title, String detail,
			String userMensage, List<Object> objects) {
		this.status = status;
		this.timestamp = timestamp;
		this.type = type;
		this.title = title;
		this.detail = detail;
		this.userMensage = userMensage;
		this.objects = objects;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getUserMensage() {
		return userMensage;
	}

	public void setUserMensage(String userMensage) {
		this.userMensage = userMensage;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

	@Override
	public String toString() {
		return "Problem [status=" + status + ", timestamp=" + timestamp + ", type=" + type + ", title=" + title
				+ ", detail=" + detail + ", userMensage=" + userMensage + ", objects=" + objects + "]";
	}

	public static class Object {

		private String name;
		private String userMenssage;

		public Object() {

		}

		public Object(String name, String userMenssage) {
			this.name = name;
			this.userMenssage = userMenssage;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUserMenssage() {
			return userMenssage;
		}

		public void setUserMenssage(String userMenssage) {
			this.userMenssage = userMenssage;
		}

		@Override
		public String toString() {
			return "Object [name=" + name + ", userMenssage=" + userMenssage + "]";
		}

	}

}
