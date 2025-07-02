package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.dto.NoticeDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/notice")
@RequiredArgsConstructor
@RestController
public class NoticeRestController {

    final NoticeService noticeService;

    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody NoticeDto.CreateReqDto params){
        return ResponseEntity.ok(noticeService.create(params));
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody NoticeDto.UpdateReqDto params){
        noticeService.update(params);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody DefaultDto.DeleteReqDto params){
        noticeService.delete(params);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/detail")
    public ResponseEntity<NoticeDto.DetailResDto> detail(DefaultDto.DetailReqDto params){
        return ResponseEntity.ok(noticeService.detail(params));
    }

    @GetMapping("/list")
    public ResponseEntity<List<NoticeDto.DetailResDto>> list(NoticeDto.ListReqDto params){
        return ResponseEntity.ok(noticeService.list(params));
    }

    @GetMapping("/pagedList")
    public ResponseEntity<DefaultDto.PagedListResDto> pagedList(NoticeDto.PagedListReqDto params){
        return ResponseEntity.ok(noticeService.pagedList(params));
    }
    @GetMapping("/scrollList")
    public ResponseEntity<List<NoticeDto.DetailResDto>> scrollList(NoticeDto.ScrollListReqDto params){
        return ResponseEntity.ok(noticeService.scrollList(params));
    }

}
