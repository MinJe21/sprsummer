package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.domain.Board;
import com.thc.sprbasic2025.dto.BoardDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/board")
@RestController
public class BoardRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final BoardService boardService;
    public BoardRestController(BoardService boardService){
        this.boardService = boardService;
    }

    public Long getReqUserId(HttpServletRequest request){
        if(request.getAttribute("reqUserId") == null){
            return null;
        }
        return (Long) request.getAttribute("reqUserId");
    }

    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody BoardDto.CreateReqDto params, HttpServletRequest request){
        Long reqUserId = getReqUserId(request);
        params.setUserId(reqUserId);
        return ResponseEntity.ok(boardService.create(params));
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody BoardDto.UpdateReqDto params, HttpServletRequest request){
        Long reqUserId = getReqUserId(request);
        // 실제 사용자에게 받은 정보들은 모두 BoardDto.UpdateReqDto params에 담겨져 있음!

        // 서비스에 넘길 정보는 BoardDto.UpdateServDto newParam에 담아서 보내고 싶음!
        // reqUserId 를 넘기고 싶으니까! (혹은 다른 정보도 가능하지만, 아직은 이거 하나만)
        // newParam를 만들어서 넘길꺼야! 근데 만드는거 너무 귀찮으니까, BaseDto에 있는 카피 기능 활용해볼래.
        /*
        BoardDto.UpdateServDto newParam = (BoardDto.UpdateServDto) BoardDto.UpdateServDto.builder().reqUserId(reqUserId).build().afterBuild(params);
        */
        BoardDto.UpdateServDto newParam = BoardDto.UpdateServDto.builder().reqUserId(reqUserId).build(); //일단 newParam 만들기!
        newParam = (BoardDto.UpdateServDto) newParam.afterBuild(params); // params 에 있는 정보 다 카피하고 싶어!
        boardService.update(newParam);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody DefaultDto.DeleteReqDto params, HttpServletRequest request){
        boardService.delete(params);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDto.DetailResDto> detail(DefaultDto.DetailReqDto params, HttpServletRequest request){
        Long reqUserId = getReqUserId(request);
        params.setUserId(reqUserId);
        return ResponseEntity.ok(boardService.detail(params));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardDto.DetailResDto>> list(BoardDto.ListReqDto params
            , HttpServletRequest request
            , HttpServletResponse response
    ){
        logger.info("list : " + params.toString());
        logger.info("reqUserId : " + getReqUserId(request));
        return ResponseEntity.ok(boardService.list(params));
    }

    @GetMapping("/pagedList")
    public ResponseEntity<DefaultDto.PagedListResDto> pagedList(BoardDto.PagedListReqDto params){
        return ResponseEntity.ok(boardService.pagedList(params));
    }
    @GetMapping("/scrollList")
    public ResponseEntity<List<BoardDto.DetailResDto>> scrollList(BoardDto.ScrollListReqDto params){
        return ResponseEntity.ok(boardService.scrollList(params));
    }

}
