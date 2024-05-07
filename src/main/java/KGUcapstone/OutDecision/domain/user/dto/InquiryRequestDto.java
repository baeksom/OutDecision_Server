package KGUcapstone.OutDecision.domain.user.dto;

import lombok.Getter;

public class InquiryRequestDto {

    @Getter
    public static class InquiryPasswordDto {
        String name;
        String email;
    }
}
