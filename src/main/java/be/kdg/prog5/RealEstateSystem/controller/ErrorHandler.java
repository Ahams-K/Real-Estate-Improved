package be.kdg.prog5.RealEstateSystem.controller;

import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.webapi.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFound(final NotFoundException e, final HttpServletRequest request) {
        final boolean isApi = request.getRequestURI().startsWith("/api");
        if (isApi) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
        }
        final ModelAndView view = new ModelAndView("error-page");
        view.addObject("error", e.getMessage());
        return view;
    }
}
