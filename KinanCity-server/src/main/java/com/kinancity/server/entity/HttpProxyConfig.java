package com.kinancity.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class HttpProxyConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// Should it be used
	private boolean active;
	
	// HttpProxy or NoProxy
	private String type;

	// DNS or IP address
	private String host;

	// Port
	private Integer port;
	
	// Proxy auth Login
	private String login;

	// Proxy auth password
	private String pass;
	
	// Serialized Policy
	private String policy;
}
