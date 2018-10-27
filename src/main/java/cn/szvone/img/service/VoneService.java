package cn.szvone.img.service;

import cn.szvone.img.dto.ApiRes;
import cn.szvone.img.dto.CommonRes;
import cn.szvone.img.entity.VoneConfig;
import cn.szvone.img.util.ApiResultUtil;
import cn.szvone.img.util.ResultUtil;
import cn.szvone.img.util.SougouApi;
import cn.szvone.img.util.VoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;


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
        save(voneConfig);

        return ResultUtil.success();
    }


    private VoneConfig getVoneConfig(){
        if (voneConfig == null){
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            String myPath = "";
            String[] tmp = path.split("/");
            for (int i = 1;i<tmp.length;i++) {
                if (tmp[i].indexOf(".war")<0){
                    myPath+=tmp[i]+"/";
                }else {
                    break;
                }
            }
            //System.out.println(myPath);
            File file = new File(myPath+"vone.txt");
            String myIni = VoneUtil.txt2String(file);
            voneConfig = new VoneConfig();

            if (myIni!=null && !myIni.equals("")){

                voneConfig.setAdmin(VoneUtil.getSubString(myIni,"Admin=[","]"));
                voneConfig.setSinaUser(VoneUtil.getSubString(myIni,"SinaUser=[","]"));
                voneConfig.setSinaPass(VoneUtil.getSubString(myIni,"SinaPass=[","]"));
                voneConfig.setKey(VoneUtil.getSubString(myIni,"Key=[","]"));
                try {
                    voneConfig.setType(Integer.valueOf(VoneUtil.getSubString(myIni,"Type=[","]")));
                }catch (Exception e){
                    voneConfig.setType(1);
                }
                voneConfig.setSinaUpdateTime(VoneUtil.getSubString(myIni,"SinaUpdateTime=[","]"));
            }else{
                voneConfig.setAdmin("123456");
                voneConfig.setSinaUser("");
                voneConfig.setSinaPass("");
                voneConfig.setKey("123456");
                voneConfig.setType(1);
                voneConfig.setSinaUpdateTime("");
                save(voneConfig);
            }


        }

        return voneConfig;
    }

    public void save(VoneConfig voneConfig){
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String myPath = "";
        String[] tmp = path.split("/");
        for (int i = 1;i<tmp.length;i++) {
            if (tmp[i].indexOf(".war")<0){
                myPath+=tmp[i]+"/";
            }else {
                break;
            }
        }
        myPath+="vone.txt";

        String myIni = "Admin=["+voneConfig.getAdmin()+"]\r\n";
        myIni+="SinaUser=["+voneConfig.getSinaUser()+"]\r\n";
        myIni+="SinaPass=["+voneConfig.getSinaPass()+"]\r\n";
        myIni+="Key=["+voneConfig.getKey()+"]\r\n";
        myIni+="Type=["+voneConfig.getType()+"]\r\n";
        myIni+="SinaUpdateTime=["+voneConfig.getSinaUpdateTime()+"]\r\n";
        VoneUtil.contentToTxt(myPath,myIni);
    }
}
