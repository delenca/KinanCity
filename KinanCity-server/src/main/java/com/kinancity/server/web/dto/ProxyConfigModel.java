package com.kinancity.server.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kinancity.server.entity.HttpProxyConfig;

import lombok.Data;

@Data
public class ProxyConfigModel {

	private long id;

	// Should it be used
	private boolean active;

	// HttpProxy or NoProxy
	private String type;

	// DNS or IP address
	@JsonInclude(Include.NON_NULL)
	private String host;

	// Port
	@JsonInclude(Include.NON_NULL)
	private Integer port;

	// Proxy auth Login
	@JsonInclude(Include.NON_NULL)
	private String login;

	// Proxy auth password
	@JsonInclude(Include.NON_NULL)
	private String pass;

	// Serialized Policy
	private String policy;

	public ProxyConfigModel(HttpProxyConfig config) {
		this.setId(config.getId());
		this.setActive(config.isActive());
		this.setType(config.getType());
		this.setHost(config.getHost());
		this.setPort(config.getPort());
		this.setLogin(config.getLogin());
		this.setPort(config.getPort());
		this.setPolicy(config.getPolicy());
	}

}
