package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.service.IDyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
@Service
public class DyReplyServiceImpl extends ServiceImpl<DyReplyDao, DyReply> implements IDyReplyService {
    @Autowired
    private DyReplyDao dyReplyDao;

}
