package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
    Integer getLastThreadById();
    List<Thread> getUserThreadById(int id);
    Thread getThreadWithCategoryName(Integer threadId);

    //Boolean putThreadById(Thread thread,Integer getThreadId,Integer getUserId,String fileUrl);
//    Boolean putThreadById(Thread thread);
}
