package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userDyThreadLove")
public class UserDyThreadLove {
    @TableId
    private Integer userDyThreadLoveId;
    private Integer userId;
    private Integer dyThreadId;
    private Integer loveStatus;
    @TableLogic(value = "1",delval = "0")
    private Boolean deleted;

    public void toggleLove() {
        this.loveStatus = this.loveStatus == 0 ? 1 : 0;
    }

}
