/**
 * 
 */
package de.evoila.cf.broker.controller.utils;

import com.sun.javafx.fxml.builder.URLBuilder;
import de.evoila.cf.broker.controller.AuthenticationController;
import de.evoila.cf.broker.model.DashboardClient;
import de.evoila.cf.broker.model.ServiceDefinition;
import de.evoila.cf.broker.model.ServiceInstance;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

/**
 * @author Johannes Hiemer.
 *
 */
public class DashboardUtils {
	
	public static boolean hasDashboard(ServiceDefinition serviceDefinition) {
		return (serviceDefinition.getDashboard() != null
				&& serviceDefinition.getDashboard().getUrl() != null
				&& DashboardUtils.isURL(serviceDefinition.getDashboard().getUrl())
				&& serviceDefinition.getDashboardClient() != null);
	}
	
	public static String dashboard(ServiceDefinition serviceDefinition, String serviceInstanceId) {
		return DashboardUtils.appendSegmentToPath(serviceDefinition.getDashboard().getUrl(), serviceInstanceId);
	}

	public static String redirectUri(DashboardClient dashboardClient, String... appendixes) {
		String url = dashboardClient.getRedirectUri();
		for (String appendix : appendixes) {
			url = DashboardUtils.appendSegmentToPath(url, appendix);
		}

		return url;
	}
	
	public static boolean isURL(String url) {
	    try {
	        new URL(url);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private static String appendSegmentToPath(String path, String segment) {
		if (path == null || path.isEmpty()) 
			return "/" + segment;

		if (path.charAt(path.length() - 1) == '/') 
			return path + segment;

		return path + "/" + segment;
	}

	public static String ssoUrl (ServiceInstance serviceInstance, ApiLocationInfo info, String redirectUri) {
		if(serviceInstance != null && serviceInstance.getContext() != null && serviceInstance.getContext().containsKey(
			AuthenticationController.SSO_URL)) {

			String ssoUrl = serviceInstance.getContext().get(AuthenticationController.SSO_URL);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ssoUrl);
			builder.replaceQueryParam("state", RandomStringUtils.randomAlphabetic(32));
			builder.replaceQueryParam("nonce", RandomStringUtils.randomAlphabetic(32));
			builder.replaceQueryParam("redirect_uri", redirectUri);


			return builder.toUriString();
		}
		return null;

	}

	public static ApiLocationInfo getApiInfo (ServiceInstance serviceInstance) {
		if(serviceInstance != null && serviceInstance.getApiLocation() != null){
			RestTemplate template = new RestTemplate();
			return template.getForEntity(serviceInstance.getApiLocation(), ApiLocationInfo.class).getBody();
		}
		return null;
	}


}
