package cn.szvone.img.service;

import cn.szvone.img.dto.ApiRes;
import cn.szvone.img.dto.CommonRes;
import cn.szvone.img.entity.VoneConfig;
import cn.szvone.img.util.ApiResultUtil;
import cn.szvone.img.util.ResultUtil;
import cn.szvone.img.util.SougouApi;
import org.springframework.stereotype.Service;



@Service
public class VoneService {

    private VoneConfig voneConfig = null;



    //上传
    public ApiRes doUpload(String key, String imgBase64){
        if (voneConfig == null){
            getVoneConfig();
        }


        if (!key.equals(voneConfig.getKey())){
            return ApiResultUtil.error("通讯密钥错误");
        }


        if (voneConfig.getType() == 1){
            String res = SougouApi.uploadImg(imgBase64);
            if (res.indexOf("http")>=0){
                return ApiResultUtil.success(res);
            }else{
                return ApiResultUtil.error("上传失败");
            }
        }


        return ApiResultUtil.error("类型错误");
    }



    //管理员登录
    public CommonRes login(String pass){
        if (voneConfig == null){
            getVoneConfig();
        }

        if (!pass.equals(voneConfig.getAdmin())){
            return ResultUtil.error(-1,"管理员密码错误！");
        }
        return ResultUtil.success();
    }

    //拉取配置
    public CommonRes getConfig(){
        if (voneConfig == null){
            getVoneConfig();
        }

        return ResultUtil.success(voneConfig);
    }

    //设置配置
    public CommonRes setConfig(VoneConfig newConfig){

        if (voneConfig == null){
            getVoneConfig();
        }

        voneConfig = newConfig;


        return ResultUtil.success();
    }


    private VoneConfig getVoneConfig(){
        if (voneConfig == null){
            voneConfig = new VoneConfig();
            voneConfig.setAdmin("123456");
            voneConfig.setSinaUser("");
            voneConfig.setSinaPass("");
            voneConfig.setKey("123456");
            voneConfig.setType(1);
            voneConfig.setSinaUpdateTime("");
        }

        return voneConfig;
    }
}
