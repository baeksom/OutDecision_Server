package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.notifications.service.NotificationService;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO.UploadPostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.service.PostServiceImpl;

import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    private final PostServiceImpl postServiceImpl;
    private final NotificationService notificationService;

    /* 등록 */
    @PostMapping(value = "", consumes = "multipart/form-data")
    @Operation(summary = "게시글 등록 API", description = "게시글을 작성하고 등록합니다.")
    public ApiResponse<Object> uploadPost(@RequestPart UploadPostDTO request,
                                        @RequestPart List<String> optionNames,
                                        @RequestPart(required = false) List<MultipartFile> optionImages) {
        return ApiResponse.onSuccess(postServiceImpl.uploadPost(request, optionNames, optionImages));
    }

    /* 조회 */
    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "상세 게시글을 조회합니다.")
    public ApiResponse<PostDTO> viewPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postServiceImpl.viewPost(postId));
    }

    /* 수정 */
    @PatchMapping (value = "/{postId}", consumes = "multipart/form-data")
    @Operation(summary = "게시글 수정 API", description = "게시글을 수정합니다.")
    public ApiResponse<Object> updatePost(@PathVariable Long postId,
                                          @RequestPart UploadPostDTO request,
                                          @RequestPart List<String> optionNames,
                                          @RequestPart List<MultipartFile> optionImages,
                                          @RequestPart List<String> originImages) {
        return ApiResponse.onSuccess(postServiceImpl.updatePost(postId, request, optionNames, optionImages, originImages));
    }

    /* 삭제 */
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제하고, 연결된 S3 이미지도 삭제됩니다.")
    public ApiResponse<Object> deletePost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postServiceImpl.deletePost(postId));
    }

    // 게시글 이메일 알림 on
    @PostMapping("/{postId}/notification")
    @Operation(summary = "게시글 알림 on", description = "사용자가 게시글 알림을 설정하여 투표 마감 시, 이메일 알림을 받을 수 있습니다.")
    public ApiResponse<Object> onNotification(@PathVariable Long postId) {
        return ApiResponse.onSuccess(notificationService.addNotifications(postId));
    }

    // 게시글 이메일 알림 off
    @DeleteMapping("/{postId}/notification")
    @Operation(summary = "게시글 알림 off", description = "사용자가 게시글 알림을 오프하여 투표 마감 시, 이메일 알림을 받는 것을 막을 수 있습니다.")
    public ApiResponse<Object> offNotification(@PathVariable Long postId) {
        return ApiResponse.onSuccess(notificationService.deleteNotifications(postId));
    }

    /* 끌어올리기 */
    @PostMapping("/{postId}/bumps")
    @Operation(summary = "게시글 끌어올리기", description = "게시글을 게시판 상단으로 올려준다.")
    public ApiResponse<Object> upPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postServiceImpl.topPost(postId));
    }
}
