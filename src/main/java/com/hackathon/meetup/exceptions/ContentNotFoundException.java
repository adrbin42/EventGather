package com.hackathon.meetup.exceptions;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by David Turk on 8/10/17.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(String message) {
        super(message);
    }
}
