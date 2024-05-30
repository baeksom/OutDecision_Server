package KGUcapstone.OutDecision.domain.user.service.mypage;

import org.springframework.web.multipart.MultipartFile;

public interface UserImgService {

    boolean updateUserImg(MultipartFile userImg);

    // 프로필 사진 삭제 -> 기본 이미지 변경
    boolean deleteUserImg();
}
