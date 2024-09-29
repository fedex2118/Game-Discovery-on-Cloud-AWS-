package com.reviews.reviews_api.custom.response;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonView;

public class CollectionResponse<T> {

    @JsonView(Object.class)
    private Collection<T> content = new ArrayList<>();
    private Collection<Message> messages = new ArrayList<>();
    private PaginationDetails paginationDetails;

    public CollectionResponse() {
        super();
    }
    
    public CollectionResponse(T element) {
        if(element != null) {
        	this.content.add(element);
        }
    }

    public CollectionResponse(Page<T> page) {
        this.content = page.getContent();
        paginationDetails = new PaginationDetails();
        paginationDetails.setTotalElements(page.getTotalElements());
        paginationDetails.setTotalPages(page.getTotalPages());
        paginationDetails.setCurrentPage(page.getNumber());
    }
    
    public CollectionResponse(Collection<T> content) {
        super();
        this.content = content;
    }

    public CollectionResponse(Collection<T> content, Collection<Message> messages) {
        super();
        this.content = content;
        this.messages = messages;
    }

    public Collection<T> getContent() {
        return content;
    }

    public void setContent(Collection<T> content) {
        this.content = content;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

	public PaginationDetails getPaginationDetails() {
		return paginationDetails;
	}

	public void setPaginationDetails(PaginationDetails paginationDetails) {
		this.paginationDetails = paginationDetails;
	}
}

