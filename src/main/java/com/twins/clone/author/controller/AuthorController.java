package com.twins.clone.author.controller;

import com.twins.clone.author.dto.*;
import com.twins.clone.author.entity.Author;
import com.twins.clone.author.service.AuthorService;
import com.twins.clone.common.auth.JwtTokenProvider;
import com.twins.clone.common.security.CustomUserDetails;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthorController(AuthorService authorService, JwtTokenProvider jwtTokenProvider) {
        this.authorService = authorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AuthorCreateDto ac_dto) {
        authorService.save(ac_dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthorLoginDto al_dto) {
        Author author = authorService.login(al_dto);

        String token = jwtTokenProvider.createToken(author);
        return token;
    }
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id ,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authorService.delete(id, customUserDetails.getAuthorid(), customUserDetails.getRole());
        return ResponseEntity.status(HttpStatus.OK).body("Delete OK");
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{id}")
    public AuthorDetailDto findById(@PathVariable Long id) {
        AuthorDetailDto ad_dto = authorService.findById(id);
        return ad_dto;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return authorService.findAll();

    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PatchMapping("/update")
    public void updatePW(@RequestBody AuthorUpdateDto au_dto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        authorService.updatepw(au_dto);
    }

}
