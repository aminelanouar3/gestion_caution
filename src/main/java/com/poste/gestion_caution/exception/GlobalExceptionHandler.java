package com.poste.gestion_caution.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStateTransitionException.class)
    public String handleInvalidState(InvalidStateTransitionException ex,
                                     RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/cautions/gestion";
    }
    @ExceptionHandler(InvalidEditTransitionException.class)
    public String handleInvalidEditTransition(InvalidEditTransitionException ex,
                                              RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/cautions/admin/edit/" + ex.getCautionId();
    }
}