package com.ankhang.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ankhang.entities.Employee;
import com.ankhang.feigncclient.InfoClient;
import com.ankhang.model.EmployeeModel;
import com.ankhang.model.InfoModel;
import com.ankhang.repository.EmployeeRepository;


@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ModelMapper mapper;
	
	//ưu tiên dùng webclient hơn restTemplate
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private InfoClient infoClient;
	
	// case dung restTemplate => webClient
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
//	@Value("${info.url}")
//	private String addressURL;
	
	
	//chỗ này rút gọn cho code đẹp hơn // case dung restTemplate => webClient
//	public EmployeeService(@Value("${info.url}") String addressURL, RestTemplateBuilder builder) {
//		this.restTemplate = builder.rootUri(addressURL).build();
//	}
	
    public EmployeeModel getEmployeeById(Long id) {
    	Employee employee = employeeRepository.findEmpById(id);
    	
    	EmployeeModel employeeModel = mapper.map(employee, EmployeeModel.class);
    	
    	// case dung restTemplate => webClient
    	//InfoModel infoModel = restTemplate.getForObject("/infos/{id}", InfoModel.class, id);
    	
    	InfoModel infoModel = new InfoModel();
    	try {
    		// infoModel = webClient.get().uri("/infos/"+id).retrieve().bodyToMono(InfoModel.class).block();
    		// dung FeignClient
    		//infoModel = infoClient.getInfoDetail(id);
    		
    		//case dung restTemplate 02 
    		infoModel = getInfoModelByIdUsingRestTemplate(id);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	employeeModel.setInfoModel(infoModel);
    	return employeeModel;
    }
    
    public InfoModel getInfoModelByIdUsingRestTemplate(Long id) {
    	
    	//balance load
    	//getInstances("INFO-APP"); need to correct capitalize letter
//    	List<ServiceInstance> instances = discoveryClient.getInstances("INFO-APP");
//    	ServiceInstance serviceInstance = instances.get(0);
//    	
//    	String uri = serviceInstance.getUri().toString();
    	
    	//Use loadBalancerClient
//    	ServiceInstance serviceInstance = loadBalancerClient.choose("INFO-APP");
//    	String uri = serviceInstance.getUri().toString();
//    	
//    	String contextRoot = serviceInstance.getMetadata().get("configPath");
//    	String username = serviceInstance.getMetadata().get("userankhang");
//    	System.out.println("url>>>>>>:" + uri+contextRoot);
    	
    	//return restTemplate.getForObject(uri+"/info-app/infos/{id}", InfoModel.class, id);
    	
    	//Can use like this with anotation @LoadBalance in restTemplate config
    	//Need comment all code before
    	return restTemplate.getForObject("http://INFO-APP/info-app/infos/{id}", InfoModel.class, id);
    	
    	//return restTemplate.getForObject(uri+contextRoot+"/infos/{id}", InfoModel.class, id);
    }
    
    
}
