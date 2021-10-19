package com.project.group.controller;


import com.project.group.service.MemberService;
import com.project.group.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RequestMapping("member")
@RestController
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/{projectId}")
    public Result memberPage(@PathParam("projectId") Integer projectId){
        return memberService.memberPage(projectId);
    }

}
