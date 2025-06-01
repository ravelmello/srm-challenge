package com.ravel.teste.srm.utils;

import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Controller;

@Controller("retryExceptionEvaluator")
public class RetryExceptionEvaluator {
    public boolean isRetryableException(Throwable throwable) {
        return throwable instanceof TransientDataAccessException;
    }
}
