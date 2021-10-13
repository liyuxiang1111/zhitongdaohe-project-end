package com.heyongqiang.TestDao;

import com.project.blog.dao.mapper.TagMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {BlogApp.class})
public class testMapper {
    @Resource
    private TagMapper tagMapper;
    @Test
    public void testTagServiceImpl(){
        String slot = "123456heyongqiang!@#$$";
        String pwd = DigestUtils.md5Hex("admin"+slot);
        System.out.println(pwd);
    }

    @Test
    public void test(){
        divide(10,3);
    }

    public int divide(int dividend, int divisor) {
        int right = dividend;
        int left = 1;
        if(divisor > dividend){
            return 0;
        }
        while(left < right){
            int mid = left + ((right - left)>>1);
            if(foreach(mid,divisor) > dividend){
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    public int foreach(int mid , int left ){
        int ans = 0;
        for(int i = 0; i < mid ; i ++){
            ans += left;
        }
        return ans;
    }
}
