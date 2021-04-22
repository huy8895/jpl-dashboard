package io.huy8895.ipldashboard.controller;

import io.huy8895.ipldashboard.model.Match;
import io.huy8895.ipldashboard.model.PersonDTO;
import io.huy8895.ipldashboard.model.RequestDTO;
import io.huy8895.ipldashboard.model.Team;
import io.huy8895.ipldashboard.repository.MatchRepository;
import io.huy8895.ipldashboard.repository.TeamRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class TeamController {

    private final TeamRepository teamRepository;

    private final MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName,
                        RequestDTO requestDTO) {
        Team team = teamRepository.findByTeamName(teamName);
        List<Match> matches = matchRepository.getLatestMatchesByTeam(teamName, 2);
        team.setMatches(matches);
        log.info("requestDTO :" + requestDTO);

        return team;
    }

    @RequestMapping(value = "/getWithoutRequestParam",method = RequestMethod.GET)
    public List<PersonDTO> getWithoutRequestParam(PersonDTO personDTO) {
        return Arrays.asList(personDTO);

    }
}
