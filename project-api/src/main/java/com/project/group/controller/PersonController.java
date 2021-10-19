package com.project.group.controller;

import com.project.group.service.PersonService;
import com.project.group.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping
public class PersonController {


    @Resource
    private PersonService personService;



    @PostMapping("person")
    public Result intoPersonCenter(){
        return personService.intoPersonCenter();
    }
}
