package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.user.service.mypage.MyActivityService;
import KGUcapstone.OutDecision.domain.user.service.mypage.MyPageService;
import KGUcapstone.OutDecision.domain.user.service.mypage.TitleService;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO.MyPageDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateMemberDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateTitleDTO;
import KGUcapstone.OutDecision.domain.user.service.mypage.MemberService;
import KGUcapstone.OutDecision.domain.user.service.mypage.PasswordService;
import KGUcapstone.OutDecision.domain.user.service.mypage.UserImgService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import static KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {

    private final MyActivityService myActivityService;
    private final MemberService memberService;
    private final PasswordService passwordService;
    private final UserImgService userImgService;
    private final MyPageService myPageService;
    private final TitleService titleService;
    private final PostConverter postConverter;

    @GetMapping("/posting")
    @Operation(summary = "마이페이지 작성글 API", description = "사용자가 작성한 글을 게시판 보드로 조회하는 API이며, 페이징을 포함합니다.")
    public ApiResponse<PostListDTO> getMyPostList(
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        // 페이지 번호를 1부터 시작하도록 변환
        Integer adjustedPage = page - 1;
        Page<Post> myPostPage = myActivityService.getMyPostListByStatus(status, adjustedPage);
        return ApiResponse.onSuccess(postConverter.toPostListDTO(myPostPage));
    }

    @GetMapping("/liked")
    @Operation(summary = "마이페이지 좋아요한 글 API", description = "사용자가 좋아요한 글을 게시판 보드로 조회하는 API이며, 페이징을 포함합니다.")
    public ApiResponse<PostListDTO> getLikedPostList(
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        // 페이지 번호를 1부터 시작하도록 변환
        Integer adjustedPage = page - 1;
        Page<Post> likedPostPage = myActivityService.getLikedPostListByStatus(status, adjustedPage);
        return ApiResponse.onSuccess(postConverter.toPostListDTO(likedPostPage));
    }

    @GetMapping("/vote")
    @Operation(summary = "마이페이지 투표한 글 API", description = "사용자가 투표한 글을 게시판 보드로 조회하는 API이며, 페이징을 포함합니다.")
    public ApiResponse<PostListDTO> getVotedPostList(
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        // 페이지 번호를 1부터 시작하도록 변환
        Integer adjustedPage = page - 1;
        Page<Post> votedPostPage = myActivityService.getVotedPostListByStatus(status, adjustedPage);
        return ApiResponse.onSuccess(postConverter.toPostListDTO(votedPostPage));
    }

    @GetMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 조회 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 조회합니다.")
    public ApiResponse<MemberInfoDTO> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberInfoDTO memberDTO = memberService.getMemberById(memberId);
        return ApiResponse.onSuccess(memberDTO);
    }

    @PatchMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 수정합니다.")
    public ApiResponse<Object> updateMemberInfo(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberDTO request) {
        boolean success = memberService.updateMemberInfo(memberId, request);
        if (success) return ApiResponse.onSuccess("개인정보가 성공적으로 수정되었습니다.");
        else return ApiResponse.onFailure("400", "개인정보 수정에 실패하였습니다.", null);
    }

    @PatchMapping("/{memberId}/edit/password")
    @Operation(summary = "마이페이지 비밀번호 변경", description = "비밀번호를 변경합니다.")
    public ApiResponse<Object> updatePassword(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdatePasswordDTO request) {
        boolean success = passwordService.updatePassword(memberId, request);
        if (success) return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
        else return ApiResponse.onFailure("400", "비밀번호 변경에 실패하였습니다.", null);
    }

    @PatchMapping(value = "/{memberId}/edit/profile", consumes = "multipart/form-data")
    @Operation(summary = "마이페이지 프로필 사진 변경", description = "프로필 사진을 변경합니다.")
    public ApiResponse<Object> updateUserImg(@PathVariable("memberId") Long memberId, @RequestPart(value = "userImg") MultipartFile userImg) {
        boolean success = userImgService.updateUserImg(memberId, userImg);
        if (success) return ApiResponse.onSuccess("프로필 사진이 성공적으로 변경되었습니다.");
        else return ApiResponse.onFailure("400", "프로필 사진 변경에 실패하였습니다.", null);
    }

    @PatchMapping("/{memberId}/delete/profile")
    @Operation(summary = "마이페이지 프로필 사진 삭제", description = "프로필 사진을 삭제합니다.")
    public ApiResponse<Object> deleteUserImg(@PathVariable("memberId") Long memberId) {
        boolean success = userImgService.deleteUserImg(memberId);
        if (success) return ApiResponse.onSuccess("프로필 사진이 성공적으로 삭제되었습니다.");
        else return ApiResponse.onFailure("400", "프로필 사진 삭제에 실패하였습니다.", null);
    }

    @GetMapping()
    @Operation(summary = "마이페이지 홈 API", description = "마이페이지 홈을 조회하는 API입니다.")
    public ApiResponse<MyPageDTO> getMyPostList(@RequestParam(name = "posts", required = false) String posts) {
        return ApiResponse.onSuccess(myPageService.getMyPage(posts));
    }

    @GetMapping("/{memberId}/title")
    @Operation(summary = "마이페이지 칭호 조회 API", description = "마이페이지에서 보유 칭호를 조회하는 API입니다.")
    public ApiResponse<List<String>> updateTitle(@PathVariable("memberId") Long memberId) {
        List<String> myTitlesDTO = titleService.myTitlesDTO(memberId);
        return ApiResponse.onSuccess(myTitlesDTO);
    }

    @PutMapping("/{memberId}/title")
    @Operation(summary = "마이페이지 칭호 변경 API", description = "마이페이지 홈에서 칭호를 변경하는 API입니다.")
    public ApiResponse<Object> updateTitle(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateTitleDTO request) {
        boolean success = titleService.updateUserTitle(memberId, request);
        if (success) return ApiResponse.onSuccess("칭호가 성공적으로 변경되었습니다.");
        else return ApiResponse.onFailure("400", "칭호 변경에 실패하였습니다.", null);
    }
}
