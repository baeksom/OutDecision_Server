package KGUcapstone.OutDecision.domain.user.duplication.service;

public interface DuplicationService {
    public boolean checkEmailExist(String request);
    public boolean checkNicknameExist(String request);
    public boolean checkPhoneExist(String request);
}
