package com.project.group.service;


import com.project.group.vo.Result;
import com.project.group.vo.params.AskParam;
import com.project.group.vo.params.Authority;
import com.project.group.vo.params.JoinProjectParams;
import org.springframework.stereotype.Service;

public interface MemberService {

    /**
     * 进入成员页面
     * @param projectId
     * @return
     */
    Result memberPage(Integer projectId);


    /**
     * 提高用户的权限
     * @param authority
     * @return
     */
    Result memberUpAuthority(Authority authority);



    /**
     * 降低用户的权限
     * @param authority
     * @return
     */
    Result memberLowAuthority(Authority authority);


    /**
     * 确认同意请求
     * @param askParam
     * @return
     */
    Result agreeThisPost(AskParam askParam);

    /**
     * 保存这一次的请求信息到member中
     * @param requestId
     */

    void save(Integer requestId);


    /**
     * 移除这个组员
     * @param memberid
     * @return
     */

    Result removeMemberById(Integer memberid);


    /**
     * 加入这个项目
     * @param joinProjectParams
     * @return
     */
    Result joinThisProject(JoinProjectParams joinProjectParams);
}
