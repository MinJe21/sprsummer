package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Board;
import com.thc.sprbasic2025.domain.Boardlike;
import com.thc.sprbasic2025.dto.BoardDto;
import com.thc.sprbasic2025.dto.BoardimgDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.mapper.BoardMapper;
import com.thc.sprbasic2025.repository.BoardRepository;
import com.thc.sprbasic2025.repository.BoardlikeRepository;
import com.thc.sprbasic2025.service.BoardService;
import com.thc.sprbasic2025.service.BoardimgService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardServiceimpl implements BoardService {

    /*
    R => mapper (mybatis)
    C U D => Repository
    * */

    final BoardRepository boardRepository;
    final BoardMapper boardMapper;
    final BoardimgService boardimgService;
    final BoardlikeRepository boardlikeRepository;
    public BoardServiceimpl(BoardRepository boardRepository
            , BoardMapper boardMapper
            , BoardimgService boardimgService
            , BoardlikeRepository boardlikeRepository
    ){
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.boardimgService = boardimgService;
        this.boardlikeRepository = boardlikeRepository;
    }

    @Override
    public DefaultDto.CreateResDto create(BoardDto.CreateReqDto param) {
        if(param.getUserId() == null){
            //로그인 한 상태에서만 쓰도록!
            throw new RuntimeException("not enough parameter");
        }

        DefaultDto.CreateResDto res = boardRepository.save(param.toEntity()).toCreateResDto();
        for(String each : param.getImgs()){
            boardimgService.create(BoardimgDto.CreateReqDto.builder().boardId(res.getId()).url(each).build());
        }
        return res;
    }

    @Override
    public void update(BoardDto.UpdateServDto param) {
        Board board = boardRepository.findById(param.getId()).orElse(null);
        if(board == null){
            throw new RuntimeException("no data");
        }

        Long reqUserId = param.getReqUserId();
        if(reqUserId == null){
            //로그인 한 상태에서만 쓰도록!
            throw new RuntimeException("not logged in");
        }
        if(!reqUserId.equals(board.getUserId())){
            // 본인 거 아닌데 왜 수정하려고 하지? 돌려보내자!
            throw new RuntimeException("not author matched");
        }


        if(param.getDeleted() != null){ board.setDeleted(param.getDeleted()); }
        if(param.getUserId() != null){ board.setUserId(param.getUserId()); }
        if(param.getTitle() != null){ board.setTitle(param.getTitle()); }
        if(param.getContent() != null){ board.setContent(param.getContent()); }
        /*if(param.getAuthor() != null){ board.setAuthor(param.getAuthor()); }*/
        boardRepository.save(board);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param) {
        update(BoardDto.UpdateServDto.builder().id(param.getId()).deleted(true).build());
    }

    @Override
    public BoardDto.DetailResDto detail(DefaultDto.DetailReqDto param) {
        /*
        Board board = boardRepository.findById(param.getId()).orElseThrow(() -> new RuntimeException(""));
        BoardDto.DetailResDto res = BoardDto.DetailResDto.builder()
                .id(param.getId())
                .deleted(board.getDeleted())
                .title(board.getTitle()).content(board.getContent()).author(board.getAuthor())
                .countread(board.getCountread())
                .build();
        return res;
        */
        BoardDto.DetailResDto res = boardMapper.detail(param.getId());
        res.setImgs(
            boardimgService.list(BoardimgDto.ListReqDto.builder().deleted(false).boardId(res.getId()).build())
        );

        Boardlike boardlike = boardlikeRepository.findByDeletedAndBoardIdAndUserId(false ,res.getId(), param.getUserId());
        res.setLiked(boardlike != null);
        return res;
    }

    @Override
    public List<BoardDto.DetailResDto> list(BoardDto.ListReqDto param) {
        return detailList(boardMapper.list(param));
    }
    public List<BoardDto.DetailResDto> detailList(List<BoardDto.DetailResDto> list){
        List<BoardDto.DetailResDto> newList = new ArrayList<>();
        for(BoardDto.DetailResDto each : list){
            newList.add(detail(DefaultDto.DetailReqDto.builder().id(each.getId()).build()));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(BoardDto.PagedListReqDto param) {
        DefaultDto.PagedListResDto res = param.init(boardMapper.pagedListCount(param));
        res.setList(detailList(boardMapper.pagedList(param)));
        return res;
    }

    @Override
    public List<BoardDto.DetailResDto> scrollList(BoardDto.ScrollListReqDto param) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        if("title".equals(param.getOrderby())){
            String mark = param.getMark();
            if(mark != null && !mark.isEmpty()){
                BoardDto.DetailResDto board = boardMapper.detail(Long.parseLong(mark));
                if(board != null){
                    mark = board.getTitle() + "_" + board.getId();
                    param.setMark(mark);
                }
            }
        }

        return detailList(boardMapper.scrollList(param));
    }


}
