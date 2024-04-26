package KGUcapstone.OutDecision.domain.user.service.duplication;

public interface DuplicationService {
    public boolean checkEmailExist(String request);
    public boolean checkNicknameExist(String request);
}
