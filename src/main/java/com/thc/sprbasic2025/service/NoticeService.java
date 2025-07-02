package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.NoticeDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoticeService {
    DefaultDto.CreateResDto create(NoticeDto.CreateReqDto param);
    void update(NoticeDto.UpdateReqDto param);
    void delete(DefaultDto.DeleteReqDto param);
    NoticeDto.DetailResDto detail(DefaultDto.DetailReqDto param);
    List<NoticeDto.DetailResDto> list(NoticeDto.ListReqDto param);
    DefaultDto.PagedListResDto pagedList(NoticeDto.PagedListReqDto param);
    List<NoticeDto.DetailResDto> scrollList(NoticeDto.ScrollListReqDto param);
}
