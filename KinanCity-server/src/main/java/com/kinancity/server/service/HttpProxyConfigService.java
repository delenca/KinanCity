package com.kinancity.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kinancity.core.proxy.HttpProxyProvider;
import com.kinancity.core.proxy.ProxyInfo;
import com.kinancity.core.proxy.ProxyManager;
import com.kinancity.core.proxy.impl.NoProxy;
import com.kinancity.core.proxy.policies.NintendoTimeLimitPolicy;
import com.kinancity.core.proxy.policies.ProxyPolicy;
import com.kinancity.server.entity.HttpProxyConfig;
import com.kinancity.server.entity.factory.HttpProxyConfigFactory;
import com.kinancity.server.entity.repository.HttpProxyConfigRepository;
import com.kinancity.server.errors.ConfigurationLoadingException;

@Service
public class HttpProxyConfigService implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProxyManager proxyManager;

	@Autowired
	private HttpProxyConfigRepository httpProxyConfigRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		List<HttpProxyConfig> proxies = httpProxyConfigRepository.findAll();
		logger.info("{} proxy configuration in DB", proxies.size());

		List<ProxyInfo> loadedProxies = new ArrayList<>();
		if (proxies.isEmpty()) {
			logger.info("Add direct connection by default");
			ProxyInfo directConnection = new ProxyInfo(new NintendoTimeLimitPolicy(), new NoProxy());
			HttpProxyConfig config = HttpProxyConfigFactory.getHttpProxyConfig(directConnection);
			config.setActive(true);
			httpProxyConfigRepository.save(config);
			loadedProxies.add(directConnection);
		} else {
			loadedProxies = StreamSupport.stream(proxies.spliterator(), false).map(proxy -> {
				try {
					return HttpProxyConfigFactory.toProxyInfo(proxy);
				} catch (ConfigurationLoadingException e) {
					logger.error("ConfigurationLoadingException {}", e.getMessage());
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());
		}

		proxyManager.getProxies().addAll(loadedProxies);
	}

}
