package com.ankhang.service;

import java.util.List;
import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ankhang.entities.Employee;
import com.ankhang.feigncclient.InfoClient;
import com.ankhang.model.EmployeeModel;
import com.ankhang.model.InfoModel;
import com.ankhang.model.InfoModelTopicKafka;
import com.ankhang.repository.EmployeeRepository;
import com.netflix.discovery.BackupRegistry;
import org.springframework.transaction.annotation.Transactional;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.ipc.http.HttpSender.Method;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
	
	@Autowired
	private ObservationRegistry observationRegistry;
	
    @Autowired
    private Tracer tracer;
    
    @Autowired
    private  ApplicationEventPublisher applicationEventPublisher;
    
    
	
//	@Value("${info.url}")
//	private String addressURL;
	
	
	//chỗ này rút gọn cho code đẹp hơn // case dung restTemplate => webClient
//	public EmployeeService(@Value("${info.url}") String addressURL, RestTemplateBuilder builder) {
//		this.restTemplate = builder.rootUri(addressURL).build();
//	}
	
	public EmployeeModel getEmployeeById_NoCallServer(Long id) {
		Employee employee = employeeRepository.findEmpById(id);
		EmployeeModel employeeModel = mapper.map(employee, EmployeeModel.class);
		InfoModel info = new InfoModel();
		info.setInfoAge("SERVER NOT RESPONE");
		info.setInfoPhone("SERVER NOT RESPONE");
		employeeModel.setInfoModel(info);
		return employeeModel;
	}
	
	/* Backup getEmployeeById Before Add Trace ID Start */
	/*
    public EmployeeModel getEmployeeById(Long id) {
    	Employee employee = employeeRepository.findEmpById(id);
    	
    	EmployeeModel employeeModel = mapper.map(employee, EmployeeModel.class);
    	
    	// case dung restTemplate => webClient
    	//InfoModel infoModel = restTemplate.getForObject("/infos/{id}", InfoModel.class, id);
    	
    	InfoModel infoModel = new InfoModel();
    	// comment try catch to use @CircuitBreaker
//    	try {
    		//case dung webClient
    		//infoModel = webClient.get().uri("/infos/"+id).retrieve().bodyToMono(InfoModel.class).block();
    		
    		// dung FeignClient
    		//infoModel = infoClient.getInfoDetail(id);
    		
    		//case dung restTemplate 02 
    	     //infoModel = getInfoModelByIdUsingRestTemplate(id);
    	
    		infoModel = infoClient.getInfoDetail(id);
//    	} catch (Exception e) {
//			e.printStackTrace();
//		}
    	
    	employeeModel.setInfoModel(infoModel);
    	return employeeModel;
    }
    */
    /* Backup getEmployeeById Before Add Trace ID End */
	
	/* Method getEmployeeById not Set span name Start */
//	public EmployeeModel getEmployeeById(Long id) {
//	    Observation inventoryServiceObservation = Observation.createNotStarted("ankhang_stanid_call_infoservice",
//	            this.observationRegistry);
//	    inventoryServiceObservation.lowCardinalityKeyValue("call_to_info-app", "info-app");
//	    inventoryServiceObservation.start();
//	   
//	    try {
//	        Employee employee = employeeRepository.findEmpById(id);
//	        EmployeeModel employeeModel = mapper.map(employee, EmployeeModel.class);
//	        InfoModel infoModel = infoClient.getInfoDetail(id);
//	        employeeModel.setInfoModel(infoModel);
//	        return employeeModel;
//	    } finally {
//	        inventoryServiceObservation.stop();
//	    }
//	}
	/* Method getEmployeeById not Set span name End */
    public EmployeeModel getEmployeeById(Long id, String token) {
    	System.out.println("---Method Calling Info---");
    	//The purposes when use Span to trace a specific unit of work across MULTIPLE SERVICES
    	//The purposes when use Observation to record information about the behavior of a SERVICE
    	//Set span name
        Span span = tracer.nextSpan().name("ankhang_service_client").start();
        try {
            // Set the Trace ID on the current span
            span.tag("Trace ID", span.context().traceIdString());
            
            Observation inventoryServiceObservation = Observation.createNotStarted("ankhang_stanid_call_infoservice",
                    this.observationRegistry);
            inventoryServiceObservation.lowCardinalityKeyValue("call_to_info-app", "info-app");
            inventoryServiceObservation.start();
            try {
                Employee employee = employeeRepository.findEmpById(id);
                EmployeeModel employeeModel = mapper.map(employee, EmployeeModel.class);
                InfoModel infoModel = infoClient.getInfoDetail(id,token);
                employeeModel.setInfoModel(infoModel);
				/* Kafka_Add_Start */
                applicationEventPublisher.publishEvent(new InfoModelTopicKafka(this, employeeModel.getEmpId(), infoModel.getInfoPhone(),"Send this notification to NotificationServer"));
                /* Kafka_Add_End */
                return employeeModel;
            } finally {
                inventoryServiceObservation.stop();
            }
        }
		/* Add another Observation calObservation to other service Start */
//        Observation inventoryServiceObservation_02 = Observation.createNotStarted("ankhang_stanid_call_infoservice_02",
//                this.observationRegistry);
//        inventoryServiceObservation.lowCardinalityKeyValue
//        inventoryServiceObservation.start();
//        try {
//        	
//        }
//        finally {
//            inventoryServiceObservation.stop();
//        }
        /* Add another Observation calObservation to other service End */
        finally {
            span.finish();
        }
        //Can see that Span will handle all of the method process Multiple Service, Observation just handle in ONE Service
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
