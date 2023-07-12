package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Hashtag;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserThreadDTO;
import com.github.yulichang.base.MPJBaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface IThreadService extends MPJBaseService<Thread> {
    //假資料--------------------------------------------------------------
    void addFakeThread(int count);

    //新增收藏文章--------------------------------------------------------------
    boolean addFavoriteThread(Integer userId, Integer threadId);

    //切換使用者按讚--------------------------------------------------------------
    void toggleLove(Thread thread);

    //按讚--------------------------------------------------------------
    void love(Thread thread);

    //取消按讚--------------------------------------------------------------
    void cancelLove(Thread thread);

    //查詢使用者收藏的文章
    List<Thread> getFavoriteThreads(Integer userId);

    //查詢使用者的所有文章--------------------------------------------------------------
    List<Thread> getUserThread(Integer userId);
    Page<Thread> getThreadByPage(Page<Thread> page);
    List<List<Thread>> getUserThreadForUser(List<Integer> userIds);
    //熱門文章--------------------------------------------------------------
    List<Thread> hotThread();

    //查詢主題文章--------------------------------------------------------------
    List<Thread> getCategoryThread(int categoryId);

    //查詢單筆論壇文章-------------------------------------------------------------
    Thread getThreadWithCategoryName(Integer threadId);

    //查詢最後一筆論壇文章--------------------------------------------------------------
    Integer getLastThreadById();
    Thread getLastThreadWithDetails();
    //關鍵字搜尋--------------------------------------------------------------
    Result searchThreads(String keyword);
    List<Thread> searchThreadsByKeyword(String keyword);
    void saveThreadHashtags(Thread thread);
    //void handleHashtags(Thread thread, List<String> hashtags);
    void handleHashtags(String threadHashtags, Thread thread) ;
    //分頁查詢--------------------------------------------------------------
    IPage<Thread> listWithPagination(Page<Thread> page, Wrapper<Thread> queryWrapper);
    List<Thread> getThreadById(Integer threadId);
    void processHashtags(String hashtags, Thread thread);
    boolean deleteByThreadId(Integer threadId);
}
