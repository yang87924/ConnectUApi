package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Thread;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IThreadService extends IService<Thread> {
    void addFakeThread(int count);
    boolean saveThread(Thread thread, Integer userId);
}
