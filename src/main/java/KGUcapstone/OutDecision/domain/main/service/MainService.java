package KGUcapstone.OutDecision.domain.main.service;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;

public interface MainService {
    PostListDTO getMain();

    PostDTO post(Post post);
}
