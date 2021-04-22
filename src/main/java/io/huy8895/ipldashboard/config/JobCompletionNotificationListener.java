package io.huy8895.ipldashboard.config;


import io.huy8895.ipldashboard.model.Match;
import io.huy8895.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            Map<String, Team> teamData = new HashMap<>();
            entityManager.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                         .getResultList()
                         .stream()
                         .map(e -> Team.builder()
                                       .teamName((String) e[0])
                                       .totalMatches((Long) e[1])
                                       .build())
                         .forEach(team -> teamData.put(team.getTeamName(), team));

            entityManager.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                         .getResultList()
                         .forEach(e -> {
                             Optional<Team> optionalTeam = Optional.ofNullable(teamData.get((String) e[0]));
                             optionalTeam.ifPresent(team -> team.setTotalMatches(team.getTotalMatches() + (Long) e[1]));
                         });

            entityManager.createQuery(" select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
            .getResultList()
            .forEach(e -> {
                Optional<Team> optionalTeam = Optional.ofNullable(teamData.get((String) e[0]));
                optionalTeam.ifPresent(team -> team.setTotalWins((Long) e[1]));
            });

            teamData.values().forEach(entityManager::persist);
            teamData.values().forEach(System.out::println);
        }
    }
}
