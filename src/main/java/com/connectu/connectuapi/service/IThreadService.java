package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
    Integer getLastThreadById();
    List<Thread> getUserThread(int id);
    Thread getThreadWithCategoryName(Integer threadId);
    boolean saveThread(Integer categoryId,Integer userId,String title, String content,  String picture) ;
    List<Thread> searchThreadsByKeyword(String keyword, String categoryName);
    void love(Thread thread);
    void cancelLove(Thread thread);
    Set<Integer> idSet(Integer num);
}
