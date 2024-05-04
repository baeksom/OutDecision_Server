package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserImgServiceImpl implements UserImgService{
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    // 프로필 사진 변경
    @Override
    public boolean updateUserImg(Long memberId, MultipartFile userImg) {
        Member member = memberRepository.findById(memberId).get();
        // 기본 이미지가 아니라면 기존 s3에 업로드된 사진 삭제
        if (!member.getUserImg().equals("https://kr.object.ncloudstorage.com/outdecisionbucket/profile/3b0ae8ae-78b6-4a05-86fc-070900b8b763.png")) {
            s3Service.deleteImage(member.getUserImg());
        }

        String profileImage = s3Service.uploadFile(userImg, "profile");

        member.updateUserImg(profileImage);
        memberRepository.save(member);

        return true;
    }

    // 프로필 사진 삭제 -> 기본 이미지 변경
    @Override
    public boolean deleteUserImg(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        // 기본 이미지가 아니라면 기존 s3에 업로드된 사진 삭제
        if (!member.getUserImg().equals("https://kr.object.ncloudstorage.com/outdecisionbucket/profile/3b0ae8ae-78b6-4a05-86fc-070900b8b763.png")) {
            s3Service.deleteImage(member.getUserImg());
        }

        member.updateUserImg("https://kr.object.ncloudstorage.com/outdecisionbucket/profile/3b0ae8ae-78b6-4a05-86fc-070900b8b763.png");
        memberRepository.save(member);

        return true;
    }
}
