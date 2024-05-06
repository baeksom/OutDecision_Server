package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    /* 등록 */
    boolean uploadPost(PostRequestDTO.UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages);
    PostDTO viewPost(Long postId);
    boolean deletePost(Long postId);
}
