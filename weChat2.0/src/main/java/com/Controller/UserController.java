package com.Controller;

import com.Service.UserService;
import com.Utils.Constant;
import com.Utils.TokenUtil;
import com.VO.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@CrossOrigin
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/set/avatar",produces = "application/json;charset=UTF-8")
    public Result setAvatar(HttpServletRequest request,@RequestBody MultipartFile file){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int singin = TokenUtil.getSignin(token);
        return userService.setAvatar(openId,file,singin);
    }

    @PostMapping(value = "/user/set/nickName")
    public Result setNickName(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        String nickName = map.get("nickName");
        int singin = TokenUtil.getSignin(token);
        return userService.setNickName(openId,nickName,singin);
    }

    @PostMapping(value = "/user/set/gender")
    public Result setGender(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int gender = 0;
        try {
            gender = Integer.parseInt(map.get("gender"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        int singin = TokenUtil.getSignin(token);
        return userService.setGender(openId,gender,singin);
    }

    @PostMapping(value = "/user/set/campus")
    public Result setCampus(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int campus = 0;
        try {
            campus = Integer.parseInt(map.get("campus"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        int singin = TokenUtil.getSignin(token);
        return userService.setCampus(openId,campus,singin);
    }

    @PostMapping(value = "/user/apply/manager")
    public Result applyManager(HttpServletRequest request){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        return userService.insertManger(openId);
    }

    @PostMapping(value = "/user/get/applyer")
    public Result getApplyer(HttpServletRequest request){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.getApplyer();
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/get/manager")
    public Result getManager(HttpServletRequest request){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.getManager();
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/set/manager")
    public Result setManager(HttpServletRequest request, @RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.setManager(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/del/manager")
    public Result delManager(HttpServletRequest request, @RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.delManager(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/get/userPage")
    public Result getUserPage(@RequestBody Map<String,String> map){
        return userService.getUserPage(map.get("openId"));
    }

    @PostMapping(value = "/user/inform/user")
    public Result informUser(HttpServletRequest request,@RequestBody Map<String,String> map){
        String openId = map.get("openId");
        String token = request.getHeader("token");
        String id = TokenUtil.getOpenId(token);
        if(id.equals(openId)){
            return null;
        }
        String content = map.get("content");
        String img = map.get("imgBase64");
        return userService.informUser(openId,content,img);
    }

    @PostMapping(value = "/user/get/informedUser")
    public Result getInformedUser(HttpServletRequest request){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.getInformedUser();
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/get/informReason")
    public Result getInformReason(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.getInformReason(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/del/informedUser")
    public Result delInformedUser(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.delInformedUser(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/lock/informedUser")
    public Result lockUser(HttpServletRequest request,@RequestBody Map<String,String> map) {
        String token = request.getHeader("token");
        String id = TokenUtil.getOpenId(token);
        if(id.equals(map.get("openId"))){
            return null;
        }
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.lockUser(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/get/lockedUser")
    public Result getLockedUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.getLockedUser();
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }

    @PostMapping(value = "/user/unlock/blackUser")
    public Result unlockUser(HttpServletRequest request,@RequestBody Map<String,String> map) {
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.ADMIN_AUTH){
            return userService.unlockUser(map.get("openId"));
        }
        Result res = new Result();
        res.setCode(Constant.NO_PERMISSION);
        res.setMessage("您没有权限！");
        return res;
    }


    @PostMapping(value = "/user/set/signature")
    public Result setSignature(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        String signature = map.get("signature");
        int singin = TokenUtil.getSignin(token);
        return userService.setSignature(openId,signature,singin);
    }

}
