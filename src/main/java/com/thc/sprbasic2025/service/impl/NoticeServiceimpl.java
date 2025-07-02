package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Notice;
import com.thc.sprbasic2025.dto.NoticeDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.mapper.NoticeMapper;
import com.thc.sprbasic2025.repository.NoticeRepository;
import com.thc.sprbasic2025.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeServiceimpl implements NoticeService {

    final NoticeRepository boardRepository;
    final NoticeMapper boardMapper;

    @Override
    public DefaultDto.CreateResDto create(NoticeDto.CreateReqDto param) {
        DefaultDto.CreateResDto res = boardRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    @Override
    public void update(NoticeDto.UpdateReqDto param) {
        Notice board = boardRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException("no data"));
        if(param.getDeleted() != null){ board.setDeleted(param.getDeleted()); }
        if(param.getTitle() != null){ board.setTitle(param.getTitle()); }
        if(param.getContent() != null){ board.setContent(param.getContent()); }
        boardRepository.save(board);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param) {
        update(NoticeDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build());
    }

    @Override
    public NoticeDto.DetailResDto detail(DefaultDto.DetailReqDto param) {
        NoticeDto.DetailResDto res = boardMapper.detail(param.getId());
        return res;
    }

    @Override
    public List<NoticeDto.DetailResDto> list(NoticeDto.ListReqDto param) {
        return detailList(boardMapper.list(param));
    }
    public List<NoticeDto.DetailResDto> detailList(List<NoticeDto.DetailResDto> list){
        List<NoticeDto.DetailResDto> newList = new ArrayList<>();
        for(NoticeDto.DetailResDto each : list){
            newList.add(detail(DefaultDto.DetailReqDto.builder().id(each.getId()).build()));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(NoticeDto.PagedListReqDto param) {
        DefaultDto.PagedListResDto res = param.init(boardMapper.pagedListCount(param));
        res.setList(detailList(boardMapper.pagedList(param)));
        return res;
    }

    @Override
    public List<NoticeDto.DetailResDto> scrollList(NoticeDto.ScrollListReqDto param) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        if("title".equals(param.getOrderby())){
            String mark = param.getMark();
            if(mark != null && !mark.isEmpty()){
                NoticeDto.DetailResDto board = boardMapper.detail(Long.parseLong(mark));
                if(board != null){
                    mark = board.getTitle() + "_" + board.getId();
                    param.setMark(mark);
                }
            }
        }

        return detailList(boardMapper.scrollList(param));
    }


}
