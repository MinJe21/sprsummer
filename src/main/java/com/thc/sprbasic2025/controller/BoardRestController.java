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

    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody BoardDto.CreateReqDto params){
        return ResponseEntity.ok(boardService.create(params));
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody BoardDto.UpdateReqDto params){
        boardService.update(params);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody DefaultDto.DeleteReqDto params){
        boardService.delete(params);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDto.DetailResDto> detail(DefaultDto.DetailReqDto params){
        return ResponseEntity.ok(boardService.detail(params));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardDto.DetailResDto>> list(BoardDto.ListReqDto params
            , HttpServletRequest request
            , HttpServletResponse response
    ){
        logger.info("list : " + params.toString());
        logger.info("request test!!: " + request.getHeader("testtoken"));
        logger.info("tempValue : " + request.getAttribute("tempValue"));
        logger.info("temp1122 : " + response.getHeader("temp1122"));
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
