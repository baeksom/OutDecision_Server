package KGUcapstone.OutDecision.domain.vote.service;

import java.util.List;

public interface VoteService {
    public boolean addVote(List<Long> optionsIds);
}
