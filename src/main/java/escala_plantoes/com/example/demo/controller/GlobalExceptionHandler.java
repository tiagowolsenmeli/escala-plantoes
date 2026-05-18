package escala_plantoes.com.example.demo.controller;

import escala_plantoes.com.example.demo.domain.plantao.CargaHorariaExceededException;
import escala_plantoes.com.example.demo.domain.plantao.DuplicatePlantaoException;
import escala_plantoes.com.example.demo.domain.plantao.PlantaoNotFoundException;
import escala_plantoes.com.example.demo.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(String message) {}

    @ExceptionHandler(PlantaoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handlePlantaoNotFound(PlantaoNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePlantaoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleDuplicatePlantao(DuplicatePlantaoException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(CargaHorariaExceededException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleCargaHorariaExceeded(CargaHorariaExceededException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleBadRequest(BadRequestException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
