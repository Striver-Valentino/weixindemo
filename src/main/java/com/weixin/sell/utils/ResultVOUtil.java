package com.weixin.sell.utils;

import com.weixin.sell.VO.ResultVO;

/**
 * 封装 返回 ResultVO 的所有 可能 情况
 */
public class ResultVOUtil {

    // 成功返回
    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");

        return resultVO;
    }

    // 没有传入 类目
    public static ResultVO success(){

        return success(null);
    }

    // 发生错误
    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);

        return resultVO;
    }

}
