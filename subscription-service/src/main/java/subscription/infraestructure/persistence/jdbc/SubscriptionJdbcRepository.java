package subscription.infraestructure.persistence.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import subscription.application.port.out.ApplicationLogger;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SubscriptionJdbcRepository implements SubscriptionRepository {

    private final JdbcTemplate jdbc;
    private final ApplicationLogger logger;

    public SubscriptionJdbcRepository(JdbcTemplate jdbc, ApplicationLogger logger) {
        this.jdbc = jdbc;
        this.logger = logger;
    }

    @Override
    public boolean existsActiveByUserId(UUID userId) {

        Integer count = jdbc.queryForObject("""
            SELECT COUNT(1)
            FROM subscriptions
            WHERE user_id = ?
              AND status = 'ATIVA'
        """, Integer.class, userId);

        return count != null && count > 0;
    }

    @Override
    public Optional<Subscription> findActiveByUserId(UUID userId) {

        return queryForOptional("""
            SELECT *
            FROM subscriptions
            WHERE user_id = ?
              AND status = 'ATIVA'
        """, userId);
    }

    @Override
    public Optional<Subscription> findById(long id) {

        return queryForOptional("""
            SELECT *
            FROM subscriptions
            WHERE id = ?
        """, id);
    }

    @Override
    public Optional<Subscription> findByUserId(UUID userId) {

        return queryForOptional("""
            SELECT *
            FROM subscriptions
            WHERE user_id = ?
        """, userId);
    }

    @Override
    public Optional<Subscription> findByUserIdToRenew(UUID userId) {
        return queryForOptional("""
            SELECT *
            FROM subscriptions
            WHERE user_id = ?
        """, userId);
    }

    @Override
    public Subscription save(Subscription subscription) {

        jdbc.update("""
        INSERT INTO subscriptions (
            user_id, plan, status, start_date, expiration_date, failed_renewal_attempts
        )
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (user_id) DO UPDATE SET
            plan = EXCLUDED.plan,
            status = EXCLUDED.status,
            start_date = EXCLUDED.start_date,
            expiration_date = EXCLUDED.expiration_date,
            failed_renewal_attempts = EXCLUDED.failed_renewal_attempts;
    """,
                subscription.getUserId(),
                subscription.getPlan().name(),
                subscription.getStatus().name(),
                Timestamp.valueOf(subscription.getStartDate().atStartOfDay()),
                Timestamp.valueOf(subscription.getExpirationDate().atStartOfDay()),
                subscription.getfailedRenewalAttempts()
        );

        return subscription;
    }

    @Override
    @Transactional
    public List<Subscription> findExpiredAndActive(LocalDate now) {

        List<Subscription> subscription = jdbc.query("""
        SELECT *
        FROM subscriptions
        WHERE status = 'ATIVA'
          AND expiration_date < ?
        FOR UPDATE SKIP LOCKED
    """, this::mapRow, now);

        for (Subscription sub : subscription) {
            jdbc.update("""
            UPDATE subscriptions
            SET status = 'RENOVANDO'
            WHERE id = ?
        """, sub.getId());
        }
        return subscription;
    }


        private Optional<Subscription> queryForOptional(String sql, Object... args) {

        List<Subscription> result =
                jdbc.query(sql, this::mapRow, args);

        return result.stream().findFirst();
    }

    private Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Subscription(
                rs.getLong("id"),
                UUID.fromString(rs.getString("user_id")),
                Plan.valueOf(rs.getString("plan")),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("expiration_date").toLocalDate(),
                SubscriptionStatus.valueOf(rs.getString("status")),
                rs.getInt("failed_renewal_attempts")
        );
    }
}
