package io.huy8895.ipldashboard.data;

import io.huy8895.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {
        Match match = Match.builder()
                           .id(Long.valueOf(matchInput.getId()))
                           .date(LocalDate.parse(matchInput.getDate()))
                           .city(matchInput.getCity())
                           .playerOfMatch(matchInput.getPlayer_of_match())
                           .tossWinner(matchInput.getToss_winner())
                           .tossDecision(matchInput.getToss_decision())
                           .matchWinner(matchInput.getWinner())
                           .result(matchInput.getResult())
                           .resultMargin(matchInput.getResult_margin())
                           .umpire1(matchInput.getUmpire1())
                           .umpire2(matchInput.getUmpire2())
                           .build();
        String firstInningTeam, secondInningTeam;

        if ("bat".equals(matchInput.getToss_decision())) {
            firstInningTeam = matchInput.getToss_winner();
            secondInningTeam = matchInput.getToss_winner()
                                         .equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        } else {
            secondInningTeam = matchInput.getToss_winner();
            firstInningTeam = matchInput.getToss_winner()
                                        .equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        match.setTeam1(firstInningTeam);
        match.setTeam2(secondInningTeam);
        return match;
    }

}
