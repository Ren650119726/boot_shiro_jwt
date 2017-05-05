package dmiaes.app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dmiaes.app.base.ResponseBase;
import dmiaes.app.config.jwt.JwtInfo;
  
@RestController  
@RequestMapping("/jwt")  
public class JwtInfoController {  
  
    @Autowired  
    private JwtInfo jwtInfo;  
  
    @RequestMapping(value = "/info", method = RequestMethod.GET)  
    public Object getJwtInfo() {  
    	ResponseBase<JwtInfo> responseBase = new ResponseBase<JwtInfo>();
    	responseBase.setCode(200);
    	responseBase.setData(jwtInfo);
        return responseBase;  
    }  
}  