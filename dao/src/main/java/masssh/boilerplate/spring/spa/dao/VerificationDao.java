package masssh.boilerplate.spring.spa.dao;

import masssh.boilerplate.spring.spa.model.row.VerificationRow;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Mapper
public interface VerificationDao {
    @Select("SELECT * FROM verification WHERE verificationId = #{verificationId}")
    Optional<VerificationRow> single(@Param("verificationId") long verificationId);

    @Select("SELECT * FROM verification WHERE verificationHash = #{verificationHash}")
    Optional<VerificationRow> singleByVerificationHash(@Param("verificationHash") String verificationHash);

    @Select("SELECT * FROM verification WHERE userId = #{userId}")
    Optional<VerificationRow> singleByUserId(@Param("userId") long userId);

    @Insert("INSERT INTO verification (verificationHash, userId, verificationType, expiresAt, createdAt, updatedAt)" +
            "VALUES ( #{verificationHash}, #{userId}, #{verificationType}, #{expiresAt}, #{createdAt}, #{updatedAt} )")
    @SelectKey(statement = "SELECT @@IDENTITY", keyProperty = "verificationId", before = false, resultType = long.class)
    void create(VerificationRow verificationRow) throws SQLIntegrityConstraintViolationException;

    @Update("UPDATE verification SET verificationType=#{verificationType}, expiresAt=#{expiresAt}, updatedAt=#{updatedAt} WHERE verificationId = #{verificationId}")
    void update(VerificationRow verificationRow) throws SQLIntegrityConstraintViolationException;

    @Delete("DELETE FROM verification WHERE verificationId = #{verificationId}")
    boolean delete(@Param("verificationId") long verificationId);
}
