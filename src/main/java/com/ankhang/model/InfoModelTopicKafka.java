package com.ankhang.model;

import org.springframework.context.ApplicationEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoModelTopicKafka extends ApplicationEvent {

	private static final long serialVersionUID = -2361783978579033329L;
	private Long infoId;
	private String infoPhone;
	private String messageSend;
	
    @JsonIgnore // annotate the source property to ignore it during serialization
    @Override
    public Object getSource() {
        return super.getSource();
    }
	
    public InfoModelTopicKafka(Object source, Long infoId, String infoPhone, String messageSend) {
        super(source);
        this.infoId = infoId;
        this.infoPhone = infoPhone;
        this.messageSend = messageSend;
    }

    public InfoModelTopicKafka(Long infoId, String infoPhone, String messageSend) {
    	super(new Object());
        this.infoId = infoId;
        this.infoPhone = infoPhone;
        this.messageSend = messageSend;
    }
    
    
}
