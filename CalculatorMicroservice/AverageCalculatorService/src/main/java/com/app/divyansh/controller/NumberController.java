package com.app.divyansh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.divyansh.model.ResponsePayload;
import com.app.divyansh.service.NumberService;

@RestController
@RequestMapping("/numbers")
public class NumberController {

    @Autowired
    private NumberService numberService;

    @GetMapping("/{numberid}")
    public ResponsePayload getNumbers(@PathVariable String numberid) {
        return numberService.getNumbers(numberid);
    }
}