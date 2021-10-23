package com.project.group.controller;


import com.project.group.service.MemberService;
import com.project.group.vo.Result;
import com.project.group.vo.params.AskParam;
import com.project.group.vo.params.Authority;
import com.project.group.vo.params.JoinProjectParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RequestMapping("member")
@RestController
public class MemberController {

    @Resource
    private MemberService memberService;

    /**
     * 查看项目的所有组员
     * @param projectId
     * @return
     */
    @PostMapping("/{id}")
    public Result memberPage(@PathVariable("id") Integer projectId){
        return memberService.memberPage(projectId);
    }

    /**
     * 提高用户权限
     * @param authority
     * @return
     */

    @PostMapping("up")
    public Result upMemberAuthority(@RequestBody Authority authority){
        return memberService.memberUpAuthority(authority);
    }


    /**
     * 降低用户权限
     * @param authority
     * @return
     */

    @PostMapping("low")
    public Result lowMemberAuthority(@RequestBody Authority authority){
        return memberService.memberLowAuthority(authority);
    }

    @PostMapping("argee")
    public Result agreeThisPost(@RequestBody AskParam askParam){
        return memberService.agreeThisPost(askParam);
    }
    
    @PostMapping("remove")
    public Result removeMember(@RequestHeader Integer memberId){
        return memberService.removeMemberById(memberId);
    }

    /**
     * 加入这个项目
     * @param joinProjectParams
     * @return
     */
    @PostMapping("join")
    public Result joinThisProejct(@RequestBody JoinProjectParams joinProjectParams){
        return memberService.joinThisProject(joinProjectParams);
    }

}
