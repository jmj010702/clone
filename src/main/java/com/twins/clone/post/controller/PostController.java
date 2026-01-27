package com.twins.clone.post.controller;

import com.twins.clone.post.dto.PostCreateDto;
import com.twins.clone.post.dto.PostDetailDto;
import com.twins.clone.post.dto.PostListDto;
import com.twins.clone.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody PostCreateDto pc_dto, @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername(); //jwt의 sub값(이메일)
        postService.save(pc_dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 작성됨");
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("성공적으로 삭제됨");
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{id}")
    public PostDetailDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }


    @GetMapping("/posts")
    public Page<PostListDto> findAll(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.pageFindAll(pageable);
    }


}
