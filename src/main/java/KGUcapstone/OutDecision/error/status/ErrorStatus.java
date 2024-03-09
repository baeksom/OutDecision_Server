package umc.easyexcel.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.easyexcel.apiPayload.code.BaseErrorCode;
import umc.easyexcel.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // Functions
    FUNCTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "FUNCTION4001", "존재하지 않는 함수입니다."),

    // ShortcutKey
    SHORTCUTKEY_NOT_FOUND(HttpStatus.BAD_REQUEST, "SHORTCUTKEY400", "해당하는 단축키를 찾을 수 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER400", "잘못된 요청 파라미터입니다."),
    INVALID_SHORTCUTKEY_CATEGORY(HttpStatus.BAD_REQUEST, "INVALID_SHORTCUTKEY_CATEGORY400", "잘못된 단축키 카테고리입니다."),
    EMPTY_SEARCH(HttpStatus.OK, "EMPTY_SEARCH", "검색 결과가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
