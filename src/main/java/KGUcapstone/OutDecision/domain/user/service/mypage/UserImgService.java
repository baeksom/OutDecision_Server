package KGUcapstone.OutDecision.domain.user.service.mypage;

import org.springframework.web.multipart.MultipartFile;

public interface UserImgService {
    String updateUserImg(MultipartFile userImg);
    String deleteUserImg();
}
