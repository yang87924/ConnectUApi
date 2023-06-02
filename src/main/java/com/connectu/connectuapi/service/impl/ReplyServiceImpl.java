package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.dao.impl.Reply;
import com.connectu.connectuapi.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyDao, Reply> implements IReplyService {
    @Autowired
    private ReplyDao replyDao;


}
