package fr.just_abdel.chroniclesskills.repository;

import fr.just_abdel.chroniclescore.api.database.CoreDatabase;
import fr.just_abdel.chroniclescore.api.persistence.CoreRepository;
import fr.just_abdel.chroniclesskills.model.MasteryData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MasteryRepository extends CoreRepository<MasteryData, UUID> {

    private static final String TABLE_NAME = "mastery_data";

    public MasteryRepository(CoreDatabase database) {
        super(database, TABLE_NAME, 500, 5);
    }

    @Override
    protected String getIdColumn() {
        return "player_uuid";
    }

    @Override
    protected UUID getId(MasteryData entity) {
        return entity.getPlayerUuid();
    }

    @Override
    public MasteryData mapRow(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        MasteryData data = new MasteryData(playerUuid);

        data.setLourdeLevel(rs.getInt("lourde_level"));
        data.setLourdeXp(rs.getDouble("lourde_xp"));

        data.setMoyenneLevel(rs.getInt("moyenne_level"));
        data.setMoyenneXp(rs.getDouble("moyenne_xp"));

        data.setLegereLevel(rs.getInt("legere_level"));
        data.setLegereXp(rs.getDouble("legere_xp"));

        return data;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + TABLE_NAME + " (player_uuid, lourde_level, lourde_xp, moyenne_level, moyenne_xp, legere_level, legere_xp) VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + TABLE_NAME + " SET lourde_level = ?, lourde_xp = ?, moyenne_level = ?, moyenne_xp = ?, legere_level = ?, legere_xp = ? WHERE player_uuid = ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "lourde_level = VALUES(lourde_level), lourde_xp = VALUES(lourde_xp), moyenne_level = VALUES(moyenne_level), moyenne_xp = VALUES(moyenne_xp), legere_level = VALUES(legere_level), legere_xp = VALUES(legere_xp)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, MasteryData entity) throws SQLException {
        stmt.setString(1, entity.getPlayerUuid().toString());
        stmt.setInt(2, entity.getLourdeLevel());
        stmt.setDouble(3, entity.getLourdeXp());
        stmt.setInt(4, entity.getMoyenneLevel());
        stmt.setDouble(5, entity.getMoyenneXp());
        stmt.setInt(6, entity.getLegereLevel());
        stmt.setDouble(7, entity.getLegereXp());
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, MasteryData entity) throws SQLException {
        stmt.setInt(1, entity.getLourdeLevel());
        stmt.setDouble(2, entity.getLourdeXp());
        stmt.setInt(3, entity.getMoyenneLevel());
        stmt.setDouble(4, entity.getMoyenneXp());
        stmt.setInt(5, entity.getLegereLevel());
        stmt.setDouble(6, entity.getLegereXp());
        stmt.setString(7, entity.getPlayerUuid().toString());
    }

    @Override
    protected void prepareUpsert(PreparedStatement stmt, MasteryData entity) throws SQLException {
        prepareInsert(stmt, entity);
    }

    /**
     * Récupère les données de maîtrise pour un joueur donné
     */
    public CompletableFuture<MasteryData> getOrCreate(UUID playerUuid) {
        return findById(playerUuid).thenApply(opt -> opt.orElseGet(() -> new MasteryData(playerUuid)));
    }

    /**
     * Sauvegarde les données de maîtrise d'un joueur
     */
    public CompletableFuture<Void> save(MasteryData data) {
        return upsert(data);
    }
}
