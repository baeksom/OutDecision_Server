package KGUcapstone.OutDecision.domain.user.dto;

import lombok.Getter;

public class UpdateRequestDTO {
    @Getter
    public static class UpdateMemberDTO{
        String name;
        String nickname;
    }

    @Getter
    public static class UpdatePasswordDTO{
        String currentPassword;
        String newPassword;
        String confirmNewPassword;
    }

    @Getter
    public static class UpdateUserImgDTO{
        String newImg;
    }

    @Getter
    public static class UpdateTitleDTO{
        String title;
    }
  
}
