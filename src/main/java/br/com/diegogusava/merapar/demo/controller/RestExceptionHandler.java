package br.com.diegogusava.merapar.demo.controller;

import br.com.diegogusava.merapar.demo.postanalyzer.exception.FileUnavailableException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.InvalidXmlContentException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.ParserException;
import br.com.diegogusava.merapar.demo.postanalyzer.exception.XmlFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ParserException.class})
    public ResponseEntity analyzerExceptions() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({InvalidUrlException.class})
    public ResponseEntity<RestErrorResponse> invalidUrl() {
        return ResponseEntity.badRequest().body(new RestErrorResponse("Invalid URL"));
    }

    @ExceptionHandler(XmlFileNotFoundException.class)
    public ResponseEntity<RestErrorResponse> xmlFileNotFound() {
        return ResponseEntity.badRequest().body(new RestErrorResponse("File not found"));
    }

    @ExceptionHandler(FileUnavailableException.class)
    public ResponseEntity<RestErrorResponse> fileUnavailable() {
        return ResponseEntity.badRequest().body(new RestErrorResponse("File is unavailable."));
    }

    @ExceptionHandler(InvalidXmlContentException.class)
    public ResponseEntity<RestErrorResponse> invalidXmlContent(InvalidXmlContentException ex) {
        return ResponseEntity.badRequest().body(new RestErrorResponse(ex.getMessage()));
    }
}
