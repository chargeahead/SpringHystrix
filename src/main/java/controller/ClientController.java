package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ClientController {
	
	@Autowired
	private RestTemplateBuilder tmplt;
	
	@Autowired
	private EurekaClient clnt;
	
	@RequestMapping("/")
	@HystrixCommand(fallbackMethod = "fallbackGreeting")
	public String callServiceClass() {
		RestTemplate rst = tmplt.build();
		InstanceInfo inst = clnt.getNextServerFromEureka("eurekaservice", false);
		String url = inst.getHomePageUrl();
		ResponseEntity<String> rsp = rst.exchange(url, HttpMethod.GET, null, String.class);
		return rsp.getBody();
	}
	
	public String fallbackGreeting() {
		System.out.println("Response from fallback method");
		return "Hello from fallback method";
	}

}
