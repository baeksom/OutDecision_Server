package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO.UploadPostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    boolean uploadPost(UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages);
    PostDTO viewPost(Long postId);
    boolean updatePost(Long postId, UploadPostDTO request, List<String> optionNames,
                       List<MultipartFile> newImages, List<String> originImages);
    boolean deletePost(Long postId);
    void turnsHot(Post post);
}
