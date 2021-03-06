package io.huy8895.ipldashboard.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    private Long id;

    private String city;

    private LocalDate date;

    private String playerOfMatch;

    private String venue;

    private String neutralVenue;

    private String team1;

    private String team2;

    private String tossWinner;

    private String tossDecision;

    private String matchWinner;

    private String result;

    private String resultMargin;

    private String umpire1;

    private String umpire2;

}
